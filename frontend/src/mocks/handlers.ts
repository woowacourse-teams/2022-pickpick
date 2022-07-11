import { rest } from "msw";
import { messages } from "./data";

export const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    const page = Number(req.url.searchParams.get("page"));
    const size = Number(req.url.searchParams.get("size"));

    const slicedMessages = messages.slice((page - 1) * size, size * page);

    return res(
      ctx.json({
        messages: slicedMessages,
        isLast: slicedMessages.length !== 20,
        nextPage: page + 1,
      })
    );
  }),
];
