import { useRef } from "react";

const useMessageDate = () => {
  const dateArrayRef = useRef<string[]>([]);

  const initializeDateArray = () => {
    dateArrayRef.current = [];
  };

  const isRenderDate = (postedDate: string) => {
    if (dateArrayRef.current.includes(postedDate)) return false;

    dateArrayRef.current.push(postedDate);
    return true;
  };

  return {
    initializeDateArray,
    isRenderDate,
  };
};

export default useMessageDate;
