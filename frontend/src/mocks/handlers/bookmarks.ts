import { rest } from "msw";
import { bookmarks } from "../data/bookmarks";
import { SIZE } from "../utils";

const handlers = [
  rest.get("/api/bookmarks", (req, res, ctx) => {
    let messageId = req.url.searchParams.get("messageId");
    if (messageId === "") messageId = "0";
    const nextId = Number(req.url.searchParams.get("messageId")) + 1;
    const index = bookmarks.findIndex(({ id }) => id === nextId);
    const newBookmarks = bookmarks.slice(index, index + SIZE);
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        bookmarks: newBookmarks,
        isLast: newBookmarks.length < SIZE,
      })
    );
  }),
];

export default handlers;
