import { ChangeEventHandler } from "react";

import { Meridiem } from "@src/@utils/date";

import * as Styled from "./style";

const parsePickerOptionText = ({
  needZeroPaddingStart,
  option,
  unit,
}: {
  needZeroPaddingStart: boolean;
  option: number | string;
  unit?: string;
}) => {
  const unitPostfix = unit ? unit : "";
  if (typeof option === "string") {
    return `${option}${unitPostfix}`;
  }
  const optionText = option.toString();
  return needZeroPaddingStart
    ? `${optionText.padStart(2, "0")}${unitPostfix}`
    : `${optionText}${unitPostfix}`;
};

interface Props {
  needZeroPaddingStart: boolean;
  optionTexts: string[] | number[];
  unit?: "년" | "월" | "일" | "시" | "분";
  checkedText: Meridiem | number;
  handleChangeText: ChangeEventHandler<HTMLInputElement>;
}

function DateTimePickerOptions({
  needZeroPaddingStart,
  optionTexts,
  unit,
  checkedText,
  handleChangeText,
}: Props) {
  return (
    <>
      {optionTexts.map((optionText) => (
        <Styled.Container key={optionText}>
          <Styled.Radio
            type="radio"
            value={optionText}
            onChange={handleChangeText}
            checked={checkedText === optionText}
          />
          <Styled.TextOption>
            {parsePickerOptionText({
              needZeroPaddingStart,
              option: optionText,
              unit,
            })}
          </Styled.TextOption>
        </Styled.Container>
      ))}
    </>
  );
}

export default DateTimePickerOptions;
