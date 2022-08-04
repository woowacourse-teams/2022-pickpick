import { DATE } from "@src/@constants";
import { ISOConverter } from "@src/@utils";
import { Link } from "react-router-dom";
import * as Styled from "@src/components/DateDropdownMenu/style";

interface Props {
  date: string;
  channelId: string;
}

function DateDropdownOption({ date, channelId }: Props) {
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
}

export default DateDropdownOption;
