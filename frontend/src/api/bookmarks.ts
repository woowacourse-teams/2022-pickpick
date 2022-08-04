import { API_ENDPOINT } from "@src/@constants";
import { ResponseBookmarks } from "@src/@types/shared";
import { privateFetcher } from ".";
interface GetBookmarkParam {
  pageParam?: string;
}

export const getBookmarks = async (
  { pageParam }: GetBookmarkParam = { pageParam: "" }
) => {
  const { data } = await privateFetcher.get<ResponseBookmarks>(
    `${API_ENDPOINT.BOOKMARKS}?bookmarkId=${pageParam ?? ""}`
  );
  return data;
};

export const postBookmark = async (messageId: string) => {
  await privateFetcher.post(API_ENDPOINT.BOOKMARKS, { messageId });
};

export const deleteBookmark = async (bookmarkId: string) => {
  await privateFetcher.delete(`${API_ENDPOINT.BOOKMARKS}/${bookmarkId}`);
};
