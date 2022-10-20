import { rest } from "msw";

import { messages } from "../data/messages";
import { BODY_TYPE } from "../utils";

let bookmarks = messages
  .filter(({ isBookmarked }) => isBookmarked)
  .map((message) => {
    return { ...message, messageId: message.id };
  });

const handlers = [
  rest.get("/api/bookmarks", (req, res, ctx) => {
    console.log(bookmarks);
    return res(
      ctx.status(200),
      ctx.json({
        bookmarks,
        hasFuture: false,
      })
    );
  }),

  rest.post("/api/bookmarks", (req, res, ctx) => {
    const body = req.body as BODY_TYPE;
    const messageId = Number(body.messageId);

    const message = messages.find(({ id }) => id === messageId);
    if (!message) return res(ctx.status(404), ctx.delay(500));
    message.isBookmarked = true;

    bookmarks.push({ ...message, messageId });

    return res(ctx.status(200));
  }),

  rest.delete("/api/bookmarks", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const message = messages.find(({ id }) => id === messageId);
    if (message) {
      message.isBookmarked = false;
    }

    const newBookmarks = bookmarks.filter(({ id }: { id: number }) => {
      return id !== messageId;
    });
    bookmarks = newBookmarks;

    return res(ctx.status(200));
  }),
];

export default handlers;
