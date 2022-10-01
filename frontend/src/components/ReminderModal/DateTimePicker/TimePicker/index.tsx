import { ChangeEventHandler, RefObject } from "react";

import ReminderIconActive from "@src/components/@svgIcons/ReminderIconActive";
import Dropdown from "@src/components/Dropdown";
import { generateTimeOptions } from "@src/components/ReminderModal/@utils";
import * as Styled from "@src/components/ReminderModal/DateTimePicker/DatePicker/style";
import DateTimePickerOptions from "@src/components/ReminderModal/DateTimePicker/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/ReminderModal/DateTimePicker/DateTimePickerToggle";

import { FlexColumn } from "@src/@styles/shared";

interface Props {
  meridiemRef: RefObject<HTMLDivElement>;
  AMHourRef: RefObject<HTMLDivElement>;
  PMHourRef: RefObject<HTMLDivElement>;
  minuteRef: RefObject<HTMLDivElement>;
  checkedMeridiem: string;
  checkedHour: string;
  checkedMinute: string;
  handleChangeMeridiem: ChangeEventHandler<HTMLInputElement>;
  handleChangeHour: ChangeEventHandler<HTMLInputElement>;
  handleChangeMinute: ChangeEventHandler<HTMLInputElement>;
  handleResetTimePickerPosition: () => void;
}

function TimePicker({
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
}: Props) {
  return (
    <Dropdown toggleHandler={handleResetTimePickerPosition}>
      {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
        const { meridiems, AMHours, PMHours, minutes } = generateTimeOptions();
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
  );
}

export default TimePicker;
