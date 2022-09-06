import { ACCESS_TOKEN_KEY } from "@src/@constants";
import { getCookie } from "@src/@utils";

export const getPublicHeaders = () => {
  return { "Access-Control-Allow-Origin": "*" };
};

export const getPrivateHeaders = () => {
  return {
    "Access-Control-Allow-Origin": "*",
    authorization: `Bearer ${getCookie(ACCESS_TOKEN_KEY)}`,
  };
};
