import { rest } from "msw";

import { channels, subscribedChannels } from "../data/channels";

const handlers = [
  rest.get("/api/channels", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        channels,
      })
    );
  }),

  rest.get("/api/channel-subscription", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        channels: subscribedChannels,
      })
    );
  }),
];

export default handlers;
