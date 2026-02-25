'use client';

import { useQuery } from '@apollo/client/react';
import { GET_MESSAGE } from '@/src/components/Header/graphql';
import { Header } from './Header';

export const HeaderWithName = () => {
  const { error, data } = useQuery(GET_MESSAGE);

  const message = data?.message.content;

  if (error) return <p>Error: {error.message}</p>;

  const heading = `Welcome${message ? ` ${message}` : ''}`;

  return <Header heading={heading} />;
};
