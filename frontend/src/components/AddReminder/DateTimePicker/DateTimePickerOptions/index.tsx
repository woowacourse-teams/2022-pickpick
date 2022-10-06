import { ChangeEventHandler } from "react";

import { Meridiem } from "@src/@utils/date";

import * as Styled from "./style";

const parsePickerOptionText = ({
  optionText,
  unit,
}: {
  optionText: number | string;
  unit?: string;
}) => {
  const unitPostfix = unit ? unit : "";
  if (typeof optionText === "string") {
    return `${optionText}${unitPostfix}`;
  }

  return `${optionText.toString().padStart(2, "0")}${unitPostfix}`;
};

interface Props {
  optionTexts: string[] | number[];
  unit?: "년" | "월" | "일" | "시" | "분";
  checkedText: Meridiem | number;
  handleChangeText: ChangeEventHandler<HTMLInputElement>;
}

function DateTimePickerOptions({
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
              optionText,
              unit,
            })}
          </Styled.TextOption>
        </Styled.Container>
      ))}
    </>
  );
}

export default DateTimePickerOptions;
