import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },   // 준비: VU 10명
        { duration: '30s',  target: 50 },   // 50 VU
        { duration: '30s',  target: 100 },  // 100 VU
        { duration: '30s',  target: 150 },  // 100 VU
        { duration: '30s',  target: 200 },  // 200 VU
        { duration: '30s', target: 0 },    // 정리
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: [
            'avg<150',      // 평균 응답시간 150ms 미만
            'p(90)<180',    // 90%는 180ms 안에
            'p(95)<200',    // 95%는 200ms 안에
            'p(99)<400',    // 99%는 400ms 안에
            'max<2000',     // 최악도 2초는 넘지 말자
        ],
    },
};

const BASE_URL = 'https://gateway-dev.origemite.com';
const PATH = '/auth/test1/traffic';

export default function () {
    const res = http.get(`${BASE_URL}${PATH}`);
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(0.1); // 100ms
}

// K6_PROMETHEUS_RW_SERVER_URL=http://localhost:19090/api/v1/write \
// k6 run --out experimental-prometheus-rw public_traffic_test.js
