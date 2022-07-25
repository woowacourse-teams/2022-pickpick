import { postBookmark, deleteBookmark } from "@src/api/bookmarks";
import { useMutation } from "react-query";

interface Props {
  handleSettle: () => void;
}

type Handler = (messageId: string) => () => void;

interface ReturnType {
  handleAddBookmark: Handler;
  handleRemoveBookmark: Handler;
}

function useBookmark({ handleSettle }: Props): ReturnType {
  const { mutate: addBookmark } = useMutation(postBookmark, {
    onSettled: () => {
      handleSettle();
    },
  });
  const { mutate: removeBookmark } = useMutation(deleteBookmark, {
    onSettled: () => {
      handleSettle();
    },
  });

  const handleAddBookmark = (messageId: string) => () => {
    addBookmark(messageId);
  };

  const handleRemoveBookmark = (messageId: string) => () => {
    removeBookmark(messageId);
  };

  return { handleAddBookmark, handleRemoveBookmark };
}

export default useBookmark;
