import { rest } from "msw";

const handlers = [
  rest.get("/api/auth", (req, res, ctx) => {
    const token = req.headers.get("authorization")?.split("Bearer")[1];
    if (!token || token.length === 0) return res(ctx.status(401));
    return res(ctx.status(200), ctx.delay(500));
  }),
];

export default handlers;
