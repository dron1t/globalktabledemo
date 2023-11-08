import http from 'k6/http';
import {sleep} from 'k6'

export const options = {
    stages: [
        { duration: '20s', target: 25},
        { duration: '1m30s', target: 25},
        { duration: '20s', target: 0}
    ],
    thresholds: {
        http_req_duration: ['p(95)<60']
    }
}

export default function () {
    http.get('http://localhost:8080/gktable/key1')
    sleep(0.5)
}