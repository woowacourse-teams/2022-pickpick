import { useState } from "react";

function useSetTargetMessage() {
  const [targetMessageId, setTargetMessageId] = useState("");
  const [isTargetMessageSetReminded, setIsTargetMessageSetReminded] =
    useState(false);

  const handleUpdateTargetMessageId = (id: string) => {
    setTargetMessageId(id);
  };

  const handleUpdateTargetMessageSetReminded = (isSetReminded: boolean) => {
    setIsTargetMessageSetReminded(isSetReminded);
  };

  const handleInitializeTargetMessageId = () => {
    setTargetMessageId("");
  };

  const handleInitializeTargetMessageSetReminded = () => {
    setIsTargetMessageSetReminded(false);
  };

  return {
    messageTargetState: {
      targetMessageId,
      isTargetMessageSetReminded,
    },
    handler: {
      handleUpdateTargetMessageId,
      handleUpdateTargetMessageSetReminded,
      handleInitializeTargetMessageId,
      handleInitializeTargetMessageSetReminded,
    },
  };
}

export default useSetTargetMessage;
