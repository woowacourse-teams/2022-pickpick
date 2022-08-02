import { DATE } from "@src/@constants";
import { ISOConverter } from "@src/@utils";
import usePortal from "@src/hooks/usePortal";
import { useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import Dimmer from "../@shared/Dimmer";
import Portal from "../@shared/Portal";
import Calendar from "../Calendar";
import * as Styled from "./style";

interface Props {
  date: string;
}

function DropdownMenu({ date }: Props) {
  const {
    isPortalOpened: isCalenderOpened,
    handleOpenPortal: handleOpenCalendar,
    handleClosePortal: handleCloseCalendar,
  } = usePortal();

  const { channelId } = useParams();

  useEffect(() => {
    if (isCalenderOpened) {
      document.body.style.overflowY = "hidden";
      return;
    }
    document.body.style.overflowY = "auto";
  }, [isCalenderOpened]);

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
      <Portal isOpened={isCalenderOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseCalendar} />
          <Calendar />
        </>
      </Portal>
    </Styled.Container>
  );
}

export default DropdownMenu;
