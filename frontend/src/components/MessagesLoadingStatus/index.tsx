import MessageCardSkeleton from "@src/components/MessageCardSkeleton";

interface Props {
  length: number;
}

function MessagesLoadingStatus({ length }: Props) {
  return (
    <>
      {Array.from({ length }).map((_, index) => (
        <MessageCardSkeleton key={index} />
      ))}
    </>
  );
}

export default MessagesLoadingStatus;
