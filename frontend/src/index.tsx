import ReactDOM from "react-dom/client";
import App from "./App";
import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
import GlobalStyle from "./@styles/GlobalStyle";
import { QueryClientProvider, QueryClient } from "react-query";
import { BrowserRouter } from "react-router-dom";
if (process.env.NODE_ENV === "development") {
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  const { worker } = require("./mocks/browser");
  worker.start();
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={LIGHT_MODE_THEME}>
        <GlobalStyle />
        <App />
      </ThemeProvider>
    </QueryClientProvider>
  </BrowserRouter>
);
