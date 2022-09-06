import { postBookmark, deleteBookmark } from "@src/api/bookmarks";
import { useMutation } from "react-query";

interface Props {
  handleSettleAddBookmark?: () => void;
  handleSettleRemoveBookmark?: () => void;
}

type Handler = (messageId: number) => () => void;

interface ReturnType {
  handleAddBookmark: Handler;
  handleRemoveBookmark: Handler;
}

function useMutateBookmark({
  handleSettleAddBookmark,
  handleSettleRemoveBookmark,
}: Props): ReturnType {
  const { mutate: addBookmark } = useMutation(postBookmark, {
    onSettled: handleSettleAddBookmark,
  });
  const { mutate: removeBookmark } = useMutation(deleteBookmark, {
    onSettled: handleSettleRemoveBookmark,
  });

  const handleAddBookmark = (messageId: number) => () => {
    addBookmark(messageId);
  };

  const handleRemoveBookmark = (messageId: number) => () => {
    removeBookmark(messageId);
  };

  return { handleAddBookmark, handleRemoveBookmark };
}

export default useMutateBookmark;
