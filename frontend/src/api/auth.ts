import { fetcher } from ".";
import { getHeaders } from "./utils";

export const isAuthenticated = async () => {
  const data = await fetcher.get("/api/auth", {
    headers: getHeaders(),
  });
  return data;
};
