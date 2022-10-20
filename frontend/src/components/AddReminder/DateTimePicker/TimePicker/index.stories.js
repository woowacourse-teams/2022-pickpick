import TimePicker from ".";
import useTimePicker from "../../@hooks/useTimePicker";
import { Container } from "../../style";

export default {
  title: "@component/TimePicker",
  component: TimePicker,
};

const Template = () => {
  const {
    meridiemRef,
    AMHourRef,
    PMHourRef,
    minuteRef,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
    handleChangeMeridiem,
    handleChangeHour,
    handleChangeMinute,
    handleResetTimePickerPosition,
  } = useTimePicker({ remindDate: "2022-09-22T22:50:00" });

  return (
    <Container>
      <TimePicker
        meridiemRef={meridiemRef}
        AMHourRef={AMHourRef}
        PMHourRef={PMHourRef}
        minuteRef={minuteRef}
        checkedMeridiem={checkedMeridiem}
        checkedHour={checkedHour}
        checkedMinute={checkedMinute}
        handleChangeMeridiem={handleChangeMeridiem}
        handleChangeHour={handleChangeHour}
        handleChangeMinute={handleChangeMinute}
        handleResetTimePickerPosition={handleResetTimePickerPosition}
      />
    </Container>
  );
};

export const DefaultTemplate = Template.bind({});
