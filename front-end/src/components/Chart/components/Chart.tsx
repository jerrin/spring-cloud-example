'use client';

import { CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis } from 'recharts';

import { useSubscription } from '@apollo/client/react';
import { STOCK_PRICES_REACTIVE_STREAM } from '../graphql';
import { useEffect, useState, useRef } from 'react';
import { GetStockPricesSubscription } from '@/src/__generated__/types';

export const Chart = () => {
  const [stockPrices, setStockPrices] = useState<GetStockPricesSubscription['stockPrice'][]>([]);
  const { data } = useSubscription(STOCK_PRICES_REACTIVE_STREAM);
  const pricesRef = useRef<GetStockPricesSubscription['stockPrice'][]>([]);

  useEffect(() => {
    if (data?.stockPrice) {
      const dd = data.stockPrice.date && new Date(data.stockPrice.date);
      if (dd) {
        const day = String(dd.getDate()).padStart(2, '0');
        const month = String(dd.getMonth() + 1).padStart(2, '0');
        pricesRef.current.push({ ...data.stockPrice, date: `${day}-${month}` });
        setStockPrices([...pricesRef.current]);
      }
    }
  }, [data?.stockPrice]);

  return (
    <LineChart
      key={stockPrices.length === 1 ? 'update' : 'original'}
      style={{
        width: '100%',
        maxWidth: '700px',
        height: '100%',
        maxHeight: '70vh',
        aspectRatio: 1.618,
      }}
      responsive
      data={stockPrices}
      margin={{
        top: 5,
        right: 0,
        left: 0,
        bottom: 5,
      }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="date" tick={false} />
      <YAxis width="auto" domain={[200, 'auto']} />
      <Tooltip />
      <Legend />
      <Line type="monotone" dataKey="price" stroke="#8884d8" name="Price" dot={false} />
    </LineChart>
  );
};
