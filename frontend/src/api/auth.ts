import { API_ENDPOINT } from "@src/@constants";
import { privateFetcher, publicFetcher } from ".";

export const isAuthenticated = async () => {
  const data = await privateFetcher.get("/api/auth");
  return data;
};

export const slackLogin = async (code: string) => {
  const data = await publicFetcher.get(
    `${API_ENDPOINT.SLACK_LOGIN}?code=${code}`
  );
  return data;
};
