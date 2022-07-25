import ReactDOM from "react-dom";

interface Props {
  children: JSX.Element;
  isOpened: boolean;
}

function Portal({ isOpened = false, children }: Props) {
  const root = document.getElementById("portal-root");
  return isOpened && root ? ReactDOM.createPortal(children, root) : null;
}

export default Portal;
