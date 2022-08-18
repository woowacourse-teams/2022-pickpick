import { PropsWithChildren } from "react";
import useIntersectionObserver from "@src/hooks/useIntersectionObserver";

export interface Props {
  callback: () => void;
  threshold: number;
  endPoint: boolean;
}

function InfiniteScroll({ children, ...props }: PropsWithChildren<Props>) {
  const { targetRef: nextRef } = useIntersectionObserver(props);

  return (
    <>
      {children}
      <div ref={nextRef}></div>
    </>
  );
}

export default InfiniteScroll;
