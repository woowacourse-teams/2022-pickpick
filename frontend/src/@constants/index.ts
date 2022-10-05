export const PATH_NAME = {
  HOME: "/",
  FEED: "/feed",
  BOOKMARK: "/bookmark",
  REMINDER: "/reminder",
  ADD_CHANNEL: "/add-channel",
  CERTIFICATION: "/certification",
  SEARCH_RESULT: "/search-result",
} as const;

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

export const ERROR_MESSAGE_BY_CODE = {
  MEMBER_NOT_FOUND: "로그인이 필요한 서비스 입니다.",
  INVALID_TOKEN: "로그인이 필요한 서비스 입니다.",
  CHANNEL_NOT_FOUND: "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  SUBSCRIPTION_DUPLICATE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  SUBSCRIPTION_INVALID_ORDER:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  SUBSCRIPTION_NOT_EXIST:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  SUBSCRIPTION_ORDER_DUPLICATE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  BOOKMARK_DELETE_FAILURE:
    "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  SUBSCRIPTION_NOT_FOUND:
    "현재 구독 중인 채널이 없습니다! 먼저 채널을 구독하세요!",
  BOOKMARK_NOT_FOUND: "죄송합니다. 현재 메시지를 가져올 수 없습니다.",
  MESSAGE_NOT_FOUND: "죄송합니다. 현재 메시지를 가져올 수 없습니다.",
  DEFAULT_MESSAGE: "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
} as const;

export const THEME_KIND = {
  LIGHT: "LIGHT",
  DARK: "DARK",
} as const;

export const PICKER_OPTION_SCROLL = {
  HEIGHT: 22,
  BEHAVIOR: "smooth",
} as const;
