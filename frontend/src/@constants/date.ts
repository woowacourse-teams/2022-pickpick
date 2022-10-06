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

export const DAY = {
  0: "일요일",
  1: "월요일",
  2: "화요일",
  3: "수요일",
  4: "목요일",
  5: "금요일",
  6: "토요일",
} as const;

export const WEEKDAYS = ["일", "월", "화", "수", "목", "금", "토"] as const;
export const MONTHS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] as const;