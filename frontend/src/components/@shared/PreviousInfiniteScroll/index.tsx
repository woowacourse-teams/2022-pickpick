import { Props } from "@src/components/@shared/NextInfiniteScroll";
import useIntersectionObserver from "@src/hooks/useIntersectionObserver";

function PreviousInfiniteScroll({ children, ...props }: Props) {
  const { targetRef: previousRef } = useIntersectionObserver(props);

  return (
    <>
      <div ref={previousRef}></div>
      {children}
    </>
  );
}

export default PreviousInfiniteScroll;
