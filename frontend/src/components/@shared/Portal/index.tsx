import ReactDOM from "react-dom";

import { StrictPropsWithChildren } from "@src/@types/utils";

interface Props {
  isOpened: boolean;
}

function Portal({
  isOpened = false,
  children,
}: StrictPropsWithChildren<Props>) {
  const root = document.getElementById("portal-root");
  return isOpened && root ? ReactDOM.createPortal(children, root) : null;
}

export default Portal;
