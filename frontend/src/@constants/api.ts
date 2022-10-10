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
