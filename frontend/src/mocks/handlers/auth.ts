import { rest } from "msw";

const handlers = [
  rest.get("/api/auth", (req, res, ctx) => {
    return res(ctx.status(200), ctx.delay(500));
  }),
];

export default handlers;
