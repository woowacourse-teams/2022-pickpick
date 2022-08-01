import { API_ENDPOINT } from "@src/@constants";
import { ResponseBookmarks } from "@src/@types/shared";
import { fetcher } from ".";
import { getAuthorization } from "./utils";

interface GetBookmarkParam {
  pageParam?: string;
}

export const getBookmarks = async (
  { pageParam }: GetBookmarkParam = { pageParam: "" }
) => {
  const { data } = await fetcher.get<ResponseBookmarks>(
    `${API_ENDPOINT.BOOKMARKS}?messageId=${pageParam}`
  );
  return data;
};

export const postBookmark = async (messageId: string) => {
  await fetcher.post(API_ENDPOINT.BOOKMARKS, { messageId });
};

export const deleteBookmark = async (messageId: string) => {
  await fetcher.delete(`${API_ENDPOINT.BOOKMARKS}?messageId=${messageId}`);
};
