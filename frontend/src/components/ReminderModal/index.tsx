import * as Styled from "./style";
import Calendar from "@public/assets/icons/Calendar.svg";
import ReminderIconActive from "@public/assets/icons/ReminderIcon-Active.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import useSetReminder from "@src/hooks/useSetReminder";
import DateTimePickerOptions from "@src/components/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/DateTimePickerToggle";
import useMutateReminder from "@src/hooks/useMutateReminder";
import { getDateInformation } from "@src/@utils";

const generateDateTimeOptions = () => {
  const { year, month } = getDateInformation(new Date());
  const { date: lastDate } = getDateInformation(new Date(year, month, 0));

  const years = [year, year + 1, year + 2].map((year) => year.toString());
  const months = Array.from({ length: 12 }, (_, index) =>
    (index + 1).toString()
  );
  const dates = Array.from({ length: lastDate }, (_, index) =>
    (index + 1).toString()
  );

  const meridiems = ["오전", "오후"];
  const AMHours = Array.from({ length: 12 }, (_, index) => index.toString());
  const PMHours = Array.from({ length: 12 }, (_, index) => {
    if (index === 0) return "12";

    return index.toString();
  });
  const minutes = Array.from({ length: 6 }, (_, index) =>
    (index * 10).toString()
  );

  return {
    years,
    months,
    dates,
    meridiems,
    AMHours,
    PMHours,
    minutes,
  };
};

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
    ref: {
      yearRef,
      monthRef,
      dateRef,
      meridiemRef,
      AMHourRef,
      PMHourRef,
      minuteRef,
    },
    checkedState: {
      checkedMeridiem,
      checkedHour,
      checkedMinute,
      checkedYear,
      checkedMonth,
      checkedDate,
    },
    handler: {
      handleChangeMeridiem,
      handleChangeHour,
      handleChangeMinute,
      handleChangeYear,
      handleChangeMonth,
      handleChangeDate,
      handleToggleDateTimePicker,
    },
  } = useSetReminder({
    remindDate,
  });

  const { handleCreateReminder, handleModifyReminder, handleRemoveReminder } =
    useMutateReminder({ handleCloseReminderModal, refetchFeed });

  const { years, months, dates, meridiems, AMHours, PMHours, minutes } =
    generateDateTimeOptions();

  return (
    <Styled.Container>
      <Styled.Title>리마인더 생성</Styled.Title>

      <Dropdown>
        {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
          handleToggleDateTimePicker();

          return (
            <FlexColumn marginBottom="10px" ref={innerRef}>
              <Styled.Subtitle>언제</Styled.Subtitle>

              <DateTimePickerToggle
                text={`${checkedYear}년 ${checkedMonth}월 ${checkedDate.padStart(
                  2,
                  "0"
                )}일`}
                handleToggleDropdown={handleToggleDropdown}
              >
                <Calendar width="16px" height="16px" fill="#8B8B8B" />
              </DateTimePickerToggle>

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptionsWrapper ref={yearRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={false}
                      optionTexts={years}
                      unit="년"
                      checkedText={checkedYear}
                      handleChangeText={handleChangeYear}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={monthRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={months}
                      unit="월"
                      checkedText={checkedMonth}
                      handleChangeText={handleChangeMonth}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={dateRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={dates}
                      unit="일"
                      checkedText={checkedDate}
                      handleChangeText={handleChangeDate}
                    />
                  </Styled.TextOptionsWrapper>
                </Styled.TextOptionContainer>
              )}
            </FlexColumn>
          );
        }}
      </Dropdown>

      <Dropdown>
        {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
          handleToggleDateTimePicker();

          return (
            <FlexColumn ref={innerRef}>
              <Styled.Subtitle>시간</Styled.Subtitle>

              <DateTimePickerToggle
                text={`${checkedMeridiem} ${checkedHour}시 ${checkedMinute.padStart(
                  2,
                  "0"
                )}분`}
                handleToggleDropdown={handleToggleDropdown}
              >
                <ReminderIconActive width="16px" height="16px" fill="#8B8B8B" />
              </DateTimePickerToggle>

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptionsWrapper ref={meridiemRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={false}
                      optionTexts={meridiems}
                      checkedText={checkedMeridiem}
                      handleChangeText={handleChangeMeridiem}
                    />
                  </Styled.TextOptionsWrapper>

                  {checkedMeridiem === "오전" && (
                    <Styled.TextOptionsWrapper ref={AMHourRef}>
                      <DateTimePickerOptions
                        needZeroPaddingStart={true}
                        optionTexts={AMHours}
                        unit="시"
                        checkedText={checkedHour}
                        handleChangeText={handleChangeHour}
                      />
                    </Styled.TextOptionsWrapper>
                  )}

                  {checkedMeridiem === "오후" && (
                    <Styled.TextOptionsWrapper ref={PMHourRef}>
                      <DateTimePickerOptions
                        needZeroPaddingStart={true}
                        optionTexts={PMHours}
                        unit="시"
                        checkedText={checkedHour}
                        handleChangeText={handleChangeHour}
                      />
                    </Styled.TextOptionsWrapper>
                  )}

                  <Styled.TextOptionsWrapper ref={minuteRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={minutes}
                      unit="분"
                      checkedText={checkedMinute}
                      handleChangeText={handleChangeMinute}
                    />
                  </Styled.TextOptionsWrapper>
                </Styled.TextOptionContainer>
              )}
            </FlexColumn>
          );
        }}
      </Dropdown>

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
