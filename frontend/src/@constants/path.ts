export const PATH_NAME = {
  HOME: "/",
  FEED: "/feed",
  BOOKMARK: "/bookmark",
  REMINDER: "/reminder",
  ADD_CHANNEL: "/add-channel",
  CERTIFICATION: "/certification",
  REGISTER_SLACK_WORKSPACE: "/register-slackWorkspace",
  SEARCH_RESULT: "/search-result",
} as const;

export const SLACK_URL = {
  LOGIN: process.env.SLACK_LOGIN_URL as string,
  REGISTER_WORKSPACE: process.env.SLACK_REGISTER_WORKSPACE_URL as string,
} as const;
