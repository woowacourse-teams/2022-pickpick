import { ISOConverter } from "@src/@utils";
import usePortal from "@src/hooks/usePortal";
import { Link } from "react-router-dom";
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

  const renderDateOption = () => {
    if (date === "오늘") {
      return (
        <Styled.Option>
          <Link to={`/feed/${ISOConverter("어제")}`}>
            <Styled.Button type="button">어제</Styled.Button>
          </Link>
        </Styled.Option>
      );
    }

    if (date === "어제") {
      return (
        <Styled.Option>
          <Link to={`/feed/${ISOConverter("오늘")}`}>
            <Styled.Button type="button">오늘</Styled.Button>
          </Link>
        </Styled.Option>
      );
    }

    return (
      <>
        <Styled.Option>
          <Link to={`/feed/${ISOConverter("오늘")}`}>
            <Styled.Button type="button">오늘</Styled.Button>
          </Link>
        </Styled.Option>
        <Styled.Option>
          <Link to={`/feed/${ISOConverter("어제")}`}>
            <Styled.Button type="button">어제</Styled.Button>
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
