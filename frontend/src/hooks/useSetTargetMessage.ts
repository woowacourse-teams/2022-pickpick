import { useState } from "react";

interface ReminderTarget {
  id: string;
  remindDate?: string;
}

interface ReturnType {
  reminderTarget: ReminderTarget;
  handleUpdateReminderTarget: (reminderTarget: ReminderTarget) => void;
  handleInitializeReminderTarget: () => void;
}

function useSetTargetMessage(): ReturnType {
  const [reminderTarget, setReminderTarget] = useState<ReminderTarget>({
    id: "",
    remindDate: "",
  });

  const handleUpdateReminderTarget = (reminderTarget: ReminderTarget) => {
    setReminderTarget({ ...reminderTarget });
  };

  const handleInitializeReminderTarget = () => {
    setReminderTarget({ id: "", remindDate: "" });
  };

  return {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  };
}

export default useSetTargetMessage;
