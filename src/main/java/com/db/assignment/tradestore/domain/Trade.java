package com.db.assignment.tradestore.domain;

import java.util.Date;

public class Trade implements Comparable<Trade> {
	
	private String tradeId;
	
	private Integer version;
	
	private String counterPartyId;
	
	private String bookId;
	
	private String maturityDate;
	
	private Date createdDate;
	
	private Boolean expired;
	
	public Trade(String tradeId, Integer version, String counterPartyId, String bookId, String maturityDate) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.counterPartyId = counterPartyId;
		this.bookId = bookId;
		this.maturityDate = maturityDate;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
	
	@Override
	public int hashCode() {
		
		return tradeId.hashCode() * 31 + version * 17;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Trade) {
			Trade t = (Trade) obj;
			return this.tradeId.equals(t.getTradeId()) && this.version.intValue() == t.getVersion().intValue();
		}
		return false;
	}

	 @Override
	    public int compareTo(Trade trade) {
	        int isEqual = 0;
	        
	        if (this.tradeId.compareTo(trade.getTradeId()) < 0) {
	            isEqual = -1;
	        } else if (this.tradeId.compareTo(trade.getTradeId()) > 0) {
	            isEqual = 1;
	        } else {
	            isEqual = 0;
	        }
	        
	        return isEqual;
	   }
	 
        @Override
	    public String toString() {
	        return "[TradeId - " + getTradeId() + " , Version - " + getVersion() + " , counterPartyId - " + getCounterPartyId() +
	        " , BookId - " + getBookId() + " , MaturityDate - " + getMaturityDate() + " , CreatedDate - " + getCreatedDate() +
	        " , Expired - " + getExpired() + "]";
	    }
}
