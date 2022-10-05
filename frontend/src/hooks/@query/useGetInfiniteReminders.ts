import { UseInfiniteQueryResult, useInfiniteQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants/api";
import { ResponseReminders } from "@src/@types/api";
import { CustomError } from "@src/@types/shared";

import { getReminders } from "@src/api/reminders";

function useGetInfiniteReminders(): UseInfiniteQueryResult<
  ResponseReminders,
  CustomError
> {
  return useInfiniteQuery<ResponseReminders, CustomError>(
    QUERY_KEY.REMINDERS,
    getReminders,
    {
      getNextPageParam: ({ hasFuture, reminders }: ResponseReminders) => {
        if (hasFuture) {
          return reminders[reminders.length - 1]?.id;
        }
      },
    }
  );
}

export default useGetInfiniteReminders;
