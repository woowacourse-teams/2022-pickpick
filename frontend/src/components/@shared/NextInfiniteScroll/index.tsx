import useIntersectionObserver from "@src/hooks/useIntersectionObserver";

export interface Props {
  children: JSX.Element;
  callback: () => void;
  threshold: number;
  endPoint: boolean;
}

function NextInfiniteScroll({ children, ...props }: Props) {
  const { targetRef: nextRef } = useIntersectionObserver(props);

  return (
    <>
      {children}
      <div ref={nextRef}></div>
    </>
  );
}

export default NextInfiniteScroll;
