import { useRoutes } from "react-router-dom";
import { ThemeProvider } from "styled-components";
import { DARK_MODE_THEME, LIGHT_MODE_THEME } from "@src/@styles/theme";
import GlobalStyle from "@src/@styles/GlobalStyle";
import Snackbar from "./components/Snackbar";
import useApiError from "./hooks/useApiError";
import routes from "./Routes";
import { QueryClientProvider } from "react-query";
import { useEffect } from "react";
import queryClient from "./queryClient";
import { ReactQueryDevtools } from "react-query/devtools";
import useModeTheme from "@src/hooks/useModeTheme";
import { THEME_KIND } from "./@constants";

function App() {
  const { handleError } = useApiError();
  const { theme } = useModeTheme();
  const element = useRoutes(routes);

  useEffect(() => {
    queryClient.setDefaultOptions({
      queries: {
        refetchOnWindowFocus: false,
        onError: handleError,
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
