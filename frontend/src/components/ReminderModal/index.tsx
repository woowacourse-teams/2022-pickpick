import * as Styled from "./style";
import Calendar from "@public/assets/icons/Calendar.svg";
import DownArrowIcon from "@public/assets/icons/ArrowIcon-Down.svg";
import AlarmIcon from "@public/assets/icons/AlarmIcon-Active.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import useReminderModal from "@src/hooks/useReminderModal";

export type ButtonText = "생성" | "수정" | "취소";

interface Props {
  handleCloseReminderModal: () => void;
}

function ReminderModal({ handleCloseReminderModal }: Props) {
  const {
    ref: { yearRef, monthRef, dateRef, meridiemRef, hourRef, minuteRef },
    dateStateArray: { meridiems, hours, minutes, years, months, dates },
    checkedState: {
      checkedMeridiem,
      checkedHour,
      checkedMinute,
      checkedYear,
      checkedMonth,
      checkedDate,
    },
    handler: {
      handleChangeMeridiem,
      handleChangeHour,
      handleChangeMinute,
      handleChangeYear,
      handleChangeMonth,
      handleChangeDate,
      handleOpenDropdown,
      handleSubmit,
    },
  } = useReminderModal();

  return (
    <Styled.Container>
      <Styled.Title>리마인더 생성</Styled.Title>

      <Dropdown>
        {({ isDropdownOpened, handleToggleDropdown }) => {
          handleOpenDropdown(isDropdownOpened);

          return (
            <FlexColumn marginBottom="10px">
              <Styled.Subtitle>언제</Styled.Subtitle>

              <Styled.TextContainer onClick={handleToggleDropdown}>
                <FlexRow alignItems="center" gap="8px">
                  <Calendar width="16px" height="16px" fill="#8B8B8B" />
                  <Styled.Text>
                    {`${checkedYear}년 ${checkedMonth}월 ${checkedDate.padStart(
                      2,
                      "0"
                    )}일`}
                  </Styled.Text>
                </FlexRow>

                <DownArrowIcon width="24px" height="24px" fill="#8B8B8B" />
              </Styled.TextContainer>

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptions ref={yearRef}>
                    {years.map((year) => (
                      <Styled.Label key={year}>
                        <Styled.Radio
                          type="radio"
                          value={year}
                          onChange={handleChangeYear}
                          checked={checkedYear === year}
                        />
                        <Styled.TextOption>{year}년</Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>

                  <Styled.TextOptions ref={monthRef}>
                    {months.map((month) => (
                      <Styled.Label key={month}>
                        <Styled.Radio
                          type="radio"
                          value={month}
                          onChange={handleChangeMonth}
                          checked={checkedMonth === month}
                        />
                        <Styled.TextOption>
                          {month.padStart(2, "0")}월
                        </Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>

                  <Styled.TextOptions ref={dateRef}>
                    {dates.map((date) => (
                      <Styled.Label key={date}>
                        <Styled.Radio
                          type="radio"
                          value={date}
                          onChange={handleChangeDate}
                          checked={checkedDate === date}
                        />
                        <Styled.TextOption>
                          {date.padStart(2, "0")}일
                        </Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>
                </Styled.TextOptionContainer>
              )}
            </FlexColumn>
          );
        }}
      </Dropdown>

      <Dropdown>
        {({ isDropdownOpened, handleToggleDropdown }) => {
          handleOpenDropdown(isDropdownOpened);

          return (
            <FlexColumn>
              <Styled.Subtitle>시간</Styled.Subtitle>

              <Styled.TextContainer onClick={handleToggleDropdown}>
                <FlexRow alignItems="center" gap="8px">
                  <AlarmIcon width="16px" height="16px" fill="#8B8B8B" />
                  <Styled.Text>
                    {`${checkedMeridiem} ${checkedHour}:${checkedMinute.padStart(
                      2,
                      "0"
                    )}`}
                  </Styled.Text>
                </FlexRow>

                <DownArrowIcon width="24px" height="24px" fill="#8B8B8B" />
              </Styled.TextContainer>

              {isDropdownOpened && (
                <Styled.TextOptionContainer>
                  <Styled.TextOptions ref={meridiemRef}>
                    {meridiems.map((meridiem) => (
                      <Styled.Label key={meridiem}>
                        <Styled.Radio
                          type="radio"
                          value={meridiem}
                          onChange={handleChangeMeridiem}
                          checked={checkedMeridiem === meridiem}
                        />
                        <Styled.TextOption>{meridiem}</Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>

                  <Styled.TextOptions ref={hourRef}>
                    {hours.map((hour) => (
                      <Styled.Label key={hour}>
                        <Styled.Radio
                          type="radio"
                          value={hour}
                          onChange={handleChangeHour}
                          checked={checkedHour === hour}
                        />
                        <Styled.TextOption>
                          {hour.padStart(2, "0")}시
                        </Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>

                  <Styled.TextOptions ref={minuteRef}>
                    {minutes.map((minute) => (
                      <Styled.Label key={minute}>
                        <Styled.Radio
                          type="radio"
                          value={minute}
                          onChange={handleChangeMinute}
                          checked={checkedMinute === minute}
                        />
                        <Styled.TextOption>
                          {minute.padStart(2, "0")}분
                        </Styled.TextOption>
                      </Styled.Label>
                    ))}
                  </Styled.TextOptions>
                </Styled.TextOptionContainer>
              )}
            </FlexColumn>
          );
        }}
      </Dropdown>

      <FlexRow gap="8px" margin-top="18px" justifyContent="flex-end">
        <Styled.Button
          text="취소"
          type="button"
          onClick={handleCloseReminderModal}
        >
          취소
        </Styled.Button>
        <Styled.Button text="생성" type="button" onClick={handleSubmit}>
          생성
        </Styled.Button>
      </FlexRow>
    </Styled.Container>
  );
}

export default ReminderModal;
