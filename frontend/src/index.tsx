import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { ThemeProvider } from "styled-components";
import { LIGHT_MODE_THEME } from "./@styles/theme";

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <ThemeProvider theme={LIGHT_MODE_THEME}>
      <Routes>
        <Route path="/" element={<App />} />
      </Routes>
    </ThemeProvider>
  </BrowserRouter>
);
