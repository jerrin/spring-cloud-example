import type { Metadata } from 'next';
import { Geist, Geist_Mono } from 'next/font/google';
import { AntdRegistry } from '@ant-design/nextjs-registry';
import './globals.css';
import { ApolloWrapper } from '@/src/app/ApolloWrapper';

const geistSans = Geist({
  variable: '--font-geist-sans',
  subsets: ['latin'],
});

const geistMono = Geist_Mono({
  variable: '--font-geist-mono',
  subsets: ['latin'],
});

export const metadata: Metadata = {
  title: 'Sample App for GraphQL Subscriptions with Next.js 16 and Apollo Client',
  description: 'UI for GraphQL Subscriptions with Next.js 16 and Apollo Client',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
        <ApolloWrapper>
          <AntdRegistry>
            <div className="flex min-h-screen items-center justify-center bg-zinc-50 font-sans dark:bg-black">
              <main className="flex min-h-screen w-full max-w-3xl flex-col items-center py-16 px-16 bg-white dark:bg-black sm:items-start">
                {children}
              </main>
            </div>
          </AntdRegistry>
        </ApolloWrapper>
      </body>
    </html>
  );
}
