import { useState } from "react";

interface ReminderTarget {
  id: number;
  remindDate?: string;
}

interface UseSetReminderTargetMessageResult {
  reminderTarget: ReminderTarget;
  handleUpdateReminderTarget: (reminderTarget: ReminderTarget) => void;
  handleInitializeReminderTarget: () => void;
}

function useSetReminderTargetMessage(): UseSetReminderTargetMessageResult {
  const [reminderTarget, setReminderTarget] = useState<ReminderTarget>({
    id: -1,
    remindDate: "",
  });

  const handleUpdateReminderTarget = (reminderTarget: ReminderTarget) => {
    setReminderTarget({ ...reminderTarget });
  };

  const handleInitializeReminderTarget = () => {
    setReminderTarget({ id: -1, remindDate: "" });
  };

  return {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  };
}

export default useSetReminderTargetMessage;
