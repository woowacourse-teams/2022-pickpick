import useDropdown from "@src/hooks/useDropdown";
import useOuterClick from "@src/hooks/useOuterClick";
import { RefObject } from "react";

interface ChildrenProps {
  innerRef: RefObject<HTMLDivElement>;
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
