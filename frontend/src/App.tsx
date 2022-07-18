import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
import GlobalStyle from "./@styles/GlobalStyle";
import { QueryClientProvider, QueryClient } from "react-query";
import { useRoutes } from "react-router-dom";
import routes from "./Routes";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  const element = useRoutes(routes);
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={LIGHT_MODE_THEME}>
        <GlobalStyle />
        {element}
      </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;
