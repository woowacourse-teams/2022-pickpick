import { useState } from "react";
import Dimmer from "../@shared/Dimmer";
import DropdownMenu from "../DropdownMenu";
import DropdownToggle from "../DropdownToggle";
import * as Styled from "./style";

function Dropdown() {
  const [isOpened, setIsOpened] = useState(false);

  const handleDropdownToggleClick = () => {
    setIsOpened((prev) => !prev);
  };

  const handleDimmerClick = () => {
    setIsOpened(false);
  };

  return (
    <>
      {isOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleDimmerClick} />
      )}
      <Styled.Container>
        <DropdownToggle text="Today" onClick={handleDropdownToggleClick} />
        {isOpened && <DropdownMenu date="어제" />}
      </Styled.Container>
    </>
  );
}

export default Dropdown;
