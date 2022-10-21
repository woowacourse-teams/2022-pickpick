import auth from "./auth";
import bookmarks from "./bookmarks";
import channels from "./channels";
import messages from "./messages";
import reminders from "./reminders";

const handlers = [
  ...bookmarks,
  ...channels,
  ...messages,
  ...auth,
  ...reminders,
];

export default handlers;
