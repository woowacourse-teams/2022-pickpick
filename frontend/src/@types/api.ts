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

export interface Channel {
  id: string;
  name: string;
  isSubscribed: boolean;
}

export interface SubscribedChannel {
  id: number;
  name: string;
  order: number;
}

export interface ResponseReminders {
  reminders: Reminder[];
  hasFuture: boolean;
}

export interface ResponseBookmarks {
  bookmarks: Bookmark[];
  hasPast: boolean;
}

export interface ResponseMessages {
  messages: Message[];
  hasPast: boolean;
  hasFuture: boolean;
}

export interface ResponseChannels {
  channels: Channel[];
}

export interface ResponseSubscribedChannels {
  channels: SubscribedChannel[];
}

export interface ResponseToken {
  token: string;
  isFirstLogin: boolean;
}
