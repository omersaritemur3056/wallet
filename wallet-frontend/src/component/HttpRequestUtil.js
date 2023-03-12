import axios from 'axios';

const instance = axios.create({
    baseURL: '"http://localhost:9899"',
    headers: {'X-Custom-Header': 'foobar'}
});