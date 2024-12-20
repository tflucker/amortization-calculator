package com.tim.amortization.calculator.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * POJO object used to contain all data for one month of the amortization
 * schedule
 */
public class AmortizationRecord {

	private int month;

	private BigDecimal principalPaid;

	private BigDecimal interestPaid;

	private BigDecimal remainingPrincipal;

	public AmortizationRecord() {

	}

	public AmortizationRecord(int month, BigDecimal principalPaid, BigDecimal interestPaid,
			BigDecimal remainingPrincipal) {
		super();
		this.month = month;
		this.principalPaid = principalPaid;
		this.interestPaid = interestPaid;
		this.remainingPrincipal = remainingPrincipal;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public BigDecimal getPrincipalPaid() {
		return principalPaid;
	}

	public void setPrincipalPaid(BigDecimal principalPaid) {
		this.principalPaid = principalPaid;
	}

	public BigDecimal getInterestPaid() {
		return interestPaid;
	}

	public void setInterestPaid(BigDecimal interestPaid) {
		this.interestPaid = interestPaid;
	}

	public BigDecimal getRemainingPrincipal() {
		return remainingPrincipal;
	}

	public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
		this.remainingPrincipal = remainingPrincipal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(interestPaid, month, principalPaid, remainingPrincipal);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmortizationRecord other = (AmortizationRecord) obj;
		return Objects.equals(interestPaid, other.interestPaid) && month == other.month
				&& Objects.equals(principalPaid, other.principalPaid)
				&& Objects.equals(remainingPrincipal, other.remainingPrincipal);
	}

	@Override
	public String toString() {
		return "AmortizationRecord [month=" + month + ", principalPaid=" + principalPaid + ", interestPaid="
				+ interestPaid + ", remainingPrincipal=" + remainingPrincipal + "]";
	}

}
