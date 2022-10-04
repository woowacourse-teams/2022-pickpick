import { PropsWithChildren } from "react";

import Dimmer from "@src/components/@shared/Dimmer";
import Portal from "@src/components/@shared/Portal";

interface Props {
  isOpened: boolean;
  handleCloseModal: () => void;
}

function Modal({
  isOpened,
  handleCloseModal,
  children,
}: PropsWithChildren<Props>) {
  return (
    <Portal isOpened={isOpened}>
      <>
        <Dimmer hasBackgroundColor={true} onClick={handleCloseModal} />
        {children}
      </>
    </Portal>
  );
}

export default Modal;
