import { API_ENDPOINT } from "@src/@constants";
import { ResponseBookmarks } from "@src/@types/shared";
import { fetcher } from ".";
import { getAuthorization } from "./utils";

export const getBookmarks = async ({ pageParam = "" }: any) => {
  const { data } = await fetcher.get<ResponseBookmarks>(
    `${API_ENDPOINT.BOOKMARKS}?messageId=${pageParam}`,
    {
      headers: {
        ...getAuthorization(),
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
        ...getAuthorization(),
      },
    }
  );
};

export const deleteBookmark = async (messageId: string) => {
  await fetcher.delete(`${API_ENDPOINT.BOOKMARKS}?messageId=${messageId}`, {
    headers: {
      ...getAuthorization(),
    },
  });
};
