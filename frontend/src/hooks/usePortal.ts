import { useState } from "react";

interface ReturnType {
  isPortalOpened: boolean;
  handleOpenPortal: () => void;
  handleClosePortal: () => void;
  handleTogglePortal: () => void;
}

function usePortal(): ReturnType {
  const [isPortalOpened, setIsPortalOpened] = useState(false);

  const handleOpenPortal = () => {
    setIsPortalOpened(true);
  };

  const handleClosePortal = () => {
    setIsPortalOpened(false);
  };

  const handleTogglePortal = () => {
    setIsPortalOpened((prev) => !prev);
  };

  return {
    isPortalOpened,
    handleOpenPortal,
    handleClosePortal,
    handleTogglePortal,
  };
}

export default usePortal;
