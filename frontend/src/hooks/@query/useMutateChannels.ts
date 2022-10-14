import { useMutation } from "react-query";

import { subscribeChannel, unsubscribeChannel } from "@src/api/channels";

interface Props {
  handleSettleSubscribeChannel?: VoidFunction;
  handleSettleUnsubscribeChannel?: VoidFunction;
}

type Handler = (id: string) => void;

interface UseMutateChannelsResult {
  handleSubscribeChannel: Handler;
  handleUnSubscribeChannel: Handler;
}

function useMutateChannels({
  handleSettleSubscribeChannel,
  handleSettleUnsubscribeChannel,
}: Props): UseMutateChannelsResult {
  const { mutate: subscribe } = useMutation(subscribeChannel, {
    onSettled: handleSettleSubscribeChannel,
  });

  const { mutate: unsubscribe } = useMutation(unsubscribeChannel, {
    onSettled: handleSettleUnsubscribeChannel,
  });

  const handleSubscribeChannel = (id: string) => {
    subscribe(id);
  };

  const handleUnSubscribeChannel = (id: string) => {
    unsubscribe(id);
  };

  return { handleSubscribeChannel, handleUnSubscribeChannel };
}

export default useMutateChannels;
