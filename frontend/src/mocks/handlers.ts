import { rest } from "msw";
import { messages } from "./data";

export const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const size = Number(req.url.searchParams.get("size"));

    const targetIndex =
      messages.findIndex((message) => message.id === messageId) + 1;

    const newMessages = messages.slice(targetIndex, size + targetIndex);

    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        messages: newMessages,
        isLast: newMessages.length !== size,
      })
    );
  }),
];
