import { useEffect, useRef } from "react";
import ReactDOM from "react-dom";
import { useRecoilState } from "recoil";

import { snackbarState } from "@src/@atoms";
import { SNACKBAR_STATUS } from "@src/@constants";

import * as Styled from "./style";

const SNACKBAR_TIME = 3000;

function Snackbar() {
  const [{ isOpened, message, status }, setState] =
    useRecoilState(snackbarState);

  const element = document.querySelector("#portal-root");
  const ref = useRef<any>({
    element: null,
    timeout: null,
  });

  useEffect(() => {
    if (!isOpened && ref.current.timeout) {
      clearTimeout(ref.current.timeout);
      return;
    }

    ref.current.timeout = setTimeout(() => {
      setState({
        isOpened: false,
        message: "",
        status: SNACKBAR_STATUS.SUCCESS,
      });
    }, SNACKBAR_TIME);
  }, [isOpened]);

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
