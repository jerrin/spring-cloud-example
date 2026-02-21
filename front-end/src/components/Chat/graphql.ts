import { gql, TypedDocumentNode } from '@apollo/client';
import {
  GetAllMessagesQuery,
  GetAllMessagesQueryVariables,
  GetMessageReactiveStreamSubscription,
  GetMessageReactiveStreamSubscriptionVariables,
  GetMessagesQuery,
  GetMessagesQueryVariables,
  GetMessageStreamSubscription,
  GetMessageStreamSubscriptionVariables,
  SendMessageMutation,
  SendMessageMutationVariables,
} from '@/src/__generated__/types';

export const SEND_MESSAGE: TypedDocumentNode<SendMessageMutation, SendMessageMutationVariables> =
  gql`
    mutation SendMessage($message: String!) {
      sendMessage(input: { content: $message }) {
        id
        content
      }
    }
  `;

export const GET_MESSAGES: TypedDocumentNode<GetMessagesQuery, GetMessagesQueryVariables> = gql`
  query GetMessages {
    messages {
      id
      content
    }
  }
`;

// TODO: apollo client doesnt support alias in the cache; hence introduce a separate query for all messages like messages query.
export const GET_ALL_MESSAGES: TypedDocumentNode<
  GetAllMessagesQuery,
  GetAllMessagesQueryVariables
> = gql`
  query GetAllMessages {
    allMessages {
      id
      content
    }
  }
`;

export const SUBSCRIBE_MESSAGE_STREAM: TypedDocumentNode<
  GetMessageStreamSubscription,
  GetMessageStreamSubscriptionVariables
> = gql`
  subscription GetMessageStream {
    message: messageStream {
      id
      content
    }
  }
`;

export const SUBSCRIBE_MESSAGE_REACTIVE_STREAM: TypedDocumentNode<
  GetMessageReactiveStreamSubscription,
  GetMessageReactiveStreamSubscriptionVariables
> = gql`
  subscription GetMessageReactiveStream {
    message: messageReactiveStream {
      id
      content
    }
  }
`;
