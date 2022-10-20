import { rest } from "msw";

import { messages } from "../data/reminders";

const handlers = [
  rest.get("/api/reminders", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        reminders: messages,
        hasFuture: false,
      })
    );
  }),
];

export default handlers;
