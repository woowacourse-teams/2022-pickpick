import { kindTable } from "../WrapperButton/style";
import { NavLink } from "react-router-dom";
import styled from "styled-components";
import { Kind } from "../WrapperButton";

export const Container = styled(NavLink)`
  background-color: inherit;
  cursor: pointer;

  ${({ kind }: { kind?: Kind }) => kind && kindTable[kind]}
`;
