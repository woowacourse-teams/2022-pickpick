import { useInfiniteQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseBookmarks } from "@src/@types/shared";

import { getBookmarks } from "@src/api/bookmarks";

function useGetInfiniteBookmarks() {
  return useInfiniteQuery<ResponseBookmarks, CustomError>(
    QUERY_KEY.BOOKMARKS,
    getBookmarks,
    {
      getNextPageParam: ({ hasPast, bookmarks }: ResponseBookmarks) => {
        if (hasPast) {
          return bookmarks[bookmarks.length - 1]?.id;
        }
      },
    }
  );
}

export default useGetInfiniteBookmarks;
