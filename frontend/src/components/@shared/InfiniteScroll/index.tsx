import useIntersectionObserver from "@src/components/@shared/InfiniteScroll/@hooks/useIntersectionObserver";

import { StrictPropsWithChildren } from "@src/@types/utils";

export interface Props {
  callback: VoidFunction;
  threshold: number;
  endPoint: boolean;
}

function InfiniteScroll({
  children,
  ...props
}: StrictPropsWithChildren<Props>) {
  const { targetRef: nextRef } = useIntersectionObserver(props);

  return (
    <>
      {children}
      <div ref={nextRef}></div>
    </>
  );
}

export default InfiniteScroll;
