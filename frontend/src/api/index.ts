import axios from "axios";

export const publicFetcher = axios.create({
  baseURL: process.env.API_URL,
  headers: {
    "Access-Control-Allow-Origin": "*",
  },
});

export const privateFetcher = axios.create({
  baseURL: process.env.API_URL,
  headers: {
    "Access-Control-Allow-Origin": "*",
    authorization: `Bearer 172`,
  },
});
