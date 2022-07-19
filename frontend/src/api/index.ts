import axios from "axios";

export const fetcher = axios.create({
  baseURL: process.env.API_URL,
});
