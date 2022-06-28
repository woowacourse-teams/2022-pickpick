import * as Styled from "./style";

interface Props {
  date: string;
}

function DropdownMenu({ date }: Props) {
  return (
    <Styled.Container>
      <Styled.Option>
        <Styled.Button type="button">{date}</Styled.Button>
      </Styled.Option>
      <Styled.Option>
        <Styled.Button type="button">지난주</Styled.Button>
      </Styled.Option>
      <Styled.Option>
        <Styled.Button type="button">지난달</Styled.Button>
      </Styled.Option>
      <Styled.Option>
        <Styled.Button type="button">첫시작</Styled.Button>
      </Styled.Option>
      <hr />
      <Styled.Option>
        <Styled.Button type="button">특정 날짜로 이동</Styled.Button>
      </Styled.Option>
    </Styled.Container>
  );
}

export default DropdownMenu;
