import { Link } from "react-router-dom";

import * as Styled from "@src/components/DateDropdown/DateDropdownMenu/style";

import { DATE } from "@src/@constants/date";
import { ISOConverter } from "@src/@utils";

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
