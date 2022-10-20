import queryClient from "@src/queryClient";
import { QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { RecoilRoot } from "recoil";
import { ThemeProvider } from "styled-components";

import GlobalStyle from "@src/@styles/GlobalStyle";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";

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
        <QueryClientProvider client={queryClient}>
          <ThemeProvider theme={LIGHT_MODE_THEME}>
            <GlobalStyle />
            <Story />

            <div id="portal-root"></div>
          </ThemeProvider>
        </QueryClientProvider>
      </RecoilRoot>
    </MemoryRouter>
  ),
];
