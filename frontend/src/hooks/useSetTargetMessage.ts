import { useState } from "react";

interface ReminderTarget {
  id: string;
  isSetReminded: boolean;
}

function useSetTargetMessage() {
  const [reminderTarget, setReminderTarget] = useState<ReminderTarget>({
    id: "",
    isSetReminded: false,
  });

  const handleUpdateReminderTarget = (reminderTarget: ReminderTarget) => {
    setReminderTarget({ ...reminderTarget });
  };

  const handleInitializeReminderTarget = () => {
    setReminderTarget({ id: "", isSetReminded: false });
  };

  return {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  };
}

export default useSetTargetMessage;
