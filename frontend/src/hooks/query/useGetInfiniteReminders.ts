import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseReminders } from "@src/@types/shared";
import { getReminders } from "@src/api/reminders";
import { useInfiniteQuery } from "react-query";

function useGetInfiniteReminders() {
  return useInfiniteQuery<ResponseReminders, CustomError>(
    QUERY_KEY.REMINDERS,
    getReminders,
    {
      getNextPageParam: ({ isLast, reminders }: ResponseReminders) => {
        if (!isLast) {
          return reminders[reminders.length - 1]?.id;
        }
      },
    }
  );
}

export default useGetInfiniteReminders;
