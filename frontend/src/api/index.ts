import { ACCESS_TOKEN_KEY } from "@src/@constants";
import { getCookie } from "@src/@utils";
import axios from "axios";

export const fetcher = axios.create({
  baseURL: process.env.API_URL,
  headers: {
    "Access-Control-Allow-Origin": "*",
    authorization: `Bearer ${getCookie(ACCESS_TOKEN_KEY)}`,
  },
});
