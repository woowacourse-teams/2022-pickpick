import { messages } from "../data/messages";
import { rest } from "msw";
import { SIZE, BODY_TYPE } from "../utils";

let bookmarks = [] as any;

const handlers = [
  rest.get("/api/bookmarks", (req, res, ctx) => {
    let messageId = req.url.searchParams.get("messageId");
    if (messageId === "") messageId = "0";
    const nextId = Number(req.url.searchParams.get("messageId")) + 1;
    const index = bookmarks.findIndex(
      ({ id }: { id: number }) => id === nextId
    );
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

  rest.post("/api/bookmarks", (req, res, ctx) => {
    const body = req.body as BODY_TYPE;
    const messageId = Number(body.messageId);

    const message = messages.find(({ id }) => id === messageId);
    if (!message) return res(ctx.status(404), ctx.delay(500));
    message.id = bookmarks.length + 1;
    bookmarks.push(message);

    return res(ctx.status(200), ctx.delay(500));
  }),

  rest.delete("/api/bookmarks", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const newBookmarks = bookmarks.filter(
      ({ id }: { id: number }) => id !== messageId
    );
    bookmarks = newBookmarks;
    return res(ctx.status(200), ctx.delay(500));
  }),
];

export default handlers;
