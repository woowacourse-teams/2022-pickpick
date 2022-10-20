import { ChangeEventHandler } from "react";

import { Meridiem, TimeUnit } from "@src/@types/date";
import { parsePickerOptionText } from "@src/@utils/date";

import * as Styled from "./style";

interface Props {
  optionTexts: readonly string[] | readonly number[];
  unit?: TimeUnit;
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
          <Styled.TextOption role="button">
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
