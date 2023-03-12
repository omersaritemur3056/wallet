import axios from 'axios';

const HttpRequestUtil = axios.create({
    baseURL: "http://localhost:9899",
    headers: {
        "Content-Type": "application/json",
    },
});

export default HttpRequestUtil