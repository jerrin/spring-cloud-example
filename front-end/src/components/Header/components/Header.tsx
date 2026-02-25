'use client';

import { Typography } from 'antd';

const { Title } = Typography;

export const Header = ({ heading }: { heading: string }) => {
  return (
    <div className="inline-block width w-full">
      <Title className="flex justify-center flex-1">{heading}</Title>
    </div>
  );
};
