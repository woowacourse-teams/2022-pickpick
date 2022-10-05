import {
  Hours,
  Meridiem,
  MeridiemHours,
  getDateInformation,
  getMeridiemTime,
} from "@src/@utils";

type GenerateTimeOptions = () => Record<
  "meridiems" | "AMHours" | "PMHours" | "minutes",
  string[]
>;

// {
//   // meridiems: ["오전", "오후"];
//   // AMHours: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // PMHours: ["12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // minutes: ["00", "10", "20", "30", "40", "50"];

// };

export const generateTimeOptions: GenerateTimeOptions = () => {
  const meridiems = ["오전", "오후"];
  const AMHours = Array.from({ length: 12 }, (_, index) => index.toString());
  const PMHours = Array.from({ length: 12 }, (_, index) => {
    if (index === 0) return "12";

    return index.toString();
  });
  const minutes = Array.from({ length: 6 }, (_, index) =>
    (index * 10).toString()
  );

  return {
    meridiems,
    AMHours,
    PMHours,
    minutes,
  };
};

type GenerateDateOptions = () => Record<"years" | "months" | "dates", string[]>;

export const generateDateOptions: GenerateDateOptions = () => {
  const { year, month } = getDateInformation(new Date());
  const { date: lastDate } = getDateInformation(new Date(year, month, 0));

  const years = [year, year + 1, year + 2].map((year) => year.toString());
  const months = Array.from({ length: 12 }, (_, index) =>
    (index + 1).toString()
  );
  const dates = Array.from({ length: lastDate }, (_, index) =>
    (index + 1).toString()
  );

  return {
    years,
    months,
    dates,
  };
};

type ParseTime = (ISODateTime: string) => {
  meridiem: Meridiem;
  meridiemHour: MeridiemHours;
  minute: string;
};

export const parseTime: ParseTime = (ISODateTime) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getMeridiemTime(
    Number(hour) as Hours
  );

  return {
    meridiem,
    meridiemHour,
    minute,
  };
};

type ParseDate = (
  ISODateTime: string
) => Record<"year" | "month" | "date", string>;

export const parseDate: ParseDate = (ISODateTime) => {
  const [fullDate] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");

  return {
    year,
    month,
    date,
  };
};

type ConvertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => Record<"parsedHour" | "parsedMinute", number>;

export const convertTimeToStepTenMinuteTime: ConvertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

type InvalidMeridiem = (value: Meridiem) => boolean;

export const invalidMeridiem: InvalidMeridiem = (value) => {
  return value !== "오전" && value !== "오후";
};
