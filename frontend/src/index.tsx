import App from "@src/App";
import { initMSW } from "@src/mocks";
import UnexpectedError from "@src/pages/UnexpectedError";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { RecoilRoot } from "recoil";

import ErrorBoundary from "@src/components/ErrorBoundary";

initMSW();

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <ErrorBoundary fallback={<UnexpectedError />}>
      <RecoilRoot>
        <App />
      </RecoilRoot>
    </ErrorBoundary>
  </BrowserRouter>
);
