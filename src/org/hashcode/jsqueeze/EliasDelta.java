package org.hashcode.jsqueeze;

public class EliasDelta {

	private EliasGamma gamma = new EliasGamma();
	private EliasBeta beta = new EliasBeta();

	public String encode(int value) {
		StringBuilder sb = new StringBuilder();
		String b = beta.encode(value);

		sb.append(gamma.encode(b.length()));
		b = b.substring(1);
		sb.append(b);

		return sb.toString();
	}

	public String decode(String data, int value) {
		int a = 0;
		data = gamma.decode(data, a);
		int b = 0;
		data = beta.decode(data, a - 1, b);

		value = (1 << (a - 1)) + b;
		return data;
	}
}
