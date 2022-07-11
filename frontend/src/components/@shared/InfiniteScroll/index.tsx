import { useEffect, useRef } from "react";

interface Props {
  children: JSX.Element;
  callback: () => void;
  threshold: number;
  endPoint: boolean;
}

function InfiniteScroll({ children, callback, threshold, endPoint }: Props) {
  const targetRef = useRef<HTMLDivElement>(null);
  const observer = useRef(
    new IntersectionObserver(onIntersect, {
      threshold,
    })
  ).current;

  function onIntersect([entry]: IntersectionObserverEntry[]) {
    if (entry.isIntersecting) {
      console.log("제발되어라");
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

  return (
    <>
      {children}
      <div ref={targetRef}></div>
    </>
  );
}

export default InfiniteScroll;
