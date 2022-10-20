/* eslint-disable react/prop-types */
import { useEffect } from "react";

import useSnackbar from "@src/hooks/useSnackbar";

import Snackbar from ".";

export default {
  title: "@component/Snackbar",
  component: Snackbar,
  argTypes: {
    status: {
      options: ["failure", "success"],
      control: { type: "radio" },
    },
  },
};

const template = ({ message, status }) => {
  const { openSuccessSnackbar, openFailureSnackbar } = useSnackbar();
  useEffect(() => {
    status === "success"
      ? openSuccessSnackbar(message)
      : openFailureSnackbar(message);
  }, []);
  return (
    <div>
      <button
        onClick={() => {
          status === "success"
            ? openSuccessSnackbar(message)
            : openFailureSnackbar(message);
        }}
      >
        Click To open snackbar!
      </button>
      <Snackbar />
    </div>
  );
};

export const SuccessTemplate = template.bind({});
export const FailureTemplate = template.bind({});

SuccessTemplate.args = {
  message: "스낵바 메시지",
  status: "success",
};

FailureTemplate.args = {
  message: "스낵바 메시지",
  status: "failure",
};
