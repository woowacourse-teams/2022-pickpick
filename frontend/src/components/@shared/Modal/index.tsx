import Dimmer from "@src/components/@shared/Dimmer";
import Portal from "@src/components/@shared/Portal";

import { StrictPropsWithChildren } from "@src/@types/utils";

interface Props {
  isOpened: boolean;
  handleCloseModal: VoidFunction;
}

function Modal({
  isOpened,
  handleCloseModal,
  children,
}: StrictPropsWithChildren<Props>) {
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
