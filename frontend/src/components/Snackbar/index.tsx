import { useEffect, useRef } from "react";
import ReactDOM from "react-dom";
import { useRecoilState } from "recoil";

import { snackbarState } from "@src/@atoms";
import { SNACKBAR_STATUS } from "@src/@constants";

import * as Styled from "./style";

function Snackbar() {
  const [{ isOpened, message, status }, setState] =
    useRecoilState(snackbarState);

  const element = document.querySelector("#portal-root");
  const ref = useRef<any>({
    element: null,
    timeout: null,
  });

  useEffect(() => {
    if (isOpened) {
      if (ref.current.timeout) {
        const [showAnimation] = ref.current.element.getAnimations();

        showAnimation?.cancel();
        showAnimation?.play();
        clearTimeout(ref.current.timeout);
      }

      ref.current.timeout = setTimeout(() => {
        setState({
          isOpened: false,
          message: "",
          status: SNACKBAR_STATUS.SUCCESS,
        });
      }, 3000);
    }
  });

  return isOpened && element
    ? ReactDOM.createPortal(
        <Styled.Container
          status={status}
          ref={(el) => (ref.current.element = el)}
        >
          {message}
        </Styled.Container>,
        element
      )
    : null;
}

export default Snackbar;
