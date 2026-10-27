# event-saga

**자기 이벤트 Outbox 발행 서버** (사가 상태·이벤트 이력 관리 없음).

원칙: 이벤트 지옥 → 발행 중앙화 → **중앙이 이벤트를 다시 관리하면 또 지옥** →  
그래서 여기는 **전달 창구(Outbox)** 만. 자기 이벤트·보상 이벤트 적재 후 Kafka로 보냄.  
보상 처리는 컨슈머. 상세: [own-events-outbox](../../docusaurus-archive/docs/patterns/architecture-patterns/own-events-outbox.md) · [`lib-message-saga/README`](../lib-message-saga/README.md)

앱은 `lib-message-saga`로 gRPC 발행 + Kafka 컨슘.

```text
MSA 앱 ──gRPC Accept──▶ event-saga ──event_outbox──▶ Kafka producer
MSA 앱 ◀── Kafka consume ── lib-message-saga
              REST POST /sagas 도 동일 Facade
```

## 제공

| | |
|--|--|
| REST | `POST /sagas` |
| gRPC | `SagaIngress.Accept` (proto는 lib) |
| 테이블 | `event_outbox`만 |
| Kafka | **producer만** (릴레이) |

## 설정

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      acks: all
origemite:
  message:
    saga:
      publish.enabled: false   # 서버는 lib 클라이언트 안 씀
      consume.enabled: false
      outbox:
        batch-size: 50
        relay-delay-ms: 2000
spring.grpc.server.port: 9095
server.port: 8095
```

계약·정책: [`../lib-message-saga/README.md`](../lib-message-saga/README.md)
