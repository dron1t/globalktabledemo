import http from 'k6/http';

export const options = {
    stages: [
        { duration: '20s', target: 300},
        { duration: '1m30s', target: 300},
        { duration: '20s', target: 0}
    ],
    thresholds: {
        http_req_duration: ['p(95)<60']
    }
}

export default function () {
    http.get('http://localhost:8080/redis/key1')
}