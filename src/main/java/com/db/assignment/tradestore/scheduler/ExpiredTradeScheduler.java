package com.db.assignment.tradestore.scheduler;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.db.assignment.tradestore.service.TradeStoreService;

public class ExpiredTradeScheduler {
	
	@Autowired
	private TradeStoreService service;
	
	//Task scheduled to run everyday to update the expired flag depending on maturity date
	@Scheduled(cron = "0 0 12 1/1 * ? *", zone="IST")
    public void update() throws ParseException {
    	
    	service.updateExpiredFlag();
    }
}
