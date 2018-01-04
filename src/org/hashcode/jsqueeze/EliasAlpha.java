package org.hashcode.jsqueeze;

public class EliasAlpha {

	public String encode(int value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < value; i++)
			sb.append('0');
		sb.append('1');
		return sb.toString();
	}

	public String decode(String data, int value) {
		int i = 0;
		while (data.charAt(i) == '0') {
			i++;
		}
		value = i + 1;
		return data.substring(i + 1);
	}

}
