import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { ResponseBookmarks } from "@src/@types/api";
import { getPrivateHeaders } from "@src/@utils/api";

type GetBookmarks = ({
  pageParam,
}: {
  pageParam?: string;
}) => Promise<ResponseBookmarks>;

export const getBookmarks: GetBookmarks = async (
  { pageParam } = { pageParam: "" }
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

type PostBookmark = (messageId: number) => Promise<void>;

export const postBookmark: PostBookmark = async (messageId) => {
  await fetcher.post(
    API_ENDPOINT.BOOKMARKS,
    { messageId },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};

type DeleteBookmark = (messageId: number) => Promise<void>;

export const deleteBookmark: DeleteBookmark = async (messageId) => {
  await fetcher.delete(API_ENDPOINT.BOOKMARKS, {
    headers: { ...getPrivateHeaders() },
    params: {
      messageId,
    },
  });
};
