import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { ResponseToken } from "@src/@types/api";
import { getPrivateHeaders, getPublicHeaders } from "@src/@utils/api";

type IsCertificated = () => Promise<unknown>;

export const isCertificated: IsCertificated = async () => {
  const { data } = await fetcher.get<unknown>(API_ENDPOINT.CERTIFICATION, {
    headers: { ...getPrivateHeaders() },
  });
  return data;
};

type SlackLogin = (code: string) => Promise<ResponseToken>;

export const slackLogin: SlackLogin = async (code) => {
  const { data } = await fetcher.get<ResponseToken>(API_ENDPOINT.SLACK_LOGIN, {
    headers: { ...getPublicHeaders() },
    params: {
      code,
    },
  });
  return data;
};

type RegisterSlackWorkspace = (code: string) => Promise<ResponseToken>;
export const registerSlackWorkspace: RegisterSlackWorkspace = async (code) => {
  const { data } = await fetcher.get<ResponseToken>(
    API_ENDPOINT.SLACK_REGISTER_WORKSPACE,
    {
      headers: { ...getPublicHeaders() },
      params: {
        code,
      },
    }
  );
  return data;
};
