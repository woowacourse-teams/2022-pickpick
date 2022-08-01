import { rest } from "msw";

const handlers = [
  rest.post("/api/auth", (req, res, ctx) => {
    const token = req.headers.get("authorization");
    if (!token) return res(ctx.status(401));
    return res(ctx.status(200), ctx.delay(500));
  }),
];

export default handlers;
