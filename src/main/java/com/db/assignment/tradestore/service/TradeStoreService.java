package com.db.assignment.tradestore.service;

import java.text.ParseException;
import java.util.List;

import com.db.assignment.tradestore.domain.Trade;
import com.db.assignment.tradestore.exception.TradeException;

public interface TradeStoreService {
	
	public void createOrUpdateTradeStore(List<Trade> trades) throws TradeException;
	
	public void updateExpiredFlag() throws ParseException;
}
