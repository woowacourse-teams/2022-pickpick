import { useState } from "react";

interface ReturnType {
  isDropdownOpened: boolean;
  handleOpenDropdown: VoidFunction;
  handleCloseDropdown: VoidFunction;
  handleToggleDropdown: VoidFunction;
}

function useDropdown(): ReturnType {
  const [isDropdownOpened, setIsDropdownOpened] = useState(false);

  const handleOpenDropdown = () => {
    setIsDropdownOpened(true);
  };

  const handleCloseDropdown = () => {
    setIsDropdownOpened(false);
  };

  const handleToggleDropdown = () => {
    setIsDropdownOpened((prev) => !prev);
  };

  return {
    isDropdownOpened,
    handleOpenDropdown,
    handleCloseDropdown,
    handleToggleDropdown,
  };
}

export default useDropdown;
