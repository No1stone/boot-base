# lib-message-saga

MSA 앱 Gradle에 넣으면 **발행 + 컨슘**을 제공하는 클라이언트 라이브러리.

하네스 원칙 문서(필독):  
[자기 이벤트와 Outbox 중앙 전달](../../docusaurus-archive/docs/patterns/architecture-patterns/own-events-outbox.md)  
(도쿠사우루스: `/docs/patterns/architecture-patterns/own-events-outbox`)

---

## 왜 이 모양인가 (요약)

```text
이벤트 지옥
  → 발행을 event-saga로 중앙화 (Outbox로 신뢰 전달)
  → 그런데 중앙이 이벤트·사가를 “관리”하면 또 난리
  → 그래서: 자기 이벤트만 발행
            보상도 이벤트로 발행
            보상 처리는 “필요한 소비 쪽”에서
```

**요즘 제일 중요한 것:** 중앙은 **전달 창구**이지 **이벤트 관리 시스템**이 아니다.

| | |
|--|--|
| event-saga | Accept → Outbox → Kafka **producer**만 |
| 이 라이브러리 | **gRPC 발행** + **Kafka 컨슘** |
| 각 서비스 | 자기 이벤트만. 보상 이벤트 수신 시 자기 보상 |

성공: `COMPLETE` / `SUCCEEDED`.  
실패·보상: `FAIL` + `COMPENSATE*` → 컨슈머 처리.

---

## 역할

| 이 모듈 (앱) | event-saga 서버 |
|--------------|-----------------|
| `SagaPublishPolicy` · 메시지 계약 | REST + gRPC Accept |
| **gRPC 발행 클라이언트** | `event_outbox` + Kafka producer 릴레이 |
| **Kafka 컨슘** · `SagaEventHandler` | 컨슘·사가 상태 테이블 없음 |

---

## 앱에서

### 발행 (gRPC → event-saga)

```yaml
origemite:
  message:
    saga:
      publish:
        enabled: true
        target: localhost:9095
      consume:
        enabled: false
```

```java
sagaPublishPort.publish(
  SagaMessage.builder(SagaMessageType.COMPLETE, "order")
      .status("SUCCEEDED").payload(...).build());
```

### 컨슘 (Kafka)

```yaml
spring.kafka.bootstrap-servers: localhost:9092
origemite.message.saga.publish.enabled: false
origemite.message.saga.consume.enabled: true
```

```java
@Component
class OrderCompletedHandler implements SagaEventHandler {
  public SagaMessageType type() { return SagaMessageType.COMPLETE; }
  public String domain() { return "order"; }
  public void onEvent(SagaMessage m) { /* 내 것이면 처리·보상 */ }
}
// sagaSubscribePort.subscribe("complete.order", "my-service");
```

---

## API

`SagaMessage` · `SagaPublishPolicy` · `SagaPublishPort`(gRPC) · `SagaSubscribePort` · `SagaEventHandler`
