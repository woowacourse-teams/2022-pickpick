import { useEffect, useState } from "react";
import useWebStorage, { STORAGE_KIND } from "@src/hooks/useWebStorage";

export const THEME_KIND = {
  LIGHT: "LIGHT",
  DARK: "DARK",
} as const;

type Theme = keyof typeof THEME_KIND;

interface ReturnType {
  theme: Theme;
  handleToggleTheme: () => void;
}

function useTheme(): ReturnType {
  const { getItem: getStoredTheme, setItem: setStoredTheme } =
    useWebStorage<Theme>({
      key: "theme",
      kind: STORAGE_KIND.LOCAL,
    });
  const [theme, setTheme] = useState<Theme>(
    getStoredTheme() ?? THEME_KIND.LIGHT
  );

  const handleToggleTheme = () => {
    const nextTheme =
      theme === THEME_KIND.LIGHT ? THEME_KIND.DARK : THEME_KIND.LIGHT;
    handleChangeTheme(nextTheme);
  };

  const handleChangeTheme = (theme: Theme) => {
    setStoredTheme(theme);
    setTheme(theme);
  };

  useEffect(() => {
    if (window.matchMedia("(prefers-color-scheme: dark)").matches)
      handleChangeTheme(THEME_KIND.DARK);
  }, []);

  return { theme, handleToggleTheme };
}

export default useTheme;
