import { useEffect } from "react";
import { useLocation } from "react-router-dom";

import useWebStorage, { STORAGE_KIND } from "@src/hooks/@shared/useWebStorage";

interface UseRecentFeedPathResult {
  setRecentFeedPath: (item: string) => void;
  getRecentFeedPath: () => string;
}

function useRecentFeedPath(): UseRecentFeedPathResult {
  const { key, pathname } = useLocation();
  const { setItem: setRecentFeedPath, getItem: getRecentFeedPath } =
    useWebStorage<string>({
      key: "recent-feed-path",
      kind: STORAGE_KIND.SESSION,
    });

  useEffect(() => {
    const name = pathname.split("/")[1];

    if (name !== "feed") return;

    setRecentFeedPath(pathname);
  }, [pathname, key]);

  return { setRecentFeedPath, getRecentFeedPath };
}

export default useRecentFeedPath;
