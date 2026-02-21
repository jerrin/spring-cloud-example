'use client';

import { Typography } from 'antd';
import { ChatMessage } from './ChatMessage';
import { GetMessageQuery } from '@/src/__generated__/types';
import { withChatSubscriber } from './withChatSubscriber';
import {
  GET_ALL_MESSAGES,
  GET_MESSAGES,
  SUBSCRIBE_MESSAGE_REACTIVE_STREAM,
  SUBSCRIBE_MESSAGE_STREAM,
} from '../graphql';

const { Title } = Typography;

export const Chat = ({
  id,
  messages,
  title,
}: {
  id: string;
  messages: Array<GetMessageQuery['message']>;
  title: string;
}) => {
  return (
    <>
      <Title level={5} className="ml-2 mt-2">
        {title}
      </Title>
      <div className="mt-4">
        {messages.map((message) => (
          <ChatMessage key={`${id}-${message.id} `} message={message} />
        ))}
      </div>
    </>
  );
};

export const ChatStream = withChatSubscriber({
  query: GET_MESSAGES,
  subscription: SUBSCRIBE_MESSAGE_STREAM,
});

export const ChatReactiveStream = withChatSubscriber({
  query: GET_ALL_MESSAGES,
  subscription: SUBSCRIBE_MESSAGE_REACTIVE_STREAM,
});
