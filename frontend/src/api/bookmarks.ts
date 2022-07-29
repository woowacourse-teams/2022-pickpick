import { API_ENDPOINT } from "@src/@constants";
import { ResponseBookmarks } from "@src/@types/shared";
import { fetcher } from ".";

export const getBookmarks = async ({ pageParam = "" }: any) => {
  const { data } = await fetcher.get<ResponseBookmarks>(
    `${API_ENDPOINT.BOOKMARKS}?messageId=${pageParam}`,
    {
      headers: {
        authorization: "Bearer 1",
      },
    }
  );
  return data;
};
