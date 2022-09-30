import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const ButtonText = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.TEXT.WHITE};
  `}
`;
