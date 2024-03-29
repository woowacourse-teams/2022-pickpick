import { ChangeEventHandler, RefObject } from "react";
import { useTheme } from "styled-components";

import Dropdown from "@src/components/@shared/Dropdown";
import CalendarIcon from "@src/components/@svgIcons/CalendarIcon";
import DateTimePickerOptions from "@src/components/AddReminder/DateTimePicker/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/AddReminder/DateTimePicker/DateTimePickerToggle";

import { TIME_UNIT } from "@src/@constants/date";
import { FlexColumn } from "@src/@styles/shared";
import { getFutureDateOption, parsePickerOptionText } from "@src/@utils/date";

import * as Styled from "./style";

interface Props {
  yearRef: RefObject<HTMLDivElement>;
  monthRef: RefObject<HTMLDivElement>;
  dateRef: RefObject<HTMLDivElement>;
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  handleChangeYear: ChangeEventHandler<HTMLInputElement>;
  handleChangeMonth: ChangeEventHandler<HTMLInputElement>;
  handleChangeDate: ChangeEventHandler<HTMLInputElement>;
  handleResetDatePickerPosition: VoidFunction;
}

function DatePicker({
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
}: Props) {
  const theme = useTheme();

  return (
    <Dropdown toggleHandler={handleResetDatePickerPosition}>
      {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
        const { years, months, dates } = getFutureDateOption();

        return (
          <FlexColumn marginBottom="10px" ref={innerRef}>
            <Styled.Subtitle>언제</Styled.Subtitle>

            <DateTimePickerToggle
              text={`
                ${parsePickerOptionText({
                  optionText: checkedYear,
                  unit: TIME_UNIT.YEAR,
                })} 
                ${parsePickerOptionText({
                  optionText: checkedMonth,
                  unit: TIME_UNIT.MONTH,
                })}
                ${parsePickerOptionText({
                  optionText: checkedDate,
                  unit: TIME_UNIT.DATE,
                })}
              `}
              handleToggleDropdown={handleToggleDropdown}
            >
              <CalendarIcon
                width="16px"
                height="16px"
                fill={theme.COLOR.SECONDARY.DEFAULT}
              />
            </DateTimePickerToggle>

            {isDropdownOpened && (
              <Styled.TextOptionContainer>
                <Styled.TextOptionsWrapper ref={yearRef}>
                  <DateTimePickerOptions
                    optionTexts={years}
                    unit={TIME_UNIT.YEAR}
                    checkedText={checkedYear}
                    handleChangeText={handleChangeYear}
                  />
                </Styled.TextOptionsWrapper>

                <Styled.TextOptionsWrapper ref={monthRef}>
                  <DateTimePickerOptions
                    optionTexts={months}
                    unit={TIME_UNIT.MONTH}
                    checkedText={checkedMonth}
                    handleChangeText={handleChangeMonth}
                  />
                </Styled.TextOptionsWrapper>

                <Styled.TextOptionsWrapper ref={dateRef}>
                  <DateTimePickerOptions
                    optionTexts={dates}
                    unit={TIME_UNIT.DATE}
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
  );
}

export default DatePicker;
