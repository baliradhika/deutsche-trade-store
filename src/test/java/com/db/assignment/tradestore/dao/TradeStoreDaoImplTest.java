package com.db.assignment.tradestore.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.db.assignment.tradestore.domain.Trade;

@RunWith(MockitoJUnitRunner.class)
public class TradeStoreDaoImplTest {
	
	@InjectMocks
	private TradeStoreDaoImpl dao;
	
	@Before
	public void setup() {
		
		List<Trade> tradeStore = new ArrayList<>();
        
		tradeStore.add(new Trade("T1", 2, "CP-2", "B1", "01-03-2022"));
		
		tradeStore.add(new Trade("T2", 1, "CP-1", "B1", "01-04-2022"));
		
		tradeStore.add(new Trade("T1", 3, "CP-1", "B1", "01-01-2022"));
		
		ReflectionTestUtils.setField(dao, "tradeStore", tradeStore);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void shouldNotAddDuplicateTradeHavingSameIdAndVersion() {
		
		List<Trade> trades = new ArrayList<>();
		
		trades.add(new Trade("T1", 3, "CP-2", "B1", "01-03-2022"));
		
		dao.createOrUpdateTrades(trades);
		
		List<Trade> updatedStore = (List<Trade>) ReflectionTestUtils.getField(dao, "tradeStore");
		
		assertEquals(3, updatedStore.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	public void shouldAddNewTrade() {
		
		List<Trade> trades = new ArrayList<>();
		
		trades.add(new Trade("T4", 2, "CP-2", "B1", "01-03-2022"));
		
		dao.createOrUpdateTrades(trades);
		
		List<Trade> updatedStore = (List<Trade>) ReflectionTestUtils.getField(dao, "tradeStore");
		
		assertEquals(4, updatedStore.size());
	}
	
	@Test
	@Order(3)
	public void shouldReturnTradeById() {
		
		Set<String> tradeIds = new HashSet<>();
		tradeIds.add("T1");
		
		List<Trade> response = dao.getTradesById(tradeIds);
		
		assertEquals(2, response.size());
		assertEquals("T1", response.get(0).getTradeId());
		assertEquals("T1", response.get(1).getTradeId());
	}

}
