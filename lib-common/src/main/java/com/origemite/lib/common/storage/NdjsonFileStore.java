package com.origemite.lib.common.storage;

import com.origemite.lib.common.util.TransformUtils;

import java.io.IOException;
import tools.jackson.core.JacksonException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

/**
 * 연동 설정과 토큰을 로컬 NDJSON 파일에 저장하는 공통 저장소.
 * NDJSON은 한 줄에 JSON 객체 하나를 기록하는 형식이다.
 * 단일 설정은 One 계열, 여러 토큰처럼 목록으로 관리하는 데이터는 All 계열 메서드를 사용한다.
 *
 * <p>같은 파일 이름의 작업은 이 저장소 인스턴스 안에서 한 번에 하나씩 수행한다.
 * 서로 다른 파일은 병렬로 처리할 수 있으며, 다른 프로세스나 저장소 인스턴스의 접근까지 막지는 않는다.
 * 여러 파일을 묶는 DB 트랜잭션도 제공하지 않는다.
 *
 * <p>기존 값을 읽어 일부만 변경하려면 {@link #mutateOne} 또는 {@link #mutateAll}을 사용한다.
 * read와 write를 따로 호출하면 두 호출 사이에 다른 요청이 저장한 내용을 덮어쓸 수 있다.
 * 쓰기는 임시 파일을 완성한 뒤 원본을 교체하며, 목록 저장도 추가 기록이 아닌 파일 전체 교체다.
 * JSON 읽기·쓰기 실패는 {@link IllegalStateException}으로 전달한다.
 */
public class NdjsonFileStore {

    private final Path directory;
    // 같은 파일 이름을 사용하는 작업들이 공유하는 JVM 내부 잠금 객체.
    private final ConcurrentHashMap<String, Object> fileLocks = new ConcurrentHashMap<>();

    /** 같은 디렉터리에는 저장소 인스턴스 하나를 공유하여 사용한다. */
    public NdjsonFileStore(Path directory) {
        this.directory = java.util.Objects.requireNonNull(directory, "directory");
    }

    /**
     * 파일의 첫 번째 비어 있지 않은 줄을 객체 하나로 읽는다. 이후 줄은 읽지 않는다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param type JSON을 변환할 클래스. 예: Settings.class
     * @return 읽은 객체. 파일이 없거나 비어 있으면 null
     */
    public <T> T readOne(String fileName, Class<T> type) {
        synchronized (lockFor(fileName)) {
            return readOneUnlocked(fileName, type);
        }
    }

    /**
     * 객체 하나를 JSON 한 줄로 저장하여 기존 파일 내용을 교체한다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param value 저장할 객체. null이면 원본 파일과 남아 있는 임시 파일 삭제
     */
    public <T> void writeOne(String fileName, T value) {
        synchronized (lockFor(fileName)) {
            writeOneUnlocked(fileName, value);
        }
    }

    /**
     * 단일 객체의 읽기 → 변경 함수 실행 → 저장을 같은 파일 잠금 안에서 수행한다.
     * 연동 설정처럼 기존 값 중 일부만 변경할 때 사용한다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param type JSON을 변환할 클래스
     * @param mutator 현재 객체를 받아 저장할 객체를 반환하는 함수. 파일이 없으면 null을 전달받으며,
     *                함수가 null을 반환하면 파일 삭제. 현재 값이 null이어도 새 객체를 반환하면 생성
     * @return 변경 후 저장한 객체. 삭제한 경우 null
     */
    public <T> T mutateOne(String fileName, Class<T> type, UnaryOperator<T> mutator) {
        synchronized (lockFor(fileName)) {
            T current = readOneUnlocked(fileName, type);
            T next = mutator.apply(current);
            writeOneUnlocked(fileName, next);
            return next;
        }
    }

    /**
     * 비어 있지 않은 각 줄을 객체로 변환하여 파일 순서대로 반환한다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param type 각 JSON 줄을 변환할 클래스
     * @return 읽은 목록. 파일이 없거나 비어 있으면 빈 목록
     */
    public <T> List<T> readAll(String fileName, Class<T> type) {
        synchronized (lockFor(fileName)) {
            return readAllUnlocked(fileName, type);
        }
    }

    /**
     * 목록의 각 객체를 JSON 한 줄씩 기록하여 파일 전체를 교체한다. 기존 내용에 추가하지 않는다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param values 저장할 목록. null이나 빈 목록이면 빈 파일을 저장하고, 목록 안의 null 항목은 생략
     */
    public <T> void writeAll(String fileName, List<T> values) {
        synchronized (lockFor(fileName)) {
            writeAllUnlocked(fileName, values);
        }
    }

