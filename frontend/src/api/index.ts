import axios from "axios";
import { ACCESS_TOKEN_KEY } from "@src/@constants";
import { getCookie } from "@src/@utils";

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
    authorization: `Bearer ${getCookie(ACCESS_TOKEN_KEY)}`,
  },
});
