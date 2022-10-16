import { useEffect, useState } from "react";

interface Props {
  openModalEffectCallback?: (...args: any[]) => unknown;
  closeModalEffectCallback?: (...args: any[]) => unknown;
}

interface UseModalResult {
  isModalOpened: boolean;
  handleOpenModal: VoidFunction;
  handleCloseModal: VoidFunction;
  handleToggleModal: VoidFunction;
}

function useModal({
  openModalEffectCallback,
  closeModalEffectCallback,
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
      openModalEffectCallback && openModalEffectCallback();

      return;
    }

    document.body.style.overflowY = "auto";
    closeModalEffectCallback && closeModalEffectCallback();

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
