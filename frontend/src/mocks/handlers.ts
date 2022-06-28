import { rest } from "msw";
import { messages } from "./data";

export const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    return res(
      ctx.json({
        messages,
      })
    );
  }),
];
