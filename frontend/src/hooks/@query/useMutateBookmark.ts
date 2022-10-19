import { useMutation } from "react-query";

import { deleteBookmark, postBookmark } from "@src/api/bookmarks";

interface Props {
  handleSettleAddBookmark?: VoidFunction;
  handleSettleRemoveBookmark?: VoidFunction;
}

type Handler = (messageId: number) => VoidFunction;

interface UseMutateBookmarkResult {
  handleAddBookmark: Handler;
  handleRemoveBookmark: Handler;
}

function useMutateBookmark({
  handleSettleAddBookmark,
  handleSettleRemoveBookmark,
}: Props): UseMutateBookmarkResult {
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
