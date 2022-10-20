/* eslint-disable react/prop-types */
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

export const defaultTemplate = template.bind({});

defaultTemplate.args = {
  message: "스낵바 메시지",
  status: "success",
};
