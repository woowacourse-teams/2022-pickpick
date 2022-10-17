import { useEffect, useState } from "react";

interface Props {
  openModalCallback?: (...args: any[]) => unknown;
  closeModalCallback?: (...args: any[]) => unknown;
}

interface UseModalResult {
  isModalOpened: boolean;
  handleOpenModal: VoidFunction;
  handleCloseModal: VoidFunction;
  handleToggleModal: VoidFunction;
}

function useModal({
  openModalCallback,
  closeModalCallback,
}: Props = {}): UseModalResult {
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
      openModalCallback && openModalCallback();

      return;
    }

    document.body.style.overflowY = "auto";
    closeModalCallback && closeModalCallback();

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
