import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { GetReminderPageParam, ResponseReminders } from "@src/@types/api";
import { getPrivateHeaders } from "@src/@utils/api";

type GetReminders = ({
  pageParam,
}: GetReminderPageParam) => Promise<ResponseReminders>;

export const getReminders: GetReminders = async ({ pageParam }) => {
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

type PoseReminder = (postData: {
  messageId: number;
  reminderDate: string;
}) => Promise<void>;

export const postReminder: PoseReminder = async (postData) => {
  await fetcher.post(API_ENDPOINT.REMINDERS, postData, {
    headers: { ...getPrivateHeaders() },
  });
};

type DeleteReminder = (messageId: number) => Promise<void>;

export const deleteReminder: DeleteReminder = async (messageId) => {
  await fetcher.delete(API_ENDPOINT.REMINDERS, {
    headers: { ...getPrivateHeaders() },
    params: {
      messageId,
    },
  });
};

type PutReminder = (modifyDate: {
  messageId: number;
  reminderDate: string;
}) => Promise<void>;

export const putReminder: PutReminder = async (modifyData) => {
  await fetcher.put(
    API_ENDPOINT.REMINDERS,
    { ...modifyData },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};
