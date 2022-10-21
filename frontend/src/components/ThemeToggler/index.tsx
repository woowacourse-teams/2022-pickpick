import MoonIcon from "@src/components/@svgIcons/MoonIcon";
import SunIcon from "@src/components/@svgIcons/SunIcon";

import useModeTheme from "@src/hooks/useModeTheme";

import { THEME_KIND } from "@src/@constants";
import { SrOnlyDescription } from "@src/@styles/shared";

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
      <SrOnlyDescription aria-live="assertive">
        {theme === THEME_KIND.DARK
          ? "현재 다크 모드입니다. 라이트 모드로 변경하시려면 선택해주세요."
          : "현재 라이트 모드입니다. 다크 모드로 변경하시려면 선택해주세요."}
      </SrOnlyDescription>

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
