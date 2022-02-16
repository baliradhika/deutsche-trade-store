package com.db.assignment.tradestore.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.db.assignment.tradestore.domain.Trade;

@Repository
public class TradeStoreDaoImpl implements TradeStoreDao {
	
	final List<Trade> tradeStore = new ArrayList<>();

	@Override
	public void createOrUpdateTrades(List<Trade> trades) {
		
		//If trades are updated we will remove them from the store and add the updated trade
		List<Trade> existingTradesToBeRemoved = new ArrayList<>();
		
		trades.stream().forEach(trade -> {
			
			tradeStore.stream().forEach(storeTrade -> {
				
				if(trade.getTradeId().equals(storeTrade.getTradeId()) && trade.getVersion().intValue() == storeTrade.getVersion().intValue())
					existingTradesToBeRemoved.add(storeTrade);
			});
		});
		
		tradeStore.removeAll(existingTradesToBeRemoved);
		
		tradeStore.addAll(trades);
		
		//Comparator implemented in Trade class to sort by Trade Id & version
		Collections.sort(tradeStore);
	}
	
	@Override
	public List<Trade> getTradeStore() {
		return tradeStore;
	}

	@Override
	public List<Trade> getTradesById(Set<String> tradeIds) {
		
		return tradeStore.stream().filter(k -> tradeIds.contains(k.getTradeId())).collect(Collectors.toList());
	}


}
