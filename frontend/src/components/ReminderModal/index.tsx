import * as Styled from "./style";
import Calendar from "@public/assets/icons/Calendar.svg";
import AlarmIcon from "@public/assets/icons/AlarmIcon-Active.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import useSetReminder from "@src/hooks/useSetReminder";
import DateTimePickerOptions from "@src/components/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/DateTimePickerToggle";

export type ButtonText = "생성" | "수정" | "취소";

interface Props {
  targetMessageId: string;
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

function ReminderModal({
  targetMessageId,
  handleCloseReminderModal,
  refetchFeed,
}: Props) {
  const {
    ref: { yearRef, monthRef, dateRef, meridiemRef, hourRef, minuteRef },
    dateStateArray: { meridiems, hours, minutes, years, months, dates },
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
      handleSubmit,
    },
  } = useSetReminder({
    targetMessageId,
    handleCloseReminderModal,
    refetchFeed,
  });

  return (
    <Styled.Container>
      <Styled.Title>리마인더 생성</Styled.Title>

      <Dropdown>
        {({ isDropdownOpened, handleToggleDropdown }) => {
          handleToggleDateTimePicker();

          return (
            <FlexColumn marginBottom="10px">
              <Styled.Subtitle>언제</Styled.Subtitle>

              <DateTimePickerToggle
                text={`${checkedYear} ${checkedMonth} ${checkedDate.padStart(
                  3,
                  "0"
                )}`}
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
                      checkedText={checkedYear}
                      handleChangeText={handleChangeYear}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={monthRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={months}
                      checkedText={checkedMonth}
                      handleChangeText={handleChangeMonth}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={dateRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={dates}
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
        {({ isDropdownOpened, handleToggleDropdown }) => {
          handleToggleDateTimePicker();

          return (
            <FlexColumn>
              <Styled.Subtitle>시간</Styled.Subtitle>

              <DateTimePickerToggle
                text={`${checkedMeridiem} ${checkedHour} ${checkedMinute.padStart(
                  3,
                  "0"
                )}`}
                handleToggleDropdown={handleToggleDropdown}
              >
                <AlarmIcon width="16px" height="16px" fill="#8B8B8B" />
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

                  <Styled.TextOptionsWrapper ref={hourRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={hours}
                      checkedText={checkedHour}
                      handleChangeText={handleChangeHour}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={minuteRef}>
                    <DateTimePickerOptions
                      needZeroPaddingStart={true}
                      optionTexts={minutes}
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

      <FlexRow gap="8px" margin-top="18px" justifyContent="flex-end">
        <Styled.Button
          text="취소"
          type="button"
          onClick={handleCloseReminderModal}
        >
          취소
        </Styled.Button>
        <Styled.Button text="생성" type="button" onClick={handleSubmit}>
          생성
        </Styled.Button>
      </FlexRow>
    </Styled.Container>
  );
}

export default ReminderModal;
