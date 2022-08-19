import { PropsWithChildren } from "react";
import ReactDOM from "react-dom";

interface Props {
  isOpened: boolean;
}

function Portal({ isOpened = false, children }: PropsWithChildren<Props>) {
  const root = document.getElementById("portal-root");
  return isOpened && root ? ReactDOM.createPortal(children, root) : null;
}

export default Portal;
