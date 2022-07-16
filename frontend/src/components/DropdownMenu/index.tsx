import * as Styled from "./style";

interface Props {
  date: string;
}

function DropdownMenu({ date }: Props) {
  const renderDateOption = () => {
    if (date === "오늘") {
      return (
        <Styled.Option>
          <Styled.Button type="button">어제</Styled.Button>
        </Styled.Option>
      );
    }

    if (date === "어제") {
      return (
        <Styled.Option>
          <Styled.Button type="button">오늘</Styled.Button>
        </Styled.Option>
      );
    }

    return (
      <>
        <Styled.Option>
          <Styled.Button type="button">오늘</Styled.Button>
        </Styled.Option>
        <Styled.Option>
          <Styled.Button type="button">어제</Styled.Button>
        </Styled.Option>
      </>
    );
  };

  return (
    <Styled.Container>
      {renderDateOption()}
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
