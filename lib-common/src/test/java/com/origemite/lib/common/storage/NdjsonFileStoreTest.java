package com.origemite.lib.common.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("단위 / NDJSON 파일 동시성")
class NdjsonFileStoreTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("mutateOne 은 같은 파일에 대한 증가를 직렬화한다")
    void mutateOneSerializesIncrements() throws Exception {
        NdjsonFileStore store = new NdjsonFileStore(tempDir);
        String file = "counter.ndjson";
        store.writeOne(file, new Counter(0));

        int threads = 16;
        int incrementsPerThread = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                start.await();
                for (int n = 0; n < incrementsPerThread; n++) {
                    store.mutateOne(file, Counter.class, current -> {
                        int value = current == null ? 0 : current.value;
                        return new Counter(value + 1);
                    });
                }
                return null;
            }));
        }
        start.countDown();
        for (Future<?> future : futures) {
            future.get(10, TimeUnit.SECONDS);
        }
        pool.shutdown();

        assertThat(store.readOne(file, Counter.class).value).isEqualTo(threads * incrementsPerThread);
    }

    @Test
    @DisplayName("서로 다른 파일은 동시에 mutate 할 수 있다")
    void differentFilesDoNotBlockEachOther() throws Exception {
        NdjsonFileStore store = new NdjsonFileStore(tempDir);
        AtomicInteger concurrent = new AtomicInteger();
        AtomicInteger maxConcurrent = new AtomicInteger();
        CountDownLatch bothEntered = new CountDownLatch(2);
        CountDownLatch release = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<?> a = pool.submit(() -> store.mutateOne("a.ndjson", Counter.class, current ->
                hold(concurrent, maxConcurrent, bothEntered, release)));
        Future<?> b = pool.submit(() -> store.mutateOne("b.ndjson", Counter.class, current ->
                hold(concurrent, maxConcurrent, bothEntered, release)));
        assertThat(bothEntered.await(3, TimeUnit.SECONDS)).isTrue();
        release.countDown();
        a.get(3, TimeUnit.SECONDS);
        b.get(3, TimeUnit.SECONDS);
        pool.shutdown();

        assertThat(maxConcurrent.get()).isGreaterThanOrEqualTo(2);
        assertThat(store.readOne("a.ndjson", Counter.class).value).isEqualTo(1);
        assertThat(store.readOne("b.ndjson", Counter.class).value).isEqualTo(1);
    }

    @Test
    @DisplayName("파일용 공통 변환기는 ISO 날짜의 소수 초와 알 수 없는 필드를 처리한다")
    void preservesFileDatePrecisionAndIgnoresUnknownFields() throws Exception {
        NdjsonFileStore store = new NdjsonFileStore(tempDir);
        LocalDateTime time = LocalDateTime.of(2026, 10, 29, 12, 30, 0, 123456789);
        store.writeOne("date.ndjson", new TimestampValue(time));
        assertThat(store.readOne("date.ndjson", TimestampValue.class).time()).isEqualTo(time);

        Files.writeString(tempDir.resolve("old.ndjson"),
                "{\"time\":\"2026-10-29T12:30:00.123456789\",\"extra\":true}\n");
        assertThat(store.readOne("old.ndjson", TimestampValue.class).time()).isEqualTo(time);
        Files.writeString(tempDir.resolve("legacy.ndjson"),
                "{\"time\":\"2026-10-29 12:30:00\"}\n");
        assertThat(store.readOne("legacy.ndjson", TimestampValue.class).time()).isEqualTo(time.withNano(0));
    }

    @Test
    @DisplayName("문자열의 줄바꿈은 JSON 안에서 이스케이프하여 한 줄씩 저장한다")
    void storesStringsAsSingleJsonLines() throws Exception {
        NdjsonFileStore store = new NdjsonFileStore(tempDir);
        List<String> values = List.of("first\nsecond", "quoted \"value\"");
        store.writeAll("strings.ndjson", values);
        assertThat(Files.readAllLines(tempDir.resolve("strings.ndjson"))).hasSize(2);
        assertThat(store.readAll("strings.ndjson", String.class)).containsExactlyElementsOf(values);
    }

    public record TimestampValue(LocalDateTime time) {
    }

    private static Counter hold(
            AtomicInteger concurrent,
            AtomicInteger maxConcurrent,
            CountDownLatch bothEntered,
            CountDownLatch release) {
        try {
            int now = concurrent.incrementAndGet();
            maxConcurrent.accumulateAndGet(now, Math::max);
            bothEntered.countDown();
            release.await(3, TimeUnit.SECONDS);
            concurrent.decrementAndGet();
            return new Counter(1);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }

    public static final class Counter {
        public int value;

        public Counter() {
        }

        public Counter(int value) {
            this.value = value;
        }
    }
}
