import { rest } from "msw";

const handlers = [
  rest.get("/api/certification", (req, res, ctx) => {
    return res(ctx.status(200), ctx.delay(500));
  }),

  rest.get("/api/slack-login", (req, res, ctx) => {
    return res(
      ctx.body(
        JSON.stringify({
          token:
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMDEzIiwiaWF0IjoxNjU5NjEzMTI3LCJleHAiOjE2NTk2OTk1Mjd9.9s1dsQ2CIyJGPn75VPW5atsOaJdA9KcMUom__1oLuSk",
          isFirstLogin: true,
        })
      )
    );
  }),
];

export default handlers;
