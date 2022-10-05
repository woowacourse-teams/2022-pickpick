import { ReactNode, RefObject, useEffect } from "react";

import useDropdown from "@src/components/@shared/Dropdown/@hooks/useDropdown";

import useOuterClick from "@src/hooks/@shared/useOuterClick";

import { PropsWithFunctionChildren } from "@src/@types/utils";

interface ChildrenProps {
  innerRef: RefObject<HTMLDivElement>;
  isDropdownOpened: boolean;
  handleOpenDropdown: () => void;
  handleCloseDropdown: () => void;
  handleToggleDropdown: () => void;
}

interface Props {
  toggleHandler?: () => void;
  children: ({ handleToggleDropdown }: ChildrenProps) => ReactNode;
}

function Dropdown({
  toggleHandler,
  children,
}: PropsWithFunctionChildren<Props>) {
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
