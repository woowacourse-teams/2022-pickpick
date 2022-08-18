import useDropdown from "@src/hooks/useDropdown";
import useOuterClick from "@src/hooks/useOuterClick";
import { MutableRefObject, RefObject } from "react";
// import Dimmer from "@src/components/@shared/Dimmer";

interface ChildrenProps {
  innerRef: any;
  isDropdownOpened: boolean;
  handleOpenDropdown: () => void;
  handleCloseDropdown: () => void;
  handleToggleDropdown: () => void;
}

interface Props {
  children: ({ handleToggleDropdown }: ChildrenProps) => JSX.Element;
}

function Dropdown({ children }: Props) {
  const {
    isDropdownOpened,
    handleOpenDropdown,
    handleCloseDropdown,
    handleToggleDropdown,
  } = useDropdown();

  const { innerRef } = useOuterClick(handleCloseDropdown);

  return (
    <>
      {/* {isDropdownOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseDropdown} />
      )} */}

      {children({
        innerRef,
        isDropdownOpened,
        handleOpenDropdown,
        handleCloseDropdown,
        handleToggleDropdown,
      })}
    </>
  );
}

export default Dropdown;
