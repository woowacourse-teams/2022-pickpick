import { API_ENDPOINT } from "@src/@constants";
import { ResponseReminder, ResponseReminders } from "@src/@types/shared";
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
    `${API_ENDPOINT.REMINDERS}?reminderId=${pageParam ?? ""}`,
    {
      headers: { ...getPrivateHeaders() },
    }
  );

  return data;
};

export const getReminder = async (messageId: string) => {
  const { data } = await fetcher.get<ResponseReminder>(
    `${API_ENDPOINT.REMINDERS}?messageId=${messageId}`,
    {
      headers: { ...getPrivateHeaders() },
    }
  );

  return data;
};

export const postReminder = async (postData: ReminderProps) => {
  await fetcher.post(API_ENDPOINT.REMINDERS, postData, {
    headers: { ...getPrivateHeaders() },
  });
};

export const deleteReminder = async (messageId: string) => {
  await fetcher.delete(`${API_ENDPOINT.REMINDERS}?messageId=${messageId}`, {
    headers: { ...getPrivateHeaders() },
  });
};

export const putReminder = async (modifyData: ReminderProps) => {
  await fetcher.put(
    `${API_ENDPOINT.REMINDERS}`,
    { ...modifyData },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};
