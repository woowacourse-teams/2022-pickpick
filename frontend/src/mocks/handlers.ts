import { rest } from "msw";
import { messages, channels, subscribedChannels } from "./data";

const SIZE = 20;
let standardDate = "";

export const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const needPastMessage =
      req.url.searchParams.get("needPastMessage") === "true";
    const date = req.url.searchParams.get("date");

    const targetIndex =
      messages.findIndex((message) => message.id === messageId) + 1;

    const newMessages = messages.slice(targetIndex, SIZE + targetIndex);

    if (!date) {
      return res(
        ctx.status(200),
        ctx.delay(500),
        ctx.json({
          messages: newMessages,
          isLast: newMessages.length !== SIZE,
        })
      );
    }

    // 같은 리스트를 계속해서 렌더링하는 문제가 있음
    // 첫번째 요청시에는 date를 기준으로 slice를 쳐야함
    // 두번째 세번째 ~~ 부터는 배열에 id를 기준으로 slice를 쳐야한다.

    if (standardDate !== date) {
      standardDate = date;

      const dateTargetIndex = messages.findIndex(
        (message) => message.postedDate === date
      );

      if (needPastMessage) {
        const newDateMessages = messages.slice(
          dateTargetIndex,
          SIZE + dateTargetIndex
        );

        return res(
          ctx.status(200),
          ctx.delay(500),
          ctx.json({
            messages: newDateMessages,
            isLast: newDateMessages.length !== SIZE,
          })
        );
      }

      const frontIndex =
        dateTargetIndex - SIZE < 0 ? 0 : dateTargetIndex - SIZE;

      const newDateMessages = messages.slice(frontIndex, dateTargetIndex);

      return res(
        ctx.status(200),
        ctx.delay(500),
        ctx.json({
          messages: newDateMessages,
          isLast: newDateMessages.length != SIZE,
        })
      );
    }

    if (standardDate === date) {
      if (messageId === 0) {
        const dateTargetIndex = messages.findIndex(
          (message) => message.postedDate === date
        );

        const newDateMessages = messages.slice(
          dateTargetIndex,
          SIZE + dateTargetIndex
        );

        return res(
          ctx.status(200),
          ctx.delay(500),
          ctx.json({
            messages: newDateMessages,
            isLast: newDateMessages.length !== SIZE,
          })
        );
      }

      const targetIndex = messages.findIndex(
        (message) => message.id === messageId
      );

      if (needPastMessage) {
        const newMessages = messages.slice(targetIndex, SIZE + targetIndex);

        return res(
          ctx.status(200),
          ctx.delay(500),
          ctx.json({
            messages: newMessages,
            isLast: newMessages.length !== SIZE,
          })
        );
      }

      const frontIndex = targetIndex - SIZE < 0 ? 0 : targetIndex - SIZE;

      const newMessages = messages.slice(frontIndex, targetIndex);

      return res(
        ctx.status(200),
        ctx.delay(500),
        ctx.json({
          messages: newMessages,
          isLast: newMessages.length !== SIZE,
        })
      );
    }
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
