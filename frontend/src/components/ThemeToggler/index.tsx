import MoonIcon from "@src/components/@svgIcons/MoonIcon";
import SunIcon from "@src/components/@svgIcons/SunIcon";

import useModeTheme from "@src/hooks/useModeTheme";

import { THEME_KIND } from "@src/@constants";

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
        <MoonIcon width="13" height="13" />
      </Styled.LeftIconContainer>

      <Styled.RightIconContainer isVisible={theme === THEME_KIND.DARK}>
        <SunIcon width="13" height="13" />
      </Styled.RightIconContainer>
    </Styled.Container>
  );
}

export default ThemeToggler;
