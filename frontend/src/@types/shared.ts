import {
  SNACKBAR_STATUS,
  ERROR_MESSAGE_BY_CODE,
  THEME_KIND,
} from "@src/@constants";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";

export type Theme = typeof LIGHT_MODE_THEME;

export type ThemeKind = keyof typeof THEME_KIND;

export interface StyledDefaultProps {
  theme: Theme;
}
export interface Message {
  id: number;
  username: string;
  postedDate: string;
  remindDate: string;
  text: string;
  userThumbnail: string;
  isBookmarked: boolean;
  isSetReminded: boolean;
}

export interface Bookmark {
  id: number;
  messageId: number;
  username: string;
  postedDate: string;
  remindDate: string;
  text: string;
  userThumbnail: string;
}

export interface Reminder {
  id: number;
  messageId: number;
  username: string;
  userThumbnail: string;
  text: string;
  postedDate: string;
  remindDate: string;
  modifyDate: string;
}

export interface ResponseReminders {
  reminders: Reminder[];
  isLast: boolean;
}

export interface ResponseBookmarks {
  bookmarks: Bookmark[];
  isLast: boolean;
}

export interface ResponseMessages {
  messages: Message[];
  isLast: boolean;
}

export interface Channel {
  id: string;
  name: string;
  isSubscribed: boolean;
}

export interface ResponseChannels {
  channels: Channel[];
}

export interface SubscribedChannel {
  id: number;
  name: string;
  order: number;
}

export interface ResponseSubscribedChannels {
  channels: SubscribedChannel[];
}

export type SnackbarStatus = keyof typeof SNACKBAR_STATUS;

export interface ResponseToken {
  token: string;
  isFirstLogin: boolean;
}

export interface CustomError {
  response: {
    data: Error;
  };
}

export interface Error {
  code: keyof typeof ERROR_MESSAGE_BY_CODE;
  message: string;
}
