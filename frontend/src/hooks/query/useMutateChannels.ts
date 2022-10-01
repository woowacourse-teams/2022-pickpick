import { useMutation } from "react-query";

import { subscribeChannel, unsubscribeChannel } from "@src/api/channels";

interface Props {
  handleSettleSubscribeChannel?: () => void;
  handleSettleUnsubscribeChannel?: () => void;
}

function useMutateChannels({
  handleSettleSubscribeChannel,
  handleSettleUnsubscribeChannel,
}: Props) {
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
