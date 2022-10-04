import { useEffect } from "react";

interface Props {
  dependencies?: unknown[];
}

function useScrollToTop({ dependencies = [] }: Props = {}): void {
  useEffect(() => {
    window.scrollTo({
      top: 0,
    });
  }, dependencies);
}

export default useScrollToTop;
