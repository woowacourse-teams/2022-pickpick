import { deleteReminder, postReminder, putReminder } from "@src/api/reminders";
import { getDateInformation, ISOConverter } from "@src/@utils";
import { useMutation } from "react-query";
import useSnackbar from "./useSnackbar";

interface IsInvalidateDateTimeProps {
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  checkedHour: number;
  checkedMinute: number;
  year: number;
  month: number;
  date: number;
  hour: number;
  minute: number;
}

const isInvalidateDateTime = ({
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedHour,
  checkedMinute,
  year,
  month,
  date,
  hour,
  minute,
}: IsInvalidateDateTimeProps) => {
  if (checkedYear < year) return true;
  if (checkedYear <= year && checkedMonth < month) return true;
  if (checkedYear <= year && checkedMonth <= month && checkedDate < date)
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour < hour
  )
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour <= hour &&
    checkedMinute <= minute
  )
    return true;

  return false;
};

const convertMeridiemHourToStandardHour = (
  meridiem: string,
  meridiemHour: number
): number => {
  if (meridiem === "오후") {
    return meridiemHour === 12 ? 12 : meridiemHour + 12;
  }

  return meridiemHour;
};

interface GetReplaceDateTimeProps {
  checkedYear: string;
  checkedMonth: string;
  checkedDate: string;
  checkedMeridiem: string;
  checkedHour: string;
  checkedMinute: string;
}

const getReplaceDateTime = ({
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedMeridiem,
  checkedHour,
  checkedMinute,
}: GetReplaceDateTimeProps) => {
  const replaceCheckedYear = Number(checkedYear.replace("년", ""));
  const replaceCheckedMonth = Number(checkedMonth.replace("월", ""));
  const replaceCheckedDate = Number(checkedDate.replace("일", ""));

  const replaceCheckedHour = convertMeridiemHourToStandardHour(
    checkedMeridiem,
    Number(checkedHour.replace("시", ""))
  );
  const replaceCheckedMinute = Number(checkedMinute.replace("분", ""));

  return {
    replaceCheckedYear,
    replaceCheckedMonth,
    replaceCheckedDate,
    replaceCheckedHour,
    replaceCheckedMinute,
  };
};

interface handlerProps {
  messageId: number;
  checkedYear: string;
  checkedMonth: string;
  checkedDate: string;
  checkedMeridiem: string;
  checkedHour: string;
  checkedMinute: string;
}

interface Props {
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

interface ReturnType {
  handleCreateReminder: ({
    messageId,
    checkedYear,
    checkedMonth,
    checkedDate,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
  }: handlerProps) => void;
  handleModifyReminder: ({
    messageId,
    checkedYear,
    checkedMonth,
    checkedDate,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
  }: handlerProps) => void;
  handleRemoveReminder: (messageId: number) => void;
}

function useMutateReminder({
  handleCloseReminderModal,
  refetchFeed,
}: Props): ReturnType {
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

  const { year, month, date, hour, minute } = getDateInformation(new Date());

  const handleCreateReminder = ({
    messageId,
    checkedYear,
    checkedMonth,
    checkedDate,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
  }: handlerProps) => {
    const {
      replaceCheckedYear,
      replaceCheckedMonth,
      replaceCheckedDate,
      replaceCheckedHour,
      replaceCheckedMinute,
    } = getReplaceDateTime({
      checkedYear,
      checkedMonth,
      checkedDate,
      checkedMeridiem,
      checkedHour,
      checkedMinute,
    });

    if (
      isInvalidateDateTime({
        checkedYear: replaceCheckedYear,
        checkedMonth: replaceCheckedMonth,
        checkedDate: replaceCheckedDate,
        checkedHour: replaceCheckedHour,
        checkedMinute: replaceCheckedMinute,
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
      `${replaceCheckedYear}-${replaceCheckedMonth}-${replaceCheckedDate}`,
      `${replaceCheckedHour}:${replaceCheckedMinute}`
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
  }: handlerProps) => {
    const {
      replaceCheckedYear,
      replaceCheckedMonth,
      replaceCheckedDate,
      replaceCheckedHour,
      replaceCheckedMinute,
    } = getReplaceDateTime({
      checkedYear,
      checkedMonth,
      checkedDate,
      checkedMeridiem,
      checkedHour,
      checkedMinute,
    });

    if (
      isInvalidateDateTime({
        checkedYear: replaceCheckedYear,
        checkedMonth: replaceCheckedMonth,
        checkedDate: replaceCheckedDate,
        checkedHour: replaceCheckedHour,
        checkedMinute: replaceCheckedMinute,
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
      `${replaceCheckedYear}-${replaceCheckedMonth}-${replaceCheckedDate}`,
      `${replaceCheckedHour}:${replaceCheckedMinute}`
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
