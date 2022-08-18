import { useEffect, useRef } from "react";

type CallbackType = () => void;

function useOuterClick(callback: CallbackType) {
  const callbackRef = useRef<CallbackType>();
  const innerRef = useRef<HTMLDivElement>(null);

  callbackRef.current = callback;

  useEffect(() => {
    const handleClick = (event: MouseEvent) => {
      if (
        innerRef.current &&
        callbackRef.current &&
        !innerRef.current.contains(event.target as HTMLDivElement)
      ) {
        callbackRef.current();
      }
    };

    document.addEventListener("click", handleClick);

    return () => document.removeEventListener("click", handleClick);
  }, [callbackRef, innerRef]);

  return { innerRef };
}

export default useOuterClick;
