export type Message = {
  id: number;
  username: string;
  postedDate: string;
  remindDate: string;
  text: string;
  userThumbnail: string;
  isBookmarked: boolean;
  isSetReminded: boolean;
};

export type Bookmark = {
  id: number;
  messageId: number;
  username: string;
  postedDate: string;
  remindDate: string;
  text: string;
  userThumbnail: string;
};

export type Reminder = {
  id: number;
  messageId: number;
  username: string;
  userThumbnail: string;
  text: string;
  postedDate: string;
  remindDate: string;
  modifyDate: string;
};

export type Channel = {
  id: string;
  name: string;
  isSubscribed: boolean;
};

export type SubscribedChannel = {
  id: number;
  name: string;
  order: number;
};

export type ResponseReminders = {
  reminders: Reminder[];
  hasFuture: boolean;
};

export type ResponseBookmarks = {
  bookmarks: Bookmark[];
  hasPast: boolean;
};

export type ResponseMessages = {
  messages: Message[];
  hasPast: boolean;
  hasFuture: boolean;
};

export type ResponseChannels = {
  channels: Channel[];
};

export type ResponseSubscribedChannels = {
  channels: SubscribedChannel[];
};

export type ResponseToken = {
  token: string;
  isFirstLogin: boolean;
};
