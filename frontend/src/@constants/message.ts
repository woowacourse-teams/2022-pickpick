import { ERROR_CODE } from "@src/@constants/api";

export const MESSAGE = {
  DEFAULT_SERVER_ERROR: "서버에 오류가 있습니다. 잠시 후에 다시 시도해주세요.",
  LOGIN_SUCCESS: "로그인 되었습니다!",
  WORKSPACE_SUCCESS:
    "워크스페이스가 등록되었습니다. 로그인 후 서비스를 이용해주세요.",
  INVALID_SEARCH_CHANNELS: "채널을 하나 이상 선택 후 검색 버튼을 눌러주세요.",
  INVALID_SEARCH_KEYWORD: "검색할 키워드를 입력하신 후 검색 버튼을 눌러주세요.",
  INVALID_REMINDER_TIME:
    "리마인더 시간은 현재 시간보다 미래로 설정해주셔야 합니다.",
  CONFIRM_REMINDER_REMOVE: "해당하는 메시지 리마인더를 정말 삭제하시겠습니까?",
  WORKSPACE_DUPLICATE: "이미 등록된 워크스페이스입니다.",
} as const;

export const API_ERROR_MESSAGE = {
  [ERROR_CODE.MEMBER_NOT_FOUND]: "로그인이 필요한 서비스 입니다.",
  [ERROR_CODE.INVALID_TOKEN]: "로그인이 필요한 서비스 입니다.",
  [ERROR_CODE.CHANNEL_NOT_FOUND]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.SUBSCRIPTION_DUPLICATE]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.SUBSCRIPTION_INVALID_ORDER]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.SUBSCRIPTION_NOT_EXIST]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.SUBSCRIPTION_ORDER_DUPLICATE]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.BOOKMARK_DELETE_FAILURE]: MESSAGE.DEFAULT_SERVER_ERROR,
  [ERROR_CODE.WORKSPACE_DUPLICATE]: MESSAGE.WORKSPACE_DUPLICATE,
  [ERROR_CODE.SUBSCRIPTION_NOT_FOUND]:
    "현재 구독 중인 채널이 없습니다! 먼저 채널을 구독하세요!",
  [ERROR_CODE.BOOKMARK_NOT_FOUND]:
    "죄송합니다. 현재 메시지를 가져올 수 없습니다.",
  [ERROR_CODE.MESSAGE_NOT_FOUND]:
    "죄송합니다. 현재 메시지를 가져올 수 없습니다.",
} as const;
