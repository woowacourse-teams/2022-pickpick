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

export const postBookmark = async (messageId: string) => {
  await fetcher.post(
    API_ENDPOINT.BOOKMARKS,
    { messageId },
    {
      headers: {
        authorization: "Bearer 2004",
      },
    }
  );
};

export const deleteBookmark = async (messageId: string) => {
  await fetcher.delete(`${API_ENDPOINT.BOOKMARKS}?messageId=${messageId}`, {
    headers: {
      authorization: "Bearer 2004",
    },
  });
};
