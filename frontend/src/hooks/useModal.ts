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
  isModalOpened: boolean;
  handleOpenModal: () => void;
  handleCloseModal: () => void;
  handleToggleModal: () => void;
}

function useModal(refetch?: Refetch): ReturnType {
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
  }, [isModalOpened]);

  return {
    isModalOpened,
    handleOpenModal,
    handleCloseModal,
    handleToggleModal,
  };
}

export default useModal;
