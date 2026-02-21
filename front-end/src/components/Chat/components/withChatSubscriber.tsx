'use client';

import { Chat } from './Chat';
import { TypedDocumentNode } from '@apollo/client';
import { useEffect } from 'react';
import { useSuspenseQuery } from '@apollo/client/react';
import {
  GetAllMessagesQuery,
  GetAllMessagesQueryVariables,
  GetMessageReactiveStreamSubscription,
  GetMessageReactiveStreamSubscriptionVariables,
  GetMessagesQuery,
  GetMessagesQueryVariables,
  GetMessageStreamSubscription,
  GetMessageStreamSubscriptionVariables,
} from '@/src/__generated__/types';

//TODO: make this generic
type WithChatSubscriberProps = {
  query: TypedDocumentNode<
    GetMessagesQuery | GetAllMessagesQuery,
    GetMessagesQueryVariables | GetAllMessagesQueryVariables
  >;
  subscription: TypedDocumentNode<
    GetMessageStreamSubscription | GetMessageReactiveStreamSubscription,
    GetMessageStreamSubscriptionVariables | GetMessageReactiveStreamSubscriptionVariables
  >;
};

export const withChatSubscriber = (props: WithChatSubscriberProps) => {
  const ChatWithSubscriber = ({ title }: { title: string }) => {
    const { data, error, subscribeToMore } = useSuspenseQuery(props.query);

    useEffect(() => {
      if (data) {
        const unsubscribe = subscribeToMore({
          document: props.subscription,
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-expect-error
          updateQuery: (prev, { subscriptionData }) => {
            if (!subscriptionData.data) return prev;
            const newMessage = subscriptionData.data.message;
            const key = 'messages' in data ? 'messages' : 'allMessages';
            return {
              ...prev,
              // eslint-disable-next-line @typescript-eslint/ban-ts-comment
              // @ts-expect-error
              [key]: [newMessage, ...(prev?.[key] || [])],
            };
          },
        });
        return () => {
          unsubscribe();
        };
      }
    }, [data, subscribeToMore]);

    if (error) return <p>Error: {error.message}</p>;

    return (
      <Chat
        id={'messages' in data ? 'msgs' : 'all-msgs'}
        messages={'messages' in data ? data.messages : data.allMessages}
        title={title}
      />
    );
  };

  return ChatWithSubscriber;
};
