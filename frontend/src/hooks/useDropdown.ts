import { useState } from "react";

interface ReturnType {
  isDropdownOpened: boolean;
  handleOpenDropdown: () => void;
  handleCloseDropdown: () => void;
  handleToggleDropdown: () => void;
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
