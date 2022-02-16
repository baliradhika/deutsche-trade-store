package com.db.assignment.tradestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.assignment.tradestore.domain.Trade;
import com.db.assignment.tradestore.exception.TradeException;
import com.db.assignment.tradestore.service.TradeStoreService;

@RestController
public class TradeStoreController {
	
	@Autowired
	private TradeStoreService service;
	
	@PostMapping("/trade-store")
	public ResponseEntity<String> createTradeStore(@RequestBody List<Trade> trades) throws TradeException {
		 
		 service.createOrUpdateTradeStore(trades);
		 return new ResponseEntity<>("Store created successfully!", HttpStatus.OK);
	}
	
}
