import { DATE } from "@src/@constants";
import { ISOConverter } from "@src/@utils";
import { Link } from "react-router-dom";
import * as Styled from "./style";

interface Props {
  date: string;
  channelId: string;
  handleOpenCalendar: () => void;
}

function DropdownMenu({ date, channelId, handleOpenCalendar }: Props) {
  const renderDateOption = () => {
    if (date === DATE.TODAY) {
      return (
        <Styled.Option>
          <Link to={`/feed/${channelId}/${ISOConverter(DATE.YESTERDAY)}`}>
            <Styled.Button type="button">{DATE.YESTERDAY}</Styled.Button>
          </Link>
        </Styled.Option>
      );
    }

    if (date === DATE.YESTERDAY) {
      return (
        <Styled.Option>
          <Link to={`/feed/${channelId}/${ISOConverter(DATE.TODAY)}`}>
            <Styled.Button type="button">{DATE.TODAY}</Styled.Button>
          </Link>
        </Styled.Option>
      );
    }

    return (
      <>
        <Styled.Option>
          <Link to={`/feed/${channelId}/${ISOConverter(DATE.TODAY)}`}>
            <Styled.Button type="button">{DATE.TODAY}</Styled.Button>
          </Link>
        </Styled.Option>
        <Styled.Option>
          <Link to={`/feed/${channelId}/${ISOConverter(DATE.YESTERDAY)}`}>
            <Styled.Button type="button">{DATE.YESTERDAY}</Styled.Button>
          </Link>
        </Styled.Option>
      </>
    );
  };

  return (
    <Styled.Container>
      {renderDateOption()}
      <hr />
      <Styled.Option>
        <Styled.Button type="button" onClick={handleOpenCalendar}>
          특정 날짜로 이동
        </Styled.Button>
      </Styled.Option>
    </Styled.Container>
  );
}

export default DropdownMenu;
