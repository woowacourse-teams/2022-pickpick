import { ChangeEventHandler, RefObject } from "react";

import Dropdown from "@src/components/@shared/Dropdown";
import CalendarIcon from "@src/components/@svgIcons/CalendarIcon";
import { generateDateOptions } from "@src/components/AddReminder/@utils";
import DateTimePickerOptions from "@src/components/AddReminder/DateTimePicker/DateTimePickerOptions";
import DateTimePickerToggle from "@src/components/AddReminder/DateTimePicker/DateTimePickerToggle";

import { FlexColumn } from "@src/@styles/shared";

import * as Styled from "./style";

interface Props {
  yearRef: RefObject<HTMLDivElement>;
  monthRef: RefObject<HTMLDivElement>;
  dateRef: RefObject<HTMLDivElement>;
  checkedYear: string;
  checkedMonth: string;
  checkedDate: string;
  handleChangeYear: ChangeEventHandler<HTMLInputElement>;
  handleChangeMonth: ChangeEventHandler<HTMLInputElement>;
  handleChangeDate: ChangeEventHandler<HTMLInputElement>;
  handleResetDatePickerPosition: () => void;
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
  return (
    <Dropdown toggleHandler={handleResetDatePickerPosition}>
      {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
        const { years, months, dates } = generateDateOptions();
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
              <CalendarIcon width="16px" height="16px" fill="#8B8B8B" />
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
  );
}

export default DatePicker;
