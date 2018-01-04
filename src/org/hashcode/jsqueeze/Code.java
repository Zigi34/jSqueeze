package org.hashcode.jsqueeze;

public class Code {
	private Character sign;
	private Double prob;
	private String prefix = "";

	public Code(Character sign, Double prob) {
		this.sign = sign;
		this.prob = prob;
	}

	public Character getSign() {
		return sign;
	}

	public void setSign(Character sign) {
		this.sign = sign;
	}

	public Double getProb() {
		return prob;
	}

	public void setProb(Double prob) {
		this.prob = prob;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
