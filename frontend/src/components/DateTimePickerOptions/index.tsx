import { parsedOptionText } from "@src/@utils";
import { ChangeEventHandler } from "react";
import * as Styled from "./style";

interface Props {
  needZeroPaddingStart: boolean;
  optionTexts: string[];
  checkedText: string;
  handleChangeText: ChangeEventHandler<HTMLInputElement>;
}

function DateTimePickerOptions({
  needZeroPaddingStart,
  optionTexts,
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
            {parsedOptionText({ needZeroPaddingStart, optionText })}
          </Styled.TextOption>
        </Styled.Container>
      ))}
    </>
  );
}

export default DateTimePickerOptions;
