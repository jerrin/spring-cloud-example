'use client';

import { Button, Input } from 'antd';
import { useMutation } from '@apollo/client/react';
import { SEND_MESSAGE } from '../graphql';
import { useCallback, useState } from 'react';
import { useClearMessages } from './hook/useClearMessages';

const { Search } = Input;

export const ChatPublisher = () => {
  const [message, setMessage] = useState<string>('');
  const [sendMessage] = useMutation(SEND_MESSAGE);
  const clearMessages = useClearMessages();

  const onSearch = useCallback(
    (msg: string) => {
      setMessage('');
      if (!msg.trim()) return;
      sendMessage({ variables: { message: msg } });
    },
    [sendMessage]
  );

  return (
    <div className="ml-2 my-4 w-full">
      <Button type="primary" size="large" className="my-2" onClick={clearMessages}>
        Clear Messages
      </Button>
      <Search
        showCount
        placeholder="Type your message here..."
        enterButton="Search"
        maxLength={200}
        size="large"
        style={{ width: 590 }}
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onSearch={onSearch}
      />
    </div>
  );
};
