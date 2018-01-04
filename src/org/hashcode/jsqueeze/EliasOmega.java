package org.hashcode.jsqueeze;

public class EliasOmega {

	private EliasBeta beta = new EliasBeta();

	public String encode(int value) {
		if (value == 1)
			return "0";
		StringBuilder sb = new StringBuilder();
		sb.append('0');
		String d = beta.encode(value);
		for (int i = d.length() - 1; i >= 0; i--) {
			sb.append(d.charAt(i));
		}

		while (d.length() > 2) {
			d = beta.encode(d.length() - 1);
			for (int i = d.length() - 1; i >= 0; i--) {
				sb.append(d.charAt(i));
			}
		}

		String s = sb.toString();
		StringBuilder sb2 = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb2.append(s.charAt(i));
		}
		return s;
	}

	public String decode(String data, int value) {
		int index = 0;
		value = 1;
		while (data.charAt(index) != '0') {
			String val = data.substring(index, value + 1);
			index += value + 1;
			beta.decode(val, value);

		}
		return data.substring(index + 1);

	}
}
