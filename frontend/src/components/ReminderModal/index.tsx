import * as Styled from "./style";
import Calendar from "@public/assets/icons/Calendar.svg";
import AlarmIcon from "@public/assets/icons/AlarmIcon-Active.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import useReminderModal from "@src/hooks/useReminderModal";
import ReminderModalOptions from "../ReminderModalOptions";
import ReminderModalDropdownToggle from "../ReminderModalToggleDropdown";

export type ButtonText = "생성" | "수정" | "취소";

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
      handleOpenDropdown,
      handleSubmit,
    },
  } = useReminderModal();

  return (
    <Styled.Container>
      <Styled.Title>리마인더 생성</Styled.Title>

      <Dropdown>
        {({ isDropdownOpened, handleToggleDropdown }) => {
          handleOpenDropdown(isDropdownOpened);

          return (
            <FlexColumn marginBottom="10px">
              <Styled.Subtitle>언제</Styled.Subtitle>

              <ReminderModalDropdownToggle
                text={`${checkedYear} ${checkedMonth} ${checkedDate.padStart(
                  3,
                  "0"
                )}`}
                IconComponent={() => (
                  <Calendar width="16px" height="16px" fill="#8B8B8B" />
                )}
                handleToggleDropdown={handleToggleDropdown}
              />

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptionsWrapper ref={yearRef}>
                    <ReminderModalOptions
                      isPadStart={false}
                      optionTexts={years}
                      checkedText={checkedYear}
                      handleChangeText={handleChangeYear}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={monthRef}>
                    <ReminderModalOptions
                      isPadStart={true}
                      optionTexts={months}
                      checkedText={checkedMonth}
                      handleChangeText={handleChangeMonth}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={dateRef}>
                    <ReminderModalOptions
                      isPadStart={true}
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
          handleOpenDropdown(isDropdownOpened);

          return (
            <FlexColumn>
              <Styled.Subtitle>시간</Styled.Subtitle>

              <ReminderModalDropdownToggle
                text={`${checkedMeridiem} ${checkedHour} ${checkedMinute.padStart(
                  3,
                  "0"
                )}`}
                IconComponent={() => (
                  <AlarmIcon width="16px" height="16px" fill="#8B8B8B" />
                )}
                handleToggleDropdown={handleToggleDropdown}
              />

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptionsWrapper ref={meridiemRef}>
                    <ReminderModalOptions
                      isPadStart={false}
                      optionTexts={meridiems}
                      checkedText={checkedMeridiem}
                      handleChangeText={handleChangeMeridiem}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={hourRef}>
                    <ReminderModalOptions
                      isPadStart={true}
                      optionTexts={hours}
                      checkedText={checkedHour}
                      handleChangeText={handleChangeHour}
                    />
                  </Styled.TextOptionsWrapper>

                  <Styled.TextOptionsWrapper ref={minuteRef}>
                    <ReminderModalOptions
                      isPadStart={true}
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
