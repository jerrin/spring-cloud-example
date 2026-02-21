'use client';

import { HttpLink, ApolloLink } from '@apollo/client';
import { GraphQLWsLink } from '@apollo/client/link/subscriptions';
import { createClient } from 'graphql-ws';
import {
  ApolloNextAppProvider,
  ApolloClient,
  InMemoryCache,
} from '@apollo/client-integration-nextjs';
import { OperationTypeNode } from 'graphql/language';
import { map } from 'rxjs';

function makeClient() {
  const httpLink = new HttpLink({
    uri: process.env.NEXT_PUBLIC_GRAPHQL_ENDPOINT,
  });

  const wsLink = new GraphQLWsLink(
    createClient({
      url: process.env.NEXT_PUBLIC_GRAPHQL_WS_ENDPOINT!,
      retryAttempts: Infinity,
    })
  );

  const link = ApolloLink.split(
    ({ operationType }) => operationType === OperationTypeNode.SUBSCRIPTION,
    wsLink,
    httpLink
  );

  const logger = new ApolloLink((operation, forward) => {
    return forward(operation).pipe(
      map((result) => {
        console.log(` operation ${operation.operationName}`, result);
        return result;
      })
    );
  });

  const links = [link];
  const linksWithLogger =
    process.env.NEXT_PUBLIC_ENABLE_LOGGING === 'true' ? [logger, ...links] : links;

  return new ApolloClient({
    cache: new InMemoryCache(),
    link: ApolloLink.from(linksWithLogger),
    devtools: {
      enabled: true,
    },
  });
}

// you need to create a component to wrap your app in
export function ApolloWrapper({ children }: React.PropsWithChildren) {
  return <ApolloNextAppProvider makeClient={makeClient}>{children}</ApolloNextAppProvider>;
}
