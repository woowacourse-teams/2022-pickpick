import { API_ENDPOINT } from "@src/@constants";
import { ResponseReminders } from "@src/@types/shared";
import { fetcher } from ".";
import { getPrivateHeaders } from "./utils";

interface ReminderProps {
  messageId: number;
  reminderDate: string;
}

interface GetReminderParam {
  pageParam?: string;
}

export const getReminders = async ({ pageParam }: GetReminderParam) => {
  const { data } = await fetcher.get<ResponseReminders>(
    API_ENDPOINT.REMINDERS,
    {
      headers: { ...getPrivateHeaders() },
      params: {
        reminderId: pageParam ?? "",
      },
    }
  );

  return data;
};

export const postReminder = async (postData: ReminderProps) => {
  await fetcher.post(API_ENDPOINT.REMINDERS, postData, {
    headers: { ...getPrivateHeaders() },
  });
};

export const deleteReminder = async (messageId: number) => {
  await fetcher.delete(API_ENDPOINT.REMINDERS, {
    headers: { ...getPrivateHeaders() },
    params: {
      messageId,
    },
  });
};

export const putReminder = async (modifyData: ReminderProps) => {
  await fetcher.put(
    API_ENDPOINT.REMINDERS,
    { ...modifyData },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};
