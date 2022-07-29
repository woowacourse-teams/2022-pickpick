import bookmarks from "./bookmarks";
import channels from "./channels";
import messages from "./messages";

const handlers = [...bookmarks, ...channels, ...messages];

export default handlers;
