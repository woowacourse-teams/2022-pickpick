import useDatePicker from "@src/components/AddReminder/@hooks/useDatePicker";
import useTimePicker from "@src/components/AddReminder/@hooks/useTimePicker";
import DatePicker from "@src/components/AddReminder/DateTimePicker/DatePicker";
import TimePicker from "@src/components/AddReminder/DateTimePicker/TimePicker";

import useMutateReminder from "@src/hooks/@query/useMutateReminder";

import { REMINDER_BUTTON_TEXT } from "@src/@constants";
import { FlexRow } from "@src/@styles/shared";
import { ValueOf } from "@src/@types/utils";

import * as Styled from "./style";

export type ButtonText = ValueOf<typeof REMINDER_BUTTON_TEXT>;

interface Props {
  messageId: number;
  remindDate: string;
  handleCloseReminderModal: VoidFunction;
  refetchFeed: VoidFunction;
}

function AddReminder({
  messageId,
  remindDate,
  handleCloseReminderModal,
  refetchFeed,
}: Props) {
  const {
    yearRef,
    monthRef,
    dateRef,
    checkedYear,
    checkedMonth,
    checkedDate,
    handleChangeYear,
    handleChangeMonth,
    handleChangeDate,
    handleResetDatePickerPosition,
  } = useDatePicker({ remindDate });

  const {
    meridiemRef,
    AMHourRef,
    PMHourRef,
    minuteRef,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
    handleChangeMeridiem,
    handleChangeHour,
    handleChangeMinute,
    handleResetTimePickerPosition,
  } = useTimePicker({ remindDate });

  const { handleCreateReminder, handleModifyReminder, handleRemoveReminder } =
    useMutateReminder({ handleCloseReminderModal, refetchFeed });

  return (
    <Styled.Container>
      <Styled.Title>리마인더 생성</Styled.Title>

      <DatePicker
        yearRef={yearRef}
        monthRef={monthRef}
        dateRef={dateRef}
        checkedYear={checkedYear}
        checkedMonth={checkedMonth}
        checkedDate={checkedDate}
        handleChangeYear={handleChangeYear}
        handleChangeMonth={handleChangeMonth}
        handleChangeDate={handleChangeDate}
        handleResetDatePickerPosition={handleResetDatePickerPosition}
      />

      <TimePicker
        meridiemRef={meridiemRef}
        AMHourRef={AMHourRef}
        PMHourRef={PMHourRef}
        minuteRef={minuteRef}
        checkedMeridiem={checkedMeridiem}
        checkedHour={checkedHour}
        checkedMinute={checkedMinute}
        handleChangeMeridiem={handleChangeMeridiem}
        handleChangeHour={handleChangeHour}
        handleChangeMinute={handleChangeMinute}
        handleResetTimePickerPosition={handleResetTimePickerPosition}
      />

      <FlexRow justifyContent="flex-end" gap="8px" marginTop="18px">
        <Styled.Button
          text={REMINDER_BUTTON_TEXT.CANCEL}
          type="button"
          onClick={handleCloseReminderModal}
        >
          {REMINDER_BUTTON_TEXT.CANCEL}
        </Styled.Button>

        {!remindDate && (
          <Styled.Button
            text={REMINDER_BUTTON_TEXT.CREATE}
            type="button"
            onClick={() =>
              handleCreateReminder({
                messageId,
                checkedYear,
                checkedMonth,
                checkedDate,
                checkedMeridiem,
                checkedHour,
                checkedMinute,
              })
            }
          >
            {REMINDER_BUTTON_TEXT.CREATE}
          </Styled.Button>
        )}

        {remindDate && (
          <Styled.Button
            text={REMINDER_BUTTON_TEXT.REMOVE}
            type="button"
            onClick={() => handleRemoveReminder(messageId)}
          >
            {REMINDER_BUTTON_TEXT.REMOVE}
          </Styled.Button>
        )}

        {remindDate && (
          <Styled.Button
            text={REMINDER_BUTTON_TEXT.MODIFY}
            type="button"
            onClick={() =>
              handleModifyReminder({
                messageId,
                checkedYear,
                checkedMonth,
                checkedDate,
                checkedMeridiem,
                checkedHour,
                checkedMinute,
              })
            }
          >
            {REMINDER_BUTTON_TEXT.MODIFY}
          </Styled.Button>
        )}
      </FlexRow>
    </Styled.Container>
  );
}

export default AddReminder;
