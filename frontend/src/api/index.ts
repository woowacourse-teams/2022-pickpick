import axios from "axios";
import { getAuthorization } from "./utils";

export const fetcher = axios.create({
  baseURL: process.env.API_URL,
  headers: { "Access-Control-Allow-Origin": "*", ...getAuthorization() },
});
