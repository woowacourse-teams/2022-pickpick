import { rest } from "msw";
import {
  messages,
  reverseMessages,
  channels,
  subscribedChannels,
} from "./data";

const SIZE = 20;
export const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const needPastMessage =
      req.url.searchParams.get("needPastMessage") === "true";

    const targetIndex =
      messages.findIndex((message) => message.id === messageId) + 1;

    const newMessages = messages.slice(targetIndex, SIZE + targetIndex);
    const newReverseMessages = reverseMessages.slice(
      targetIndex,
      SIZE + targetIndex
    );

    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.json({
        messages: needPastMessage ? newMessages : newReverseMessages,
        isLast: newMessages.length !== SIZE,
      })
    );
  }),

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
