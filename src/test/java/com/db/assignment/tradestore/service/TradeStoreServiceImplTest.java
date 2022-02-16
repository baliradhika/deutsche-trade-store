package com.db.assignment.tradestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.db.assignment.tradestore.dao.TradeStoreDaoImpl;
import com.db.assignment.tradestore.domain.Trade;
import com.db.assignment.tradestore.exception.TradeException;

@RunWith(MockitoJUnitRunner.class)
public class TradeStoreServiceImplTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@InjectMocks
	private TradeStoreServiceImpl service;
	
	@Mock
	private TradeStoreDaoImpl dao; 
	
	@Test
	public void shouldThrowExceptionIfLowerVersionTrade() throws TradeException {
		
		exception.expect(TradeException.class);
		
		List<Trade> input = new ArrayList<>();
		input.add((new Trade("T1", 1, "CP-1", "B1", "01-04-2022")));
		
		Mockito.when(dao.getTradesById(Mockito.anySet())).thenReturn(getTestTrades());
		service.createOrUpdateTradeStore(input);
		
		exception.expectMessage("Invalid version. Greater version exists in store for T1");
	}
    
	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotAddTradeWithInvaliMaturity() throws TradeException {
		
		//To check the list that is created as input to the dao method
		 doAnswer(new Answer<Void>() {
	            @Override
	            public Void answer(InvocationOnMock invocation) throws Throwable {
	                List<Trade> trades = (List<Trade>) (invocation.getArguments())[0];
	                assertEquals(2, trades.size());
	                return null;
	     }}).when(dao).createOrUpdateTrades(Mockito.anyList());

		service.createOrUpdateTradeStore(getTestTrades());
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldUpdateExpiredFlag() throws ParseException {
		
		//Input contains 1 expired trade so the method should try to update 1 trade -> T1
		
		Mockito.when(dao.getTradeStore()).thenReturn(getTestTrades());
		
		doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                List<Trade> trades = (List<Trade>) (invocation.getArguments())[0];
                assertEquals(1, trades.size());
                assertEquals("T1", trades.get(0).getTradeId());
                assertEquals("01-01-2022", trades.get(0).getMaturityDate());
                return null;
        }}).when(dao).createOrUpdateTrades(Mockito.anyList());

		
		service.updateExpiredFlag();
	}
	
	@Test
	public void shouldCreateOrUpdateStore() throws TradeException {
		
		service.createOrUpdateTradeStore(getTestTrades());
		
		Mockito.verify(dao).createOrUpdateTrades(Mockito.anyList());
	}
	
	private List<Trade> getTestTrades() {
		
		List<Trade> trades = new ArrayList<>();
		
		trades.add(new Trade("T1", 2, "CP-2", "B1", "01-03-2022"));
		
		trades.add(new Trade("T2", 1, "CP-1", "B2", "01-04-2022"));
		
		trades.add(new Trade("T1", 3, "CP-1", "B1", "01-01-2022"));
		
		return trades;
	} 
}
