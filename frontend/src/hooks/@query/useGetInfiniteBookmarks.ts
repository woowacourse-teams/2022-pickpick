import { UseInfiniteQueryResult, useInfiniteQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants/api";
import { ResponseBookmarks } from "@src/@types/api";
import { CustomError } from "@src/@types/shared";

import { getBookmarks } from "@src/api/bookmarks";

function useGetInfiniteBookmarks(): UseInfiniteQueryResult<
  ResponseBookmarks,
  CustomError
> {
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
