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

export const ERROR_MESSAGE_BY_CODE = {
  MEMBER_NOT_FOUND: "로그인이 필요한 서비스 입니다.",
  INVALID_TOKEN: "로그인이 필요한 서비스 입니다.",
  CHANNEL_NOT_FOUND: "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  SUBSCRIPTION_DUPLICATE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  SUBSCRIPTION_INVALID_ORDER:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  SUBSCRIPTION_NOT_EXIST:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  SUBSCRIPTION_ORDER_DUPLICATE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  BOOKMARK_DELETE_FAILURE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
  SUBSCRIPTION_NOT_FOUND:
    "현재 구독 중인 채널이 없습니다! 먼저 채널을 구독하세요!",
  BOOKMARK_NOT_FOUND: "죄송합니다. 현재 메시지를 가져올 수 없습니다. 🥲",
  MESSAGE_NOT_FOUND: "죄송합니다. 현재 메시지를 가져올 수 없습니다. 🥲",
  DEFAULT_MESSAGE: "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요. 🥲",
} as const;
