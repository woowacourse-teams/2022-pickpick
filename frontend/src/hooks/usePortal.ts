import { ResponseSubscribedChannels } from "@src/@types/shared";
import { useEffect, useState } from "react";
import {
  QueryObserverResult,
  RefetchOptions,
  RefetchQueryFilters,
} from "react-query";

type Refetch = <TPageData>(
  options?: (RefetchOptions & RefetchQueryFilters<TPageData>) | undefined
) => Promise<QueryObserverResult<ResponseSubscribedChannels, unknown>>;

interface ReturnType {
  isPortalOpened: boolean;
  handleOpenPortal: () => void;
  handleClosePortal: () => void;
  handleTogglePortal: () => void;
}

function usePortal(refetch?: Refetch): ReturnType {
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

  useEffect(() => {
    if (isPortalOpened) {
      document.body.style.overflowY = "hidden";
      refetch && refetch();

      return;
    }
    document.body.style.overflowY = "auto";
  }, [isPortalOpened]);

  return {
    isPortalOpened,
    handleOpenPortal,
    handleClosePortal,
    handleTogglePortal,
  };
}

export default usePortal;
