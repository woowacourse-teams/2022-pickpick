import { ChangeEventHandler, RefObject } from "react";
import { useTheme } from "styled-components";

import Dropdown from "@src/components/@shared/Dropdown";
import ReminderIconActive from "@src/components/@svgIcons/ReminderIconActive";
import * as Styled from "@src/components/AddReminder/DateTimePicker/DatePicker/style";
import DateTimePickerOptions from "@src/components/AddReminder/DateTimePicker/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/AddReminder/DateTimePicker/DateTimePickerToggle";

import { MERIDIEM, TIME_UNIT } from "@src/@constants/date";
import { AM_HOURS, MINUTES, PM_HOURS } from "@src/@constants/date";
import { FlexColumn } from "@src/@styles/shared";
import { Meridiem } from "@src/@types/date";
import { parsePickerOptionText } from "@src/@utils/date";

interface Props {
  meridiemRef: RefObject<HTMLDivElement>;
  AMHourRef: RefObject<HTMLDivElement>;
  PMHourRef: RefObject<HTMLDivElement>;
  minuteRef: RefObject<HTMLDivElement>;
  checkedMeridiem: Meridiem;
  checkedHour: number;
  checkedMinute: number;
  handleChangeMeridiem: ChangeEventHandler<HTMLInputElement>;
  handleChangeHour: ChangeEventHandler<HTMLInputElement>;
  handleChangeMinute: ChangeEventHandler<HTMLInputElement>;
  handleResetTimePickerPosition: VoidFunction;
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
  const theme = useTheme();

  return (
    <Dropdown toggleHandler={handleResetTimePickerPosition}>
      {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
        return (
          <FlexColumn ref={innerRef}>
            <Styled.Subtitle>시간</Styled.Subtitle>
            <DateTimePickerToggle
              text={`
              ${checkedMeridiem}
              ${parsePickerOptionText({
                optionText: checkedHour,
                unit: TIME_UNIT.HOUR,
              })}
              ${parsePickerOptionText({
                optionText: checkedMinute,
                unit: TIME_UNIT.MINUTE,
              })}
              `}
              handleToggleDropdown={handleToggleDropdown}
            >
              <ReminderIconActive
                width="16px"
                height="16px"
                fill={theme.COLOR.SECONDARY.DEFAULT}
              />
            </DateTimePickerToggle>

            {isDropdownOpened && (
              <Styled.TextOptionContainer>
                <Styled.TextOptionsWrapper ref={meridiemRef}>
                  <DateTimePickerOptions
                    optionTexts={[MERIDIEM.AM, MERIDIEM.PM]}
                    checkedText={checkedMeridiem}
                    handleChangeText={handleChangeMeridiem}
                  />
                </Styled.TextOptionsWrapper>

                {checkedMeridiem === MERIDIEM.AM && (
                  <Styled.TextOptionsWrapper ref={AMHourRef}>
                    <DateTimePickerOptions
                      optionTexts={AM_HOURS}
                      unit={TIME_UNIT.HOUR}
                      checkedText={checkedHour}
                      handleChangeText={handleChangeHour}
                    />
                  </Styled.TextOptionsWrapper>
                )}

                {checkedMeridiem === MERIDIEM.PM && (
                  <Styled.TextOptionsWrapper ref={PMHourRef}>
                    <DateTimePickerOptions
                      optionTexts={PM_HOURS}
                      unit={TIME_UNIT.HOUR}
                      checkedText={checkedHour}
                      handleChangeText={handleChangeHour}
                    />
                  </Styled.TextOptionsWrapper>
                )}

                <Styled.TextOptionsWrapper ref={minuteRef}>
                  <DateTimePickerOptions
                    optionTexts={MINUTES}
                    unit={TIME_UNIT.MINUTE}
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
