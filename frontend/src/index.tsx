import ReactDOM from "react-dom/client";
import App from "./App";
import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";
import GlobalStyle from "./@styles/GlobalStyle";
import { BrowserRouter } from "react-router-dom";
import ErrorBoundary from "./components/ErrorBoundary";
import UnexpectedError from "./pages/UnexpectedError";
import { RecoilRoot } from "recoil";

if (process.env.NODE_ENV === "development") {
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  const { worker } = require("./mocks/browser");
  worker.start();
}

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <RecoilRoot>
      <ThemeProvider theme={LIGHT_MODE_THEME}>
        <GlobalStyle />
        <ErrorBoundary fallback={<UnexpectedError />}>
          <App />
        </ErrorBoundary>
      </ThemeProvider>
    </RecoilRoot>
  </BrowserRouter>
);
