import axios from 'axios';

const token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.JYAaZ6_q5z2I9lNbAdgpmTkUvXkrtAB7mqBb6oLBxAszigwXQld_dFJRU3aAfF2mQZtu1I5TpsvtpElctGN4Yg";

const HttpRequestUtil = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        "Authorization": "Bearer " + token,
        "Access-Control-Allow-Origin": "*",
    },
});

export default HttpRequestUtil