import { useEffect } from "react";
import useWebStorage, { STORAGE_KIND } from "@src/hooks/useWebStorage";
import { useRecoilState } from "recoil";
import { themeState } from "@src/@atoms";

export const THEME_KIND = {
  LIGHT: "LIGHT",
  DARK: "DARK",
} as const;

type ThemeKind = keyof typeof THEME_KIND;

interface ReturnType {
  theme: ThemeKind;
  handleToggleTheme: () => void;
}

function useModeTheme(): ReturnType {
  const [theme, setTheme] = useRecoilState(themeState);

  const { getItem: getStoredTheme, setItem: setStoredTheme } =
    useWebStorage<ThemeKind>({
      key: "theme",
      kind: STORAGE_KIND.LOCAL,
    });

  const handleToggleTheme = () => {
    const nextTheme =
      theme === THEME_KIND.LIGHT ? THEME_KIND.DARK : THEME_KIND.LIGHT;
    handleChangeTheme(nextTheme);
  };

  const handleChangeTheme = (theme: ThemeKind) => {
    setStoredTheme(theme);
    setTheme(theme);
  };

  useEffect(() => {
    const storedTheme = getStoredTheme();
    if (
      window.matchMedia("(prefers-color-scheme: dark)").matches &&
      theme !== THEME_KIND.LIGHT
    ) {
      handleChangeTheme(THEME_KIND.DARK);
      return;
    }
    handleChangeTheme(storedTheme);
  }, []);

  return { theme, handleToggleTheme };
}

export default useModeTheme;
