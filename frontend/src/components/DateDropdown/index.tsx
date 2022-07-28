import Dropdown from "../Dropdown";
import * as Styled from "./style";

interface Props {
  postedDate: string;
}

function DateDropDown({ postedDate }: Props) {
  return (
    <Styled.Container>
      <Dropdown postedDate={postedDate} />
    </Styled.Container>
  );
}

export default DateDropDown;
