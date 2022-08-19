import { parsedOptionText } from "@src/@utils";
import { ChangeEventHandler } from "react";
import * as Styled from "./style";

interface Props {
  needZeroPaddingStart: boolean;
  optionTexts: string[];
  unit?: "년" | "월" | "일" | "시" | "분";
  checkedText: string;
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
            {unit &&
              `${parsedOptionText({
                needZeroPaddingStart,
                optionText,
              })}${unit}`}

            {!unit && parsedOptionText({ needZeroPaddingStart, optionText })}
          </Styled.TextOption>
        </Styled.Container>
      ))}
    </>
  );
}

export default DateTimePickerOptions;
