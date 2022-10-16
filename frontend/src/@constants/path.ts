export const PATH_NAME = {
  HOME: "/",
  FEED: "/feed",
  BOOKMARK: "/bookmark",
  REMINDER: "/reminder",
  ADD_CHANNEL: "/add-channel",
  CERTIFICATION: "/certification",
  REGISTER_SLACK_WORKSPACE: "/registerSlackWorkspace",
  SEARCH_RESULT: "/search-result",
} as const;

export const SLACK_URL = {
  LOGIN: `https://slack.com/oauth/v2/authorize?scope=users:read&user_scope=identity.basic&redirect_uri=${process.env.SLACK_REDIRECT_URL}&client_id=${process.env.SLACK_CLIENT_ID}`,
  REGISTER_WORKSPACE: `https://slack.com/oauth/v2/authorize?client_id=${process.env.SLACK_WORKSPACE_CLIENT_ID}&scope=channels:history,channels:read,chat:write,incoming-webhook,users.profile:read,users:read,channels:manage&user_scope=channels:read,channels:write`,
} as const;
