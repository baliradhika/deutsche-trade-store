package com.db.assignment.tradestore.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.assignment.tradestore.dao.TradeStoreDao;
import com.db.assignment.tradestore.domain.Trade;
import com.db.assignment.tradestore.exception.TradeException;

@Service
public class TradeStoreServiceImpl implements TradeStoreService {
	
	@Autowired
	private TradeStoreDao dao;
	
	@Override
	public void createOrUpdateTradeStore(List<Trade> trades) throws TradeException{
		
		Set<String> tradeIds = trades.stream().map(Trade :: getTradeId).collect(Collectors.toSet());
		
		/* Map of TradeId -> Version -> TradeObject
		 *  
		 *  Trade id will help identify if trade exists
		 *  
		 *  Version will help to identify if new record need to be created or existing record to be updated
		 *  (TradeId + Version will be a unique record in the store)
		 */
		Map<String, Map<Integer, Trade>> tradeMap = dao.getTradesById(tradeIds).stream().collect(Collectors.groupingBy(Trade :: getTradeId, Collectors.toMap(Trade :: getVersion, Function.identity())));
		
		for(Trade trade : trades) {
			
			if(!isValidaMaturityDate(trade))
				break;
			
			trade.setExpired(Boolean.FALSE);
			
			trade.setCreatedDate(new Date());
			
			// Does trade exists?
			if (tradeMap.containsKey(trade.getTradeId())) {
				
				updateExistingTrade(tradeMap, trade);
				
			} else {
				
				createNewTrade(tradeMap, trade);
			}
		}
		
		dao.createOrUpdateTrades(getFinalList(tradeMap));
	}

	private List<Trade> getFinalList(Map<String, Map<Integer, Trade>> tradeMap) {
		
		List<Trade> finalList = new ArrayList<>();
		
		tradeMap.values().stream().forEach(k -> k.values().stream().forEach(i -> finalList.add(i)));
		
		return finalList;
	}


	private void createNewTrade(Map<String, Map<Integer, Trade>> tradeMap, Trade trade) {
		
		Map<Integer, Trade> tempMap = new HashMap<>();
		
		tempMap.put(trade.getVersion(), trade);
		
		tradeMap.put(trade.getTradeId(), tempMap);
	}


	private void updateExistingTrade(Map<String, Map<Integer, Trade>> tradeMap, Trade trade) throws TradeException {
		
		Map<Integer, Trade> existingTrade = tradeMap.get(trade.getTradeId());
		
		int maxVersionOfExistingTrade = Collections.max(existingTrade.keySet());
		
		//Check if new trade coming in has a version less than already existing versions for the trade
		isValidVersion(trade, maxVersionOfExistingTrade);
		
		Map<Integer, Trade> tempMap = tradeMap.get(trade.getTradeId());
		
		tempMap.put(trade.getVersion(), trade);
			
		tradeMap.put(trade.getTradeId(), tempMap);
		
	}
	
	
	private boolean isValidVersion(Trade trade, int maxVersionOfExistingTrade) throws TradeException {
		
		if(trade.getVersion().intValue() < maxVersionOfExistingTrade)
			throw new TradeException("Invalid version. Greater version exists in store for " + trade.getTradeId());
	     
		return true;
	}


	private boolean isValidaMaturityDate(Trade trade) throws TradeException {
		
        Date tradeDate;
        
		try {
			tradeDate = convertStringToDate(trade.getMaturityDate());
		} catch (ParseException e) {
			throw new TradeException("Invalid Input. Enter Maturity Date in dd-MM-yyy foramt"); 
		}
        
		return !tradeDate.before(new Date());
	}
	
	private Date convertStringToDate(String stringDate) throws ParseException {
		
		SimpleDateFormat smdf = new SimpleDateFormat("dd-MM-yyyy");
		
		return smdf.parse(stringDate);
	}
	
	@Override
	public void updateExpiredFlag() throws ParseException {
		
		List<Trade> tradesWithUpdatedFlags = new ArrayList<>();
		
		for(Trade trade : dao.getTradeStore()) {
			
			if(convertStringToDate(trade.getMaturityDate()).before(new Date())) {
				
				trade.setExpired(Boolean.TRUE);
				
				tradesWithUpdatedFlags.add(trade);
			}	
		}
		
		dao.createOrUpdateTrades(tradesWithUpdatedFlags);
	}

}
