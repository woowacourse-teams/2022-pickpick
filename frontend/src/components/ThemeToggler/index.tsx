import { THEME_KIND } from "@src/@constants";
import useModeTheme from "@src/hooks/useModeTheme";
import MoonIcon from "@public/assets/icons/MoonIcon.svg";
import SunIcon from "@public/assets/icons/SunIcon.svg";
import * as Styled from "./style";

function ThemeToggler() {
  const { theme, handleToggleTheme } = useModeTheme();
  return (
    <Styled.Container>
      <Styled.CheckBox
        type="checkbox"
        checked={theme === THEME_KIND.DARK}
        onChange={handleToggleTheme}
      />
      <Styled.LeftIconContainer isVisible={theme === THEME_KIND.LIGHT}>
        <MoonIcon />
      </Styled.LeftIconContainer>
      <Styled.RightIconContainer isVisible={theme === THEME_KIND.DARK}>
        <SunIcon />
      </Styled.RightIconContainer>
    </Styled.Container>
  );
}

export default ThemeToggler;
