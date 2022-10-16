import { useEffect, useRef, useState } from "react";
import { useRecoilState } from "recoil";

import Portal from "@src/components/@shared/Portal";

import { snackbarState } from "@src/@atoms";
import { SNACKBAR_STATUS } from "@src/@constants";

import * as Styled from "./style";

const DURATION = 3500;
const DURATION_OFFSET = 500;

type TimerRef = {
  durationTimer: NodeJS.Timeout | null;
  durationWithOffsetTimer: NodeJS.Timeout | null;
};

function Snackbar() {
  const [{ isOpened, message, status }, setState] =
    useRecoilState(snackbarState);

  const [isAlive, setIsAlive] = useState(true);

  const snackbarRef = useRef<HTMLDivElement>(null);
  const timerRef = useRef<TimerRef>({
    durationTimer: null,
    durationWithOffsetTimer: null,
  });

  useEffect(() => {
    if (
      !isOpened &&
      timerRef.current.durationTimer &&
      timerRef.current.durationWithOffsetTimer
    ) {
      clearTimeout(timerRef.current.durationTimer);
      clearTimeout(timerRef.current.durationWithOffsetTimer);
      return;
    }

    timerRef.current.durationTimer = setTimeout(() => {
      setState({
        isOpened: false,
        message: "",
        status: SNACKBAR_STATUS.SUCCESS,
      });
      setIsAlive(true);
    }, DURATION);

    timerRef.current.durationWithOffsetTimer = setTimeout(() => {
      setIsAlive(false);
    }, DURATION - DURATION_OFFSET);
  }, [isOpened]);

  return (
    <Portal isOpened={isOpened}>
      <Styled.Container status={status} ref={snackbarRef} isAlive={isAlive}>
        {message}
      </Styled.Container>
    </Portal>
  );
}

export default Snackbar;
