import { MemoryRouter } from "react-router-dom";
import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
import { RecoilRoot } from "recoil";
import GlobalStyle from "@src/@styles/GlobalStyle";

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

export const decorators = [
  (Story) => (
    <MemoryRouter>
      <RecoilRoot>
        <ThemeProvider theme={LIGHT_MODE_THEME}>
          <GlobalStyle />
          <Story />
        </ThemeProvider>
      </RecoilRoot>
    </MemoryRouter>
  ),
];