    /**
     * 목록의 읽기 → 변경 함수 실행 → 전체 저장을 같은 파일 잠금 안에서 수행한다.
     * 토큰 목록에서 특정 제공자의 항목을 추가·교체·삭제할 때 사용한다.
     *
     * @param fileName 저장 디렉터리 기준 파일 이름
     * @param type 각 JSON 줄을 변환할 클래스
     * @param mutator 수정 가능한 현재 목록을 받아 저장할 목록을 반환하는 함수.
     *                파일이 없으면 빈 목록을 전달받으며, null을 반환하면 빈 파일 저장
     * @return 변경 결과의 수정 불가능한 목록 복사본. 함수가 null을 반환하면 빈 목록.
     *         반환 목록에는 null 항목을 포함할 수 없음
     */
    public <T> List<T> mutateAll(String fileName, Class<T> type, UnaryOperator<List<T>> mutator) {
        synchronized (lockFor(fileName)) {
            List<T> current = readAllUnlocked(fileName, type);
            List<T> next = mutator.apply(new ArrayList<>(current));
            writeAllUnlocked(fileName, next == null ? List.of() : next);
            return next == null ? List.of() : List.copyOf(next);
        }
    }

    /**
     * 원본 파일과 같은 이름의 .tmp 임시 파일을 삭제한다. 파일이 없어도 정상 처리한다.
     */
    public void delete(String fileName) {
        synchronized (lockFor(fileName)) {
            deleteUnlocked(fileName);
        }
    }

    /** 같은 파일 이름에 항상 같은 잠금 객체를 배정하여 동시 읽기·쓰기 순서를 조정한다. */
    private Object lockFor(String fileName) {
        return fileLocks.computeIfAbsent(fileName, key -> new Object());
    }

    // Unlocked 메서드는 자체 잠금이 없으므로 호출부에서 해당 파일의 잠금을 잡아야 한다.
    private <T> T readOneUnlocked(String fileName, Class<T> type) {
        Path file = directory.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            return null;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                return TransformUtils.parseFileValue(line, type);
            }
            return null;
        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException("settings ndjson read failed: " + file, exception);
        }
    }

    private <T> void writeOneUnlocked(String fileName, T value) {
        if (value == null) {
            deleteUnlocked(fileName);
            return;
        }
        try {
            ensureDirectory();
            Path file = directory.resolve(fileName);
            Path temp = directory.resolve(fileName + ".tmp");
            String line = TransformUtils.stringifyFileValue(value);
            Files.writeString(temp, line + System.lineSeparator(), StandardCharsets.UTF_8);
            moveReplace(temp, file);
        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException("settings ndjson write failed: " + fileName, exception);
        }
    }

    private <T> List<T> readAllUnlocked(String fileName, Class<T> type) {
        Path file = directory.resolve(fileName);
        if (!Files.isRegularFile(file)) {
            return List.of();
        }
        try {
            List<T> items = new ArrayList<>();
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                items.add(TransformUtils.parseFileValue(line, type));
            }
            return items;
        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException("settings ndjson readAll failed: " + file, exception);
        }
    }

    private <T> void writeAllUnlocked(String fileName, List<T> values) {
        try {
            ensureDirectory();
            Path file = directory.resolve(fileName);
            Path temp = directory.resolve(fileName + ".tmp");
            StringBuilder body = new StringBuilder();
            if (values != null) {
                for (T value : values) {
                    if (value == null) {
                        continue;
                    }
                    body.append(TransformUtils.stringifyFileValue(value)).append(System.lineSeparator());
                }
            }
            Files.writeString(temp, body.toString(), StandardCharsets.UTF_8);
            moveReplace(temp, file);
        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException("settings ndjson writeAll failed: " + fileName, exception);
        }
    }

    private void deleteUnlocked(String fileName) {
        try {
            Files.deleteIfExists(directory.resolve(fileName));
            Files.deleteIfExists(directory.resolve(fileName + ".tmp"));
        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException("settings ndjson delete failed: " + fileName, exception);
        }
    }

    /** 저장 디렉터리가 없으면 상위 경로까지 함께 생성한다. */
    private void ensureDirectory() throws IOException {
        Files.createDirectories(directory);
    }

    /**
     * 임시 파일로 원본을 교체한다. 파일 시스템이 지원하면 원자적 이동을 우선 사용한다.
     * 실패하면 일반 교체를 시도하므로 모든 환경에서 원자적 교체가 보장되는 것은 아니다.
     */
    private static void moveReplace(Path temp, Path file) throws IOException {
        try {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ignored) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
