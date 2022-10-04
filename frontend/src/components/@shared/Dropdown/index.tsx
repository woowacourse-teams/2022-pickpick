import { RefObject, useEffect } from "react";

import useDropdown from "@src/components/@shared/Dropdown/@hooks/useDropdown";

import useOuterClick from "@src/hooks/useOuterClick";

interface ChildrenProps {
  innerRef: RefObject<HTMLDivElement>;
  isDropdownOpened: boolean;
  handleOpenDropdown: () => void;
  handleCloseDropdown: () => void;
  handleToggleDropdown: () => void;
}

interface Props {
  toggleHandler?: () => void;
  children: ({ handleToggleDropdown }: ChildrenProps) => JSX.Element;
}

function Dropdown({ toggleHandler, children }: Props) {
  const {
    isDropdownOpened,
    handleOpenDropdown,
    handleCloseDropdown,
    handleToggleDropdown,
  } = useDropdown();

  const [innerRef] = useOuterClick({
    callback: handleCloseDropdown,
    requiredInnerRefCount: 1,
  });

  useEffect(() => {
    toggleHandler && toggleHandler();
  }, [isDropdownOpened]);

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
