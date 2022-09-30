import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const ButtonText = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.TEXT.WHITE};
  `}
`;
