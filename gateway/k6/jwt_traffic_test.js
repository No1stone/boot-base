import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },   // 준비: VU 10명
        { duration: '1m',  target: 50 },   // 50 VU
        { duration: '1m',  target: 100 },  // 100 VU
        { duration: '1m',  target: 200 },  // 200 VU
        { duration: '30s', target: 0 },    // 정리
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<200', 'p(99)<400'],
    },
};

const BASE_URL = 'https://gateway-dev.origemite.com';
const PATH = '/auth/test2/traffic';

// 터미널에서 JWT_TOKEN 환경변수로 넘길 예정
const TOKEN = __ENV.JWT_TOKEN;

export default function () {
    const headers = {
        Authorization: `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    };

    const res = http.get(`${BASE_URL}${PATH}`, { headers });

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.1); // 100ms
}
// JWT_TOKEN="" k6 run --out experimental-prometheus-rw jwt_traffic_test.js
