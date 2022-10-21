import queryClient from "@src/queryClient";
import { initialize, mswDecorator } from "msw-storybook-addon";
import { QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { RecoilRoot } from "recoil";
import { ThemeProvider } from "styled-components";

import useModeTheme from "@src/hooks/useModeTheme";

import { THEME_KIND } from "@src/@constants";
import GlobalStyle from "@src/@styles/GlobalStyle";
import { DARK_MODE_THEME, LIGHT_MODE_THEME } from "@src/@styles/theme";

initialize({
  onUnhandledRequest: "bypass",
});

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

const App = ({ children }) => {
  const { theme } = useModeTheme();
  return (
    <ThemeProvider
      theme={theme === THEME_KIND.LIGHT ? LIGHT_MODE_THEME : DARK_MODE_THEME}
    >
      <GlobalStyle />
      {children}
      <div id="portal-root"></div>
    </ThemeProvider>
  );
};

export const decorators = [
  mswDecorator,
  (Story) => (
    <MemoryRouter>
      <RecoilRoot>
        <QueryClientProvider client={queryClient}>
          <App>
            <Story />
          </App>
        </QueryClientProvider>
      </RecoilRoot>
    </MemoryRouter>
  ),
];

if (typeof global.process === "undefined") {
  const { worker } = require("../src/mocks/browser");
  worker.start();
}
