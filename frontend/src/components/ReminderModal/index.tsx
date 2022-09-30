import * as Styled from "./style";
import { FlexRow } from "@src/@styles/shared";
import useMutateReminder from "@src/hooks/query/useMutateReminder";
import useTimePicker from "@src/components/ReminderModal/@hooks/useTimePicker";
import useDatePicker from "@src/components/ReminderModal/@hooks/useDatePicker";
import DatePicker from "@src/components/ReminderModal/DateTimePicker/DatePicker";
import TimePicker from "@src/components/ReminderModal/DateTimePicker/TimePicker";

export type ButtonText = "생성" | "수정" | "취소" | "삭제";

interface Props {
  messageId: number;
  remindDate: string;
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

function ReminderModal({
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
          text="취소"
          type="button"
          onClick={handleCloseReminderModal}
        >
          취소
        </Styled.Button>

        {!remindDate && (
          <Styled.Button
            text="생성"
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
            생성
          </Styled.Button>
        )}

        {remindDate && (
          <>
            <Styled.Button
              text="삭제"
              type="button"
              onClick={() => handleRemoveReminder(messageId)}
            >
              삭제
            </Styled.Button>

            <Styled.Button
              text="수정"
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
              수정
            </Styled.Button>
          </>
        )}
      </FlexRow>
    </Styled.Container>
  );
}

export default ReminderModal;
