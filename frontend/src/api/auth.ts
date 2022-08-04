import { API_ENDPOINT } from "@src/@constants";
import { fetcher } from ".";
import { getHeaders } from "./utils";

export const isAuthenticated = async () => {
  const data = await fetcher.get("/api/auth", {
    headers: getHeaders(),
  });
  return data;
};

export const slackLogin = async (code: string) => {
  const data = await fetcher.get(`${API_ENDPOINT.SLACK_LOGIN}?code=${code}`, {
    headers: { "Access-Control-Allow-Origin": "*" },
  });
  return data;
};
