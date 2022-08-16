import { HandleReminderSubmitProps } from "@src/hooks/useSetReminder";
import * as Styled from "./style";

interface Props {
  targetMessageId: string;
  isTargetMessageSetReminded: boolean;
  handleCloseReminderModal: () => void;
  handleReminderSubmit: ({ event, key }: HandleReminderSubmitProps) => void;
  handleRemoveSubmit: (targetMessageId: string) => void;
}

function ReminderModalButtons({
  targetMessageId,
  isTargetMessageSetReminded,
  handleCloseReminderModal,
  handleReminderSubmit,
  handleRemoveSubmit,
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

      {!isTargetMessageSetReminded && (
        <Styled.Button
          text="생성"
          type="submit"
          onClick={(event) => handleReminderSubmit({ event, key: "create" })}
        >
          생성
        </Styled.Button>
      )}

      {isTargetMessageSetReminded && (
        <>
          <Styled.Button
            text="삭제"
            type="button"
            onClick={() => handleRemoveSubmit(targetMessageId)}
          >
            삭제
          </Styled.Button>

          <Styled.Button
            text="수정"
            type="submit"
            onClick={(event) => handleReminderSubmit({ event, key: "modify" })}
          >
            수정
          </Styled.Button>
        </>
      )}
    </Styled.Container>
  );
}

export default ReminderModalButtons;
