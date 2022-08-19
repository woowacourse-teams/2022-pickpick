import { useEffect, useRef, WheelEventHandler, TouchEventHandler } from "react";

interface Props {
  isCallable?: boolean;
  callback: () => unknown;
  scrollOffset: number;
  touchDistanceCriterion: number;
  wheelDistanceCriterion: number;
}

interface ReturnType {
  onWheel: WheelEventHandler<HTMLDivElement>;
  onTouchStart: TouchEventHandler<HTMLDivElement>;
  onTouchEnd: TouchEventHandler<HTMLDivElement>;
}

function useTopScreenEventHandler({
  isCallable,
  callback,
  scrollOffset,
  touchDistanceCriterion,
  wheelDistanceCriterion,
}: Props): ReturnType {
  const wheelPosition = useRef({ default: 0, move: 0, scroll: 0 });
  const touchPosition = useRef({ start: 0, end: 0 });
  const scrollPosition = useRef({ default: 0 });
  const isCalled = useRef(false);

  const onWheel = (event: React.WheelEvent<HTMLDivElement>) => {
    wheelPosition.current.move = event.deltaY;

    if (
      wheelPosition.current.move < wheelDistanceCriterion &&
      scrollPosition.current.default < scrollOffset &&
      !isCalled.current
    ) {
      isCallable && callback();
      isCalled.current = true;

      return;
    }

    if (
      wheelPosition.current.move > wheelDistanceCriterion &&
      isCalled.current
    ) {
      isCalled.current = false;

      return;
    }
  };

  const onTouchStart = (event: React.TouchEvent<HTMLDivElement>) => {
    touchPosition.current.start = event.changedTouches[0].clientY;
  };

  const onTouchEnd = (event: React.TouchEvent<HTMLDivElement>) => {
    touchPosition.current.end = event.changedTouches[0].clientY;

    const { start: touchStart, end: touchEnd } = touchPosition.current;

    if (
      touchStart - touchEnd < touchDistanceCriterion &&
      scrollPosition.current.default < scrollOffset
    ) {
      isCallable && callback();

      return;
    }
  };

  useEffect(() => {
    const handleScrollEvent = () => {
      scrollPosition.current.default = window.scrollY;
    };

    window.addEventListener("scroll", handleScrollEvent);

    return () => window.removeEventListener("scroll", handleScrollEvent);
  }, []);

  return {
    onWheel,
    onTouchStart,
    onTouchEnd,
  };
}

export default useTopScreenEventHandler;
