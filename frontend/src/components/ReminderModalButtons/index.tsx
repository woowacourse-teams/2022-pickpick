import * as Styled from "./style";

interface Props {
  messageId: string;
  remindDate: string;
  handleCloseReminderModal: () => void;
  handleCreateReminder: () => void;
  handleModifyReminder: () => void;
  handleRemoveReminder: (targetMessageId: string) => void;
}

function ReminderModalButtons({
  messageId,
  remindDate,
  handleCloseReminderModal,
  handleCreateReminder,
  handleModifyReminder,
  handleRemoveReminder,
}: Props) {
  return (
    <Styled.Container>
      <Styled.Button
        text="취소"
        type="button"
        onClick={handleCloseReminderModal}
      >
        취소
      </Styled.Button>

      {!remindDate && (
        <Styled.Button text="생성" type="submit" onClick={handleCreateReminder}>
          생성
        </Styled.Button>
      )}

      {remindDate && (
        <>
          <Styled.Button
            text="삭제"
            type="button"
            onClick={() => handleRemoveReminder(messageId)}
          >
            삭제
          </Styled.Button>

          <Styled.Button
            text="수정"
            type="submit"
            onClick={handleModifyReminder}
          >
            수정
          </Styled.Button>
        </>
      )}
    </Styled.Container>
  );
}

export default ReminderModalButtons;
