import routes from "@src/Routes";
import queryClient from "@src/queryClient";
import { useEffect } from "react";
import { QueryClientProvider } from "react-query";
import { ReactQueryDevtools } from "react-query/devtools";
import { useRoutes } from "react-router-dom";
import { ThemeProvider } from "styled-components";

import Snackbar from "@src/components/Snackbar";

import useApiError from "@src/hooks/useApiError";
import useModeTheme from "@src/hooks/useModeTheme";

import { THEME_KIND } from "@src/@constants";
import GlobalStyle from "@src/@styles/GlobalStyle";
import { DARK_MODE_THEME, LIGHT_MODE_THEME } from "@src/@styles/theme";

function App() {
  const { handleError } = useApiError();
  const { theme } = useModeTheme();
  const element = useRoutes(routes);

  useEffect(() => {
    queryClient.setDefaultOptions({
      queries: {
        refetchOnWindowFocus: false,
        onError: handleError,
        retry: 0,
      },
      mutations: {
        onError: handleError,
      },
    });
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider
        theme={theme === THEME_KIND.LIGHT ? LIGHT_MODE_THEME : DARK_MODE_THEME}
      >
        <GlobalStyle />
        {element}
        <Snackbar />
      </ThemeProvider>
      <ReactQueryDevtools initialIsOpen={false} position="bottom-left" />
    </QueryClientProvider>
  );
}

export default App;
