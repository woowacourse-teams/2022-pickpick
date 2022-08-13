import { ChangeEvent } from "react";
import * as Styled from "./style";

interface Props {
  isPadStart: boolean;
  optionTexts: string[];
  checkedText: string;
  handleChangeText: (event: ChangeEvent<HTMLInputElement>) => void;
}

function ReminderModalOptions({
  isPadStart,
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
            {isPadStart ? optionText.padStart(3, "0") : optionText}
          </Styled.TextOption>
        </Styled.Container>
      ))}
    </>
  );
}

export default ReminderModalOptions;
