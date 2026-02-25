import { HeaderWithName } from '@/src/components/Header';
import { ChatStream, ChatReactiveStream, ChatPublisher } from '@/src/components/Chat';
import { Suspense } from 'react';
import { Col, Row } from 'antd';

const Chat = () => (
  <>
    <HeaderWithName />
    <ChatPublisher />
    <Suspense fallback={<p>Loading chat...</p>}>
      <Row gutter={32}>
        <Col className="gutter-row">
          <ChatStream title="Stream of Messages" />
        </Col>
        <Col className="gutter-row">
          <ChatReactiveStream title="Reactive Stream of Messages" />
        </Col>
      </Row>
    </Suspense>
  </>
);

export default Chat;
