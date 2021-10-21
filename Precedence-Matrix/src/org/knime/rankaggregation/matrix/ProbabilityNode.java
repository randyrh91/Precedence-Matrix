package org.knime.rankaggregation.matrix;

/**
 * 
 * @author Randy Reyna Hernández
 * 
 */

public class ProbabilityNode {
	private Integer dividendo;
	private Integer divisor;

	public ProbabilityNode() {
		this.dividendo = 0;
		this.divisor = 0;
	}

	public Integer getDividendo() {
		return dividendo;
	}

	public void setDividendo(Integer dividendo) {
		this.dividendo = dividendo;
	}

	public Integer getDivisor() {
		return divisor;
	}

	public void setDivisor(Integer divisor) {
		this.divisor = divisor;
	}
}
