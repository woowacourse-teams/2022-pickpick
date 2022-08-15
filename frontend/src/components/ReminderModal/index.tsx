import * as Styled from "./style";
import Calendar from "@public/assets/icons/Calendar.svg";
import AlarmIcon from "@public/assets/icons/AlarmIcon-Active.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import useSetReminder from "@src/hooks/useSetReminder";
import DateTimePickerOptions from "@src/components/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/DateTimePickerToggle";

const BUTTON_TEXT = {
  CREATE: "생성",
  UPDATE: "수정",
  CANCEL: "취소",
};

export type ButtonText = typeof BUTTON_TEXT[keyof typeof BUTTON_TEXT];

interface Props {
  handleCloseReminderModal: () => void;
}

function ReminderModal({ handleCloseReminderModal }: Props) {
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
  } = useSetReminder();

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
          text={BUTTON_TEXT.CANCEL}
          type="button"
          onClick={handleCloseReminderModal}
        >
          {BUTTON_TEXT.CANCEL}
        </Styled.Button>
        <Styled.Button
          text={BUTTON_TEXT.CREATE}
          type="button"
          onClick={handleSubmit}
        >
          {BUTTON_TEXT.CREATE}
        </Styled.Button>
      </FlexRow>
    </Styled.Container>
  );
}

export default ReminderModal;
