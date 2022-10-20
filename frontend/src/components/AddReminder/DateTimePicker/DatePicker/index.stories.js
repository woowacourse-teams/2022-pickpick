import DatePicker from ".";
import useDatePicker from "../../@hooks/useDatePicker";

export default {
  title: "@component/DatePicker",
  component: DatePicker,
};

const Template = () => {
  const {
    yearRef,
    monthRef,
    dateRef,
    checkedYear,
    checkedMonth,
    checkedDate,
    handleChangeYear,
    handleChangeMonth,
    handleChangeDate,
    handleResetDatePickerPosition,
  } = useDatePicker({ remindDate: "2022-09-22T22:50:00" });

  return (
    <DatePicker
      yearRef={yearRef}
      monthRef={monthRef}
      dateRef={dateRef}
      checkedYear={checkedYear}
      checkedMonth={checkedMonth}
      checkedDate={checkedDate}
      handleChangeYear={handleChangeYear}
      handleChangeMonth={handleChangeMonth}
      handleChangeDate={handleChangeDate}
      handleResetDatePickerPosition={handleResetDatePickerPosition}
    />
  );
};

export const DefaultTemplate = Template.bind({});
