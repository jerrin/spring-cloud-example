'use client';

import { useQuery } from '@apollo/client/react';
import { GET_MESSAGE } from '@/src/components/Hello/graphql';
import { Typography } from 'antd';

const { Title } = Typography;

export const Hello = () => {
  const { error, data } = useQuery(GET_MESSAGE);

  const message = data?.message.content;

  if (error) return <p>Error: {error.message}</p>;

  return (
    <div className="inline-block width w-full">
      <Title className="flex justify-center flex-1">Welcome{message ? ` ${message}` : ''}</Title>
    </div>
  );
};
