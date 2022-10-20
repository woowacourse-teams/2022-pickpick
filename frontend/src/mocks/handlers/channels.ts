import { rest } from "msw";

import { channels, subscribedChannels } from "../data/channels";
import { BODY_TYPE } from "../utils";

let channelsResponse = [...channels];

const handlers = [
  rest.get("/api/channels", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        channels: channelsResponse,
      })
    );
  }),

  rest.get("/api/channel-subscription", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        channels: channelsResponse,
      })
    );
  }),

  rest.post("/api/channel-subscription", (req, res, ctx) => {
    const body = req.body as BODY_TYPE;
    const channelId = Number(body.channelId);
    channelsResponse = channelsResponse.map((channel) => {
      if (channel.id === channelId) {
        return { ...channel, isSubscribed: true };
      }
      return channel;
    });

    return res(ctx.status(200));
  }),

  rest.delete("/api/channel-subscription", (req, res, ctx) => {
    const channelId = Number(req.url.searchParams.get("channelId"));
    channelsResponse = channelsResponse.map((channel) => {
      if (channel.id === channelId) {
        return { ...channel, isSubscribed: false };
      }
      return channel;
    });
    return res(ctx.status(200));
  }),
];

export default handlers;
