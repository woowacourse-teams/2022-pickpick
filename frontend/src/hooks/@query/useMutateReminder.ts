import { useMutation } from "react-query";

import useSnackbar from "@src/hooks/useSnackbar";

import {
  ISOConverter,
  Meridiem,
  getFullDateInformation,
  getFullHourFromMeridiemHour,
  isInvalidateDateTime,
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
    const parsedHour = getFullHourFromMeridiemHour(
      checkedHour,
      checkedMeridiem
    );
    if (
      isInvalidateDateTime({
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
      openFailureSnackbar(
        "리마인더 시간은 현재 시간보다 미래로 설정해주셔야 합니다."
      );

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
    const parsedHour = getFullHourFromMeridiemHour(
      checkedHour,
      checkedMeridiem
    );
    if (
      isInvalidateDateTime({
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
      openFailureSnackbar(
        "리마인더 시간은 현재 시간보다 미래로 설정해주셔야 합니다."
      );

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

  const handleRemoveReminder = async (messageId: number) => {
    if (window.confirm("해당하는 메시지 리마인더를 정말 삭제하시겠습니까?")) {
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
