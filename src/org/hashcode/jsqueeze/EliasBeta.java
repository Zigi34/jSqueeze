package org.hashcode.jsqueeze;

public class EliasBeta {

	public String encode(int value) {
		StringBuilder sb = new StringBuilder();
		while (value != 0) {
			sb.append(((value & 1) == 1) ? '1' : '0');
			value /= 2;
		}

		StringBuilder sb2 = new StringBuilder();
		for (int i = sb.length() - 1; i >= 0; i--)
			sb2.append(sb.charAt(i));
		return sb2.toString();
	}

	public String decode(String data, int count, int value) {
		value = 0;
		for (int i = 0; i < count; i++) {
			value *= 2;
			value += (data.charAt(i) == '1') ? 1 : 0;
		}
		return data.substring(count);
	}

	public String decode(String data, int value) {
		int count = data.length();
		value = 0;
		for (int i = 0; i < count; i++) {
			value *= 2;
			value += (data.charAt(i) == '1') ? 1 : 0;
		}
		return data.substring(count);
	}
}
