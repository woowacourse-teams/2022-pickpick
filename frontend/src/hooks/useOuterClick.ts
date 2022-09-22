import { useEffect, useRef } from "react";

type CallbackType = () => void;

interface Props {
  callback: CallbackType;
  requiredInnerRefCount: number;
}

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
