import { SNACKBAR_STATUS, ERROR_MESSAGE_BY_CODE } from "@src/@constants";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";

export type Theme = typeof LIGHT_MODE_THEME;

export interface StyledDefaultProps {
  theme: Theme;
}
export interface Message {
  id: string;
  username: string;
  postedDate: string;
  text: string;
  userThumbnail: string;
  isBookmarked: boolean;
  isSetReminded: boolean;
}

export type Bookmark = Omit<Message, "isBookmarked">;

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
  code: keyof typeof ERROR_MESSAGE_BY_CODE;
  message: string;
}
