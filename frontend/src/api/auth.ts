import { fetcher } from ".";

export const isAuthenticated = async () => {
  await fetcher.post("/api/auth");
};
