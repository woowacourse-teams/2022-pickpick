import { useEffect, useState } from "react";
import useWebStorage from "@src/hooks/useWebStorage";
type Theme = "LIGHT" | "DARK";

function useTheme() {
  const [getStoredTheme, setStoredTheme] = useWebStorage<Theme>({
    key: "theme",
    kind: "LOCAL",
  });
  const [theme, setTheme] = useState<Theme>(getStoredTheme() ?? "LIGHT");

  const handleToggleTheme = () => {
    const nextTheme = theme === "LIGHT" ? "DARK" : "LIGHT";
    handleChangeTheme(nextTheme);
  };

  const handleChangeTheme = (theme: Theme) => {
    setStoredTheme(theme);
    setTheme(theme);
  };

  useEffect(() => {
    if (window.matchMedia("(prefers-color-scheme: dark)").matches)
      handleChangeTheme("DARK");
  }, []);

  return { theme, handleToggleTheme };
}

export default useTheme;
