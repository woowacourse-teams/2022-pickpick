import { ChangeEventHandler, RefObject } from "react";

import Dropdown from "@src/components/@shared/Dropdown";
import ReminderIconActive from "@src/components/@svgIcons/ReminderIconActive";
import * as Styled from "@src/components/AddReminder/DateTimePicker/DatePicker/style";
import DateTimePickerOptions from "@src/components/AddReminder/DateTimePicker/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/AddReminder/DateTimePicker/DateTimePickerToggle";

import { MERIDIEM } from "@src/@constants/date";
import { FlexColumn } from "@src/@styles/shared";
import { Meridiem, getTimeOption } from "@src/@utils/date";

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
        const { AMHours, PMHours, minutes } = getTimeOption();

        return (
          <FlexColumn ref={innerRef}>
            <Styled.Subtitle>시간</Styled.Subtitle>
            <DateTimePickerToggle
              text={`${checkedMeridiem} ${checkedHour}시 ${checkedMinute
                .toString()
                .padStart(2, "0")}분`}
              handleToggleDropdown={handleToggleDropdown}
            >
              <ReminderIconActive width="16px" height="16px" fill="#8B8B8B" />
            </DateTimePickerToggle>

            {isDropdownOpened && (
              <Styled.TextOptionContainer>
                <Styled.TextOptionsWrapper ref={meridiemRef}>
                  <DateTimePickerOptions
                    needZeroPaddingStart={false}
                    optionTexts={[MERIDIEM.AM, MERIDIEM.PM]}
                    checkedText={checkedMeridiem}
                    handleChangeText={handleChangeMeridiem}
                  />
                </Styled.TextOptionsWrapper>

                {checkedMeridiem === MERIDIEM.AM && (
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

                {checkedMeridiem === MERIDIEM.PM && (
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
