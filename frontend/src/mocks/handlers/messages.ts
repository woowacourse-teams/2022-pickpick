import { DefaultBodyType, ResponseComposition, rest, RestContext } from "msw";
import { messages } from "../data/messages";
import { SIZE } from "../utils";

let standardDate = "";

const getPreviousResponseInfo = (
  res: ResponseComposition<DefaultBodyType>,
  ctx: RestContext,
  targetIndex: number
) => {
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
};

const getNextResponseInfo = (
  res: ResponseComposition<DefaultBodyType>,
  ctx: RestContext,
  targetIndex: number
) => {
  const newMessages = messages.slice(targetIndex, SIZE + targetIndex);

  return res(
    ctx.status(200),
    ctx.delay(500),
    ctx.json({
      messages: newMessages,
      isLast: newMessages.length !== SIZE,
    })
  );
};

const handlers = [
  rest.get("/api/messages", (req, res, ctx) => {
    const messageId = Number(req.url.searchParams.get("messageId"));
    const needPastMessage =
      req.url.searchParams.get("needPastMessage") === "true";
    const date = req.url.searchParams.get("date")?.split("T")[0];

    const targetIndex = messages.findIndex(
      (message) => message.id === messageId
    );

    if (!date && !messageId) {
      return getNextResponseInfo(res, ctx, targetIndex + 1);
    }

    const dateTargetIndex = messages.findIndex(
      (message) => message.postedDate.split("T")[0] === date
    );

    if (standardDate !== date && needPastMessage && date) {
      standardDate = date;

      return getNextResponseInfo(res, ctx, dateTargetIndex);
    }

    if (needPastMessage) {
      return getNextResponseInfo(res, ctx, targetIndex + 1);
    }

    return getPreviousResponseInfo(res, ctx, targetIndex);
  }),
];

export default handlers;
