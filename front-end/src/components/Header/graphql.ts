import { gql, TypedDocumentNode } from '@apollo/client';
import { GetMessageQuery, GetMessageQueryVariables } from '@/src/__generated__/types';

export const GET_MESSAGE: TypedDocumentNode<GetMessageQuery, GetMessageQueryVariables> = gql`
  query GetMessage {
    message {
      id
      content
    }
  }
`;
