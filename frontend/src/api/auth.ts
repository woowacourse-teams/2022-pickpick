import { fetcher } from ".";

export const isAuthenticated = async () => {
  const data = await fetcher.get("/api/auth");
  return data;
};
