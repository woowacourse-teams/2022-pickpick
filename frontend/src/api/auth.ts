import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { ResponseToken } from "@src/@types/api";
import { getPrivateHeaders, getPublicHeaders } from "@src/@utils/api";

export const isCertificated = async () => {
  const { data } = await fetcher.get(API_ENDPOINT.CERTIFICATION, {
    headers: { ...getPrivateHeaders() },
  });
  return data;
};

export const slackLogin = async (code: string) => {
  const { data } = await fetcher.get<ResponseToken>(API_ENDPOINT.SLACK_LOGIN, {
    headers: { ...getPublicHeaders() },
    params: {
      code,
    },
  });
  return data;
};
