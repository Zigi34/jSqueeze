package org.hashcode.jsqueeze;

public class EliasGamma {

	private EliasAlpha alpha = new EliasAlpha();
	/**
	 * <summary> The beta encoding for the second part of the code. </summary>
	 */
	private EliasBeta beta = new EliasBeta();

	public String encode(int value) {
		StringBuilder sb = new StringBuilder();
		String b = beta.encode(value);

		sb.append(alpha.encode(b.length()));
		b = b.substring(1);
		sb.append(b);

		return sb.toString();
	}

	public String decode(String data, int value) {
		int a = 0;
		data = alpha.decode(data, a);
		int b = 0;
		data = beta.decode(data, a - 1, b);

		value = (1 << (a - 1)) + b;
		return data;
	}

}
