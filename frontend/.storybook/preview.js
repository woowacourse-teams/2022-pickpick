import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
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
    <>
      <ThemeProvider theme={LIGHT_MODE_THEME}>
        <GlobalStyle />
        <Story />
      </ThemeProvider>
    </>
  ),
];
