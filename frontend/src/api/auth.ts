import { API_ENDPOINT } from "@src/@constants";
import { fetcher } from ".";

export const isAuthenticated = async () => {
  const data = await fetcher.get("/api/auth");
  return data;
};

export const slackLogin = async (code: string) => {
  const data = await fetcher.get(`${API_ENDPOINT.SLACK_LOGIN}?code=${code}`);
  return data;
};
