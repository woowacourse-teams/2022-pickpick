import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseReminders } from "@src/@types/shared";
import { getReminders } from "@src/api/reminders";
import { useInfiniteQuery } from "react-query";
import { nextRemindersCallback } from "@src/api/utils";

function useGetInfiniteReminders() {
  return useInfiniteQuery<ResponseReminders, CustomError>(
    QUERY_KEY.REMINDERS,
    getReminders,
    {
      getNextPageParam: nextRemindersCallback,
    }
  );
}

export default useGetInfiniteReminders;
