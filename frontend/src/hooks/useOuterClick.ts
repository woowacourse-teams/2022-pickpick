import { useEffect, useRef } from "react";

type CallbackType = () => void;

interface Props {
  callback: CallbackType;
  requiredInnerRefCount: number;
}
/**
 * 함수 반환 객체
 * innerRef - innerRefArray 의 첫번째 값 반환
 * innerRefArray
 */

function useOuterClick({ callback, requiredInnerRefCount = 1 }: Props) {
  const callbackRef = useRef<CallbackType>();
  const innerRefArray = [
    ...Array(requiredInnerRefCount <= 0 ? 1 : requiredInnerRefCount),
  ].map(() => useRef<HTMLDivElement>(null));
  callbackRef.current = callback;

  useEffect(() => {
    const handleClick = (event: MouseEvent) => {
      if (
        callbackRef.current &&
        !innerRefArray.some(
          (ref) =>
            ref.current && ref.current.contains(event.target as HTMLDivElement)
        )
      ) {
        callbackRef.current();
      }
    };

    document.addEventListener("click", handleClick);

    return () => document.removeEventListener("click", handleClick);
  }, []);

  return { innerRef: innerRefArray[0], innerRefArray };
}

export default useOuterClick;
