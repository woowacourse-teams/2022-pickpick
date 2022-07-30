export const PATH_NAME = {
  HOME: "/",
  FEED: "/feed",
  ALARM: "/alarm",
  BOOKMARK: "/bookmark",
  ADD_CHANNEL: "/add-channel",
} as const;

export const QUERY_KEY = {
  ALL_CHANNELS: "allChannels",
  SUBSCRIBED_CHANNELS: "subscribedChannels",
  ALL_MESSAGES: "allMessages",
  SPECIFIC_DATE_MESSAGES: "specificDateMessages",
  BOOKMARKS: "bookmarks",
} as const;

export const API_ENDPOINT = {
  MESSAGES: "/api/messages",
  CHANNEL: "/api/channels",
  CHANNEL_SUBSCRIPTION: "/api/channel-subscription",
  BOOKMARKS: "/api/bookmarks",
} as const;
