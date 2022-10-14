import { useEffect, useState } from "react";
import {
  QueryObserverResult,
  RefetchOptions,
  RefetchQueryFilters,
} from "react-query";

import { ResponseSubscribedChannels } from "@src/@types/api";

type Refetch = <TPageData>(
  options?: (RefetchOptions & RefetchQueryFilters<TPageData>) | undefined
) => Promise<QueryObserverResult<ResponseSubscribedChannels, unknown>>;

interface UseModalResult {
  isModalOpened: boolean;
  handleOpenModal: VoidFunction;
  handleCloseModal: VoidFunction;
  handleToggleModal: VoidFunction;
}

function useModal(refetch?: Refetch): UseModalResult {
  const [isModalOpened, setIsModalOpened] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpened(true);
  };

  const handleCloseModal = () => {
    setIsModalOpened(false);
  };

  const handleToggleModal = () => {
    setIsModalOpened((prev) => !prev);
  };

  useEffect(() => {
    if (isModalOpened) {
      document.body.style.overflowY = "hidden";
      refetch && refetch();

      return;
    }

    document.body.style.overflowY = "auto";

    return () => {
      document.body.style.overflowY = "auto";
    };
  }, [isModalOpened]);

  return {
    isModalOpened,
    handleOpenModal,
    handleCloseModal,
    handleToggleModal,
  };
}

export default useModal;
