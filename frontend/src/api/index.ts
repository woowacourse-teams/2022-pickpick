import axios from "axios";

export const fetcher = axios.create({
  baseURL: "/api",
});
