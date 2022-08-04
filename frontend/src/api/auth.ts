import { API_ENDPOINT } from "@src/@constants";
import { privateFetcher, publicFetcher } from ".";

export const isCertificated = async () => {
  const data = await privateFetcher.get(API_ENDPOINT.CERTIFICATION);
  return data;
};

export const slackLogin = async (code: string) => {
  const data = await publicFetcher.get(
    `${API_ENDPOINT.SLACK_LOGIN}?code=${code}`
  );
  return data;
};
