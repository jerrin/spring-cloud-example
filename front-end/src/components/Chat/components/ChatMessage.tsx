import { GetMessageQuery } from '@/src/__generated__/types';

type ChatMessageProps = {
  message: GetMessageQuery['message'];
};

export const ChatMessage = ({ message }: ChatMessageProps) => {
  return (
    <div key={message.id} className="m-2 rounded border p-2">
      <p>
        <strong>User:</strong> {message.content}
      </p>
    </div>
  );
};
