package org.hashcode.jsqueeze;

public class Symbol {
	private byte[] value;
	private byte symbol;

	public Symbol(byte[] value, byte symbol) {
		this.value = value;
		this.symbol = symbol;
	}

	public byte getValue(int index) {
		return value[index];
	}

	public int size() {
		return value.length;
	}

	public byte getSymbol() {
		return symbol;
	}

	public void setSymbol(byte symbol) {
		this.symbol = symbol;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Symbol) {
			Symbol symbol = (Symbol) obj;
			boolean isSame = true;
			for (int i = 0; i < value.length; i++)
				if (symbol.getValue(i) != getValue(i)) {
					isSame = false;
					break;
				}
			if (isSame)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int val = getValue(0);
		for (int i = 1; i < value.length; i++)
			val *= getValue(i);
		return val;
	}

	@Override
	public String toString() {
		return "Symbol(" + symbol + Util.printBytes(value, 0) + ")";
	}
}
