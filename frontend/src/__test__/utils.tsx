import queryClient from "@src/queryClient";
import { RenderResult, render } from "@testing-library/react";
import { ReactElement } from "react";
import { QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { RecoilRoot } from "recoil";
import { ThemeProvider } from "styled-components";

import { LIGHT_MODE_THEME } from "@src/@styles/theme";

const options = queryClient.getDefaultOptions();
options.queries = { ...options.queries, retry: false };

export const customRender = (element: ReactElement): RenderResult => {
  return render(
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <ThemeProvider theme={LIGHT_MODE_THEME}>{element}</ThemeProvider>
        </MemoryRouter>
      </QueryClientProvider>
    </RecoilRoot>
  );
};
