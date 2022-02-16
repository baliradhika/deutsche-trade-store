package com.db.assignment.tradestore.dao;

import java.util.List;
import java.util.Set;

import com.db.assignment.tradestore.domain.Trade;

public interface TradeStoreDao {

	public void createOrUpdateTrades(List<Trade> trades);

	public List<Trade> getTradeStore();
	
	public List<Trade> getTradesById(Set<String> tradeIds);
	
}
