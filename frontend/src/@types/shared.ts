import { LIGHT_MODE_THEME } from "@src/@styles/theme";

export type Theme = typeof LIGHT_MODE_THEME;

export interface Message {
  id: string;
  username: string;
  postedDate: string;
  text: string;
  userThumbnail: string;
}

export interface ResponseMessages {
  messages: Message[];
  isLast: boolean;
  nextPage: number;
}

export interface Channel {
  id: string;
  name: string;
  isSubscribed: boolean;
}

export interface ResponseChannels {
  channels: Channel[];
}
