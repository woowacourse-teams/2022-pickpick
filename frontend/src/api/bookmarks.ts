import { API_ENDPOINT } from "@src/@constants";
import { ResponseBookmarks } from "@src/@types/shared";
import { fetcher } from ".";
import { getHeaders } from "./utils";
interface GetBookmarkParam {
  pageParam?: string;
}

export const getBookmarks = async (
  { pageParam }: GetBookmarkParam = { pageParam: "" }
) => {
  const { data } = await fetcher.get<ResponseBookmarks>(
    `${API_ENDPOINT.BOOKMARKS}?bookmarkId=${pageParam}`
  );
  return data;
};

export const postBookmark = async (messageId: string) => {
  await fetcher.post(
    API_ENDPOINT.BOOKMARKS,
    { messageId },
    {
      headers: getHeaders(),
    }
  );
};

export const deleteBookmark = async (bookmarkId: string) => {
  await fetcher.delete(`${API_ENDPOINT.BOOKMARKS}/${bookmarkId}`, {
    headers: getHeaders(),
  });
};
