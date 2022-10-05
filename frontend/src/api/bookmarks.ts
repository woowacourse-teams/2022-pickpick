import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { ResponseBookmarks } from "@src/@types/api";
import { getPrivateHeaders } from "@src/@utils/api";

interface GetBookmarkParam {
  pageParam?: string;
}

export const getBookmarks = async (
  { pageParam }: GetBookmarkParam = { pageParam: "" }
) => {
  const { data } = await fetcher.get<ResponseBookmarks>(
    API_ENDPOINT.BOOKMARKS,
    {
      headers: { ...getPrivateHeaders() },
      params: {
        bookmarkId: pageParam ?? "",
      },
    }
  );
  return data;
};

export const postBookmark = async (messageId: number) => {
  await fetcher.post(
    API_ENDPOINT.BOOKMARKS,
    { messageId },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};

export const deleteBookmark = async (messageId: number) => {
  await fetcher.delete(API_ENDPOINT.BOOKMARKS, {
    headers: { ...getPrivateHeaders() },
    params: {
      messageId,
    },
  });
};
