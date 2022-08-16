import { THEME_KIND } from "@src/@constants";
import useModeTheme from "@src/hooks/useModeTheme";
import * as Styled from "./style";

function ThemeToggler() {
  const { theme, handleToggleTheme } = useModeTheme();
  return (
    <Styled.Container
      type="checkbox"
      checked={theme === THEME_KIND.DARK}
      onChange={handleToggleTheme}
    />
  );
}

export default ThemeToggler;
