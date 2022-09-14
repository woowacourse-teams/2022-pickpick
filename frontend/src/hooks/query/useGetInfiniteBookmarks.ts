import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseBookmarks } from "@src/@types/shared";
import { getBookmarks } from "@src/api/bookmarks";
import { useInfiniteQuery } from "react-query";

function useGetInfiniteBookmarks() {
  return useInfiniteQuery<ResponseBookmarks, CustomError>(
    QUERY_KEY.BOOKMARKS,
    getBookmarks,
    {
      getNextPageParam: ({ isLast, bookmarks }: ResponseBookmarks) => {
        if (!isLast) {
          return bookmarks[bookmarks.length - 1]?.id;
        }
      },
    }
  );
}

export default useGetInfiniteBookmarks;
