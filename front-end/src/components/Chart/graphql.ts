import { gql, TypedDocumentNode } from '@apollo/client';
import {
  GetStockPricesSubscription,
  GetStockPricesSubscriptionVariables,
} from '@/src/__generated__/types';

export const STOCK_PRICES_REACTIVE_STREAM: TypedDocumentNode<
  GetStockPricesSubscription,
  GetStockPricesSubscriptionVariables
> = gql`
  subscription GetStockPrices {
    stockPrice: stockPriceReactiveStream {
      ticker
      date
      price
    }
  }
`;
