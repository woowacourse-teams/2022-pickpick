import { useInfiniteQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseReminders } from "@src/@types/shared";

import { getReminders } from "@src/api/reminders";

function useGetInfiniteReminders() {
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
