import { useLocation } from "react-router-dom";
import useWebStorage, { STORAGE_KIND } from "@src/hooks/useWebStorage";
import { useEffect } from "react";

function useRecentFeedPath() {
  const { key, pathname } = useLocation();
  const { setItem: setRecentFeedPath, getItem: getRecentFeedPath } =
    useWebStorage<string>({
      key: "default-feed-path",
      kind: STORAGE_KIND.SESSION,
    });

  useEffect(() => {
    setRecentFeedPath(pathname);
  }, [key, pathname]);

  return { setRecentFeedPath, getRecentFeedPath };
}

export default useRecentFeedPath;
