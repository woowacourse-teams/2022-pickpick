import { useEffect, useRef } from "react";

interface ReturnType {
  isRenderDate: (postedDate: string) => boolean;
}

function useMessageDate(): ReturnType {
  const dateArrayRef = useRef<string[]>([]);

  const isRenderDate = (postedDate: string) => {
    if (dateArrayRef.current.includes(postedDate)) return false;

    dateArrayRef.current.push(postedDate);
    return true;
  };

  useEffect(() => {
    dateArrayRef.current = [];
  });

  return {
    isRenderDate,
  };
}

export default useMessageDate;
