import { useEffect, useRef } from "react";
import { Props as InfiniteScrollProps } from "@src/components/@shared/NextInfiniteScroll";

type Props = Omit<InfiniteScrollProps, "children">;

function useIntersectionObserver({ callback, threshold, endPoint }: Props) {
  const targetRef = useRef<HTMLDivElement>(null);
  const observer = useRef(
    new IntersectionObserver(onIntersect, {
      threshold,
    })
  ).current;

  function onIntersect([entry]: IntersectionObserverEntry[]) {
    if (entry.isIntersecting) {
      callback();
    }
  }

  useEffect(() => {
    if (endPoint) {
      return observer && observer.disconnect();
    }

    if (targetRef && targetRef.current) {
      observer.observe(targetRef.current);
    }

    return () => observer && observer.disconnect();
  }, [callback, endPoint, observer, targetRef]);

  return { targetRef };
}

export default useIntersectionObserver;
