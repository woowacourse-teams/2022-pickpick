import { useEffect, useRef } from "react";
import ReactDOM from "react-dom";
import { useRecoilState } from "recoil";

import { snackbarState } from "@src/@atoms";
import { SNACKBAR_STATUS } from "@src/@constants";

import * as Styled from "./style";

const SNACKBAR_TIME = 3000;

type TimerRef = {
  timeout: NodeJS.Timeout | null;
};

function Snackbar() {
  const [{ isOpened, message, status }, setState] =
    useRecoilState(snackbarState);

  const element = document.querySelector("#portal-root");
  const snackbarRef = useRef<HTMLDivElement>(null); // 애니메이션 제어 시 사용한다.
  const timerRef = useRef<TimerRef>({
    timeout: null,
  });

  useEffect(() => {
    if (!isOpened && timerRef.current.timeout) {
      clearTimeout(timerRef.current.timeout);
      return;
    }

    timerRef.current.timeout = setTimeout(() => {
      setState({
        isOpened: false,
        message: "",
        status: SNACKBAR_STATUS.SUCCESS,
      });
    }, SNACKBAR_TIME);
  }, [isOpened]);

  return isOpened && element
    ? ReactDOM.createPortal(
        <Styled.Container status={status} ref={snackbarRef}>
          {message}
        </Styled.Container>,
        element
      )
    : null;
}

export default Snackbar;
