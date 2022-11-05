import { cleanup, render, screen, waitFor } from "@testing-library/react";
import React from "react";
import { MemoryRouter } from "react-router-dom";
import { RecoilRoot } from "recoil";
import { ThemeProvider, useTheme } from "styled-components";
import { DefaultTheme } from "styled-components";

import Button from "@src/components/@shared/Button";

import GlobalStyle from "@src/@styles/GlobalStyle";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
import { Theme } from "@src/@types/shared";

import { COLORS } from "../@styles/colors";
import { FONT_SIZE } from "../@styles/fontSize";
import Home from "../pages/Home";
import { customRender } from "./utils";

// 추측
// moduleNameMapper 가 잘못되어있는듯

describe("일반 피드 페이지 테스트", () => {
  beforeEach(() => {
    customRender(<Home />);
  });
  it("should", async () => {
    // render(
    //   <RecoilRoot>
    //     <MemoryRouter>
    //       <ThemeProvider theme={LIGHT_MODE_THEME}>
    //         <GlobalStyle />
    //         <Home />
    //       </ThemeProvider>
    //     </MemoryRouter>
    //   </RecoilRoot>
    // );
    await waitFor(() => expect(screen.getByText("hello")).toBeInTheDocument());
  });
});
