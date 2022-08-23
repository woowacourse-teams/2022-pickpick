import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseBookmarks } from "@src/@types/shared";
import { getBookmarks } from "@src/api/bookmarks";
import { useInfiniteQuery } from "react-query";
import { nextBookmarksCallback } from "@src/api/utils";

function useGetInfiniteBookmarks() {
  return useInfiniteQuery<ResponseBookmarks, CustomError>(
    QUERY_KEY.BOOKMARKS,
    getBookmarks,
    {
      getNextPageParam: nextBookmarksCallback,
    }
  );
}

export default useGetInfiniteBookmarks;
