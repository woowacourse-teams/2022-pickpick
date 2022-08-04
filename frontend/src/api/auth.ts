import { API_ENDPOINT } from "@src/@constants";
import { fetcher } from ".";
import { ResponseToken } from "@src/@types/shared";
import { getPrivateHeaders, getPublicHeaders } from "./utils";

export const isCertificated = async () => {
  const { data } = await fetcher.get(API_ENDPOINT.CERTIFICATION, {
    headers: { ...getPrivateHeaders() },
  });
  return data;
};

export const slackLogin = async (code: string) => {
  const { data } = await fetcher.get<ResponseToken>(
    `${API_ENDPOINT.SLACK_LOGIN}?code=${code}`,
    {
      headers: { ...getPublicHeaders() },
    }
  );
  return data;
};
