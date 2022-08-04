export const PATH_NAME = {
  HOME: "/",
  FEED: "/feed",
  ALARM: "/alarm",
  BOOKMARK: "/bookmark",
  ADD_CHANNEL: "/add-channel",
  CERTIFICATION: "/certification",
} as const;

export const QUERY_KEY = {
  ALL_CHANNELS: "allChannels",
  SUBSCRIBED_CHANNELS: "subscribedChannels",
  ALL_MESSAGES: "allMessages",
  SPECIFIC_DATE_MESSAGES: "specificDateMessages",
  BOOKMARKS: "bookmarks",
  AUTHENTICATION: "authentication",
  SLACK_LOGIN: "slackLogin",
} as const;

export const API_ENDPOINT = {
  MESSAGES: "/api/messages",
  CHANNEL: "/api/channels",
  CHANNEL_SUBSCRIPTION: "/api/channel-subscription",
  BOOKMARKS: "/api/bookmarks",
  CERTIFICATION: "/api/certification",
  SLACK_LOGIN: "/api/slack-login",
} as const;

export const ACCESS_TOKEN_KEY = "ACCESS_TOKEN";

export const DATE = {
  TODAY: "오늘",
  YESTERDAY: "어제",
} as const;

export const TIME = {
  AM: "오전",
  PM: "오후",
  NOON: 12,
} as const;

export const CONVERTER_SUFFIX = "T23:59:59";

export const DAY: Record<number, string> = {
  0: "일요일",
  1: "월요일",
  2: "화요일",
  3: "수요일",
  4: "목요일",
  5: "금요일",
  6: "토요일",
};

export const ERROR_MESSAGES = {
  UNAUTHORIZED: "로그인이 필요한 서비스입니다.",
} as const;

export const MESSAGES = {
  LOGIN_SUCCESS: "로그인 되었습니다!",
} as const;

export const SNACKBAR_STATUS = {
  SUCCESS: "SUCCESS",
  FAIL: "FAIL",
} as const;
