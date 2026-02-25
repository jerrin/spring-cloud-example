import { Button } from 'antd';

export default function Home() {
  return (
    <div className="flex flex-col items-start">
      <Button type="link" size="large" href="/chat">
        Chat with GraphQL Subscriptions
      </Button>
      <Button type="link" size="large" href="/chart">
        Chart with GraphQL Data
      </Button>
    </div>
  );
}
