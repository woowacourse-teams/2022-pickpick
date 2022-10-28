import { ReactNode, RefObject, useEffect } from "react";

import useDropdown from "@src/components/@shared/Dropdown/@hooks/useDropdown";

import useOuterClick from "@src/hooks/@shared/useOuterClick";

import { SrOnlyDescription } from "@src/@styles/shared";
import { PropsWithFunctionChildren } from "@src/@types/utils";

interface ChildrenProps {
  innerRef: RefObject<HTMLDivElement>;
  isDropdownOpened: boolean;
  handleOpenDropdown: VoidFunction;
  handleCloseDropdown: VoidFunction;
  handleToggleDropdown: VoidFunction;
}

interface Props {
  toggleHandler?: VoidFunction;
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
      <SrOnlyDescription id="description">
        {isDropdownOpened
          ? "드랍다운 메뉴가 열려있습니다. 드랍다운 메뉴를 닫으려면 클릭해주세요."
          : "드랍다운 메뉴가 닫혀있습니다. 드랍다운 메뉴를 열려면 클릭해주세요."}
      </SrOnlyDescription>

      <SrOnlyDescription aria-live="assertive">
        {isDropdownOpened
          ? "드랍다운 메뉴가 열렸습니다."
          : "드랍다운 메뉴가 닫혔습니다."}
      </SrOnlyDescription>

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
