export const QUERY_KEY = {
  ALL_CHANNELS: "allChannels",
  SUBSCRIBED_CHANNELS: "subscribedChannels",
  ALL_MESSAGES: "allMessages",
  SPECIFIC_DATE_MESSAGES: "specificDateMessages",
  BOOKMARKS: "bookmarks",
  REMINDERS: "reminders",
  REMINDER: "reminder",
  AUTHENTICATION: "authentication",
  SLACK_LOGIN: "slackLogin",
} as const;

export const API_ENDPOINT = {
  MESSAGES: "/api/messages",
  CHANNEL: "/api/channels",
  CHANNEL_SUBSCRIPTION: "/api/channel-subscription",
  BOOKMARKS: "/api/bookmarks",
  REMINDERS: "/api/reminders",
  CERTIFICATION: "/api/certification",
  SLACK_LOGIN: "/api/slack-login",
} as const;

export const ACCESS_TOKEN_KEY = "ACCESS_TOKEN";

export const SLACK_LOGIN_URL = `https://slack.com/oauth/v2/authorize?scope=users:read&user_scope=identity.basic&redirect_uri=${process.env.SLACK_REDIRECT_URL}&client_id=${process.env.SLACK_CLIENT_ID}`;

export const SEARCH_PARAMS = {
  SEARCH_KEYWORD: "keyword",
  SEARCH_CHANNEL_IDS: "channelIds",
} as const;

export const ERROR_CODE = {
  MEMBER_NOT_FOUND: "MEMBER_NOT_FOUND",
  INVALID_TOKEN: "INVALID_TOKEN",
  CHANNEL_NOT_FOUND: "CHANNEL_NOT_FOUND",
  SUBSCRIPTION_DUPLICATE: "SUBSCRIPTION_DUPLICATE",
  SUBSCRIPTION_INVALID_ORDER: "SUBSCRIPTION_INVALID_ORDER",
  SUBSCRIPTION_NOT_EXIST: "SUBSCRIPTION_NOT_EXIST",
  SUBSCRIPTION_ORDER_DUPLICATE: "SUBSCRIPTION_ORDER_DUPLICATE",
  BOOKMARK_DELETE_FAILURE: "BOOKMARK_DELETE_FAILURE",
  SUBSCRIPTION_NOT_FOUND: "SUBSCRIPTION_NOT_FOUND",
  BOOKMARK_NOT_FOUND: "BOOKMARK_NOT_FOUND",
  MESSAGE_NOT_FOUND: "MESSAGE_NOT_FOUND",
} as const;
