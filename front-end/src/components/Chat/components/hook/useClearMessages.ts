import { useApolloClient } from '@apollo/client/react';
import { useCallback } from 'react';
import { GET_ALL_MESSAGES, GET_MESSAGES } from '@/src/components/Chat/graphql';

export const useClearMessages = () => {
  const client = useApolloClient();

  const clearMessages = useCallback(() => {
    client.writeQuery({
      query: GET_MESSAGES,
      data: {
        messages: [],
      },
    });

    client.writeQuery({
      query: GET_ALL_MESSAGES,
      data: {
        allMessages: [],
      },
    });

    client.cache.gc();
  }, [client]);

  return clearMessages;
};
