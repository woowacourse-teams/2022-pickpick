import { useEffect, useRef } from "react";

type UseMessageDateResult = (postedDate: string) => boolean;

function useMessageDate(): UseMessageDateResult {
  const dateArrayRef = useRef<string[]>([]);

  const shouldRenderDate = (postedDate: string) => {
    if (dateArrayRef.current.includes(postedDate)) return false;

    dateArrayRef.current.push(postedDate);
    return true;
  };

  useEffect(() => {
    dateArrayRef.current = [];
  });

  return shouldRenderDate;
}

export default useMessageDate;
