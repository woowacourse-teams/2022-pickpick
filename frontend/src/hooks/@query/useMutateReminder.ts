import { useMutation } from "react-query";

import useSnackbar from "@src/hooks/useSnackbar";

import { MESSAGE } from "@src/@constants";
import { Meridiem } from "@src/@types/date";
import {
  ISOConverter,
  getFullDateInformation,
  getStandardHourFormMeridiemHour,
  isInvalidReminderTime,
} from "@src/@utils/date";

import { deleteReminder, postReminder, putReminder } from "@src/api/reminders";

interface HandlerProps {
  messageId: number;
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  checkedMeridiem: Meridiem;
  checkedHour: number;
  checkedMinute: number;
}

interface Props {
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

type Handler = ({
  messageId,
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedMeridiem,
  checkedHour,
  checkedMinute,
}: HandlerProps) => void;

interface UseMutateReminderResult {
  handleCreateReminder: Handler;
  handleModifyReminder: Handler;
  handleRemoveReminder: (messageId: number) => void;
}

function useMutateReminder({
  handleCloseReminderModal,
  refetchFeed,
}: Props): UseMutateReminderResult {
  const { openFailureSnackbar } = useSnackbar();
  const { mutate: addReminder } = useMutation(postReminder, {
    onSuccess: () => {
      handleCloseReminderModal();
      refetchFeed();
    },
  });

  const { mutate: modifyReminder } = useMutation(putReminder, {
    onSuccess: () => {
      handleCloseReminderModal();
      refetchFeed();
    },
  });

  const { mutate: removeReminder } = useMutation(deleteReminder, {
    onSuccess: () => {
      refetchFeed();
      handleCloseReminderModal();
    },
  });

  const { year, month, date, hour, minute } = getFullDateInformation(
    new Date()
  );

  const handleCreateReminder = ({
    messageId,
    checkedYear,
    checkedMonth,
    checkedDate,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
  }: HandlerProps) => {
    const parsedHour = getStandardHourFormMeridiemHour(
      checkedHour,
      checkedMeridiem
    );
    console.log(
      isInvalidReminderTime({
        checkedYear,
        checkedMonth,
        checkedDate,
        checkedHour: parsedHour,
        checkedMinute,
        year,
        month,
        date,
        hour,
        minute,
      })
    );
    if (
      isInvalidReminderTime({
        checkedYear,
        checkedMonth,
        checkedDate,
        checkedHour: parsedHour,
        checkedMinute,
        year,
        month,
        date,
        hour,
        minute,
      })
    ) {
      openFailureSnackbar(MESSAGE.INVALID_REMINDER_TIME);

      return;
    }

    const reminderISODateTime = ISOConverter(
      `${checkedYear}-${checkedMonth}-${checkedDate}`,
      `${parsedHour}:${checkedMinute}`
    );

    addReminder({
      messageId: Number(messageId),
      reminderDate: reminderISODateTime,
    });
  };

  const handleModifyReminder = ({
    messageId,
    checkedYear,
    checkedMonth,
    checkedDate,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
  }: HandlerProps) => {
    const parsedHour = getStandardHourFormMeridiemHour(
      checkedHour,
      checkedMeridiem
    );
    if (
      isInvalidReminderTime({
        checkedYear,
        checkedMonth,
        checkedDate,
        checkedHour: parsedHour,
        checkedMinute,
        year,
        month,
        date,
        hour,
        minute,
      })
    ) {
      openFailureSnackbar(MESSAGE.INVALID_REMINDER_TIME);

      return;
    }

    const reminderISODateTime = ISOConverter(
      `${checkedYear}-${checkedMonth}-${checkedDate}`,
      `${parsedHour}:${checkedMinute}`
    );

    modifyReminder({
      messageId: Number(messageId),
      reminderDate: reminderISODateTime,
    });
  };

  const handleRemoveReminder = (messageId: number) => {
    if (window.confirm(MESSAGE.CONFIRM_REMINDER_REMOVE)) {
      removeReminder(messageId);
    }
  };

  return {
    handleCreateReminder,
    handleModifyReminder,
    handleRemoveReminder,
  };
}

export default useMutateReminder;
