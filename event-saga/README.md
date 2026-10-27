# event-saga

**전통 오케스트레이션 Saga** + **Outbox 중앙 전달**.

```text
시작/결과 이벤트 소비  ← Saga 서버가 컨슈머
        ↓ 진행·보상 결정
다음 요청 / 보상 지시 발행 → event_outbox → Kafka
```

원칙: [오케스트레이션 Saga와 Outbox](../../docusaurus-archive/docs/patterns/architecture-patterns/own-events-outbox.md) · [`lib-message-saga`](../lib-message-saga/README.md)

**Saga 서버 = Kafka 컨슈머.** 참여자는 결과만 Outbox로 넣고, 요청·보상은 참여자가 컨슘한다.

```text
주문/참여자 ──gRPC Accept──▶ event-saga ──outbox──▶ Kafka
                                              │
                         Saga 서버가 결과 토픽 소비 ─┘
참여자 ◀── Kafka (요청·보상) ── lib subscribe
```

## 제공

| | |
|--|--|
| REST | `POST /sagas` (발행 입구) |
| gRPC | `SagaIngress.Accept` |
| 테이블 | `event_outbox` |
| Kafka | **consumer = Saga 서버** + **producer**(Outbox 릴레이) |

## 오케스트레이션 (개념)

1. 주문 시작 이벤트 발행  
2. Saga 소비 → 다음 업무 요청 발행  
3. 각 서비스 실행 후 결과 발행  
4. Saga 소비 → 진행 또는 보상 결정 → 발행  

코레오그래피가 아니라 **Saga 지휘**. 버스에 전 이력 쌓지 않고, 필요한 결과·지시만 오간다.

## 설정

```yaml
spring.kafka.bootstrap-servers: localhost:9092
origemite.message.saga:
  publish.enabled: false
  consume.enabled: true          # Saga 서버 = 컨슈머
  orchestration:
    group: event-saga
    topics: [create.order, complete.payment]  # 구독 토픽
  outbox:
    batch-size: 50
    relay-delay-ms: 2000
```

진행·보상 로직은 `SagaEventHandler` 빈을 event-saga에 두면 Dispatcher가 호출한다.