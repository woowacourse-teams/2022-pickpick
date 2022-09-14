import { useEffect } from "react";
import useWebStorage, { STORAGE_KIND } from "@src/hooks/useWebStorage";
import { useRecoilState } from "recoil";
import { themeState } from "@src/@atoms";
import { ThemeKind } from "@src/@types/shared";
import { THEME_KIND } from "@src/@constants";
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
    if (storedTheme === THEME_KIND.DARK || storedTheme === THEME_KIND.LIGHT) {
      handleChangeTheme(storedTheme);
      return;
    }

    if (window.matchMedia("(prefers-color-scheme: light)").matches) {
      handleChangeTheme(THEME_KIND.LIGHT);
      return;
    }
    handleChangeTheme(THEME_KIND.DARK);
  }, []);

  return { theme, handleToggleTheme };
}

export default useModeTheme;
