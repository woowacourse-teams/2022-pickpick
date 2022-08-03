import bookmarks from "./bookmarks";
import channels from "./channels";
import messages from "./messages";
import auth from "./auth";

const handlers = [...bookmarks, ...channels, ...messages, ...auth];

export default handlers;
