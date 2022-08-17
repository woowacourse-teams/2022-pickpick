import { useState } from "react";

interface ReminderTarget {
  id: string;
  remindDate?: string;
}

function useSetTargetMessage() {
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
