import { API_ENDPOINT } from "@src/@constants";
import { fetcher } from ".";
import { getPrivateHeaders } from "./utils";

interface PostReminderProps {
  messageId: number;
  reminderDate: string;
}

export const postReminder = async (postData: PostReminderProps) => {
  await fetcher.post(API_ENDPOINT.REMINDERS, postData, {
    headers: { ...getPrivateHeaders() },
  });
};
