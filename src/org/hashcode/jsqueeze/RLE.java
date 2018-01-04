package org.hashcode.jsqueeze;

public class RLE implements Compression {
	public String encode(String plain) {
		StringBuilder sb = new StringBuilder();

		Character startChar = plain.charAt(0);
		int length = 1;

		for (int i = 1; i < plain.length(); i++) {
			Character sign = plain.charAt(i);
			if (sign.equals(startChar)) {
				length++;
				if (length == 9) {
					sb.append("@" + startChar + String.valueOf(length));
					length = 1;
				}
			} else {
				if (length >= 2) {
					sb.append("@" + startChar + String.valueOf(length));
					length = 1;
				} else {
					length = 1;
					sb.append(startChar);
				}
			}
			startChar = sign;
		}
		if (length >= 2) {
			sb.append("@" + startChar + String.valueOf(length));
		} else {
			sb.append(startChar);
		}

		String result = sb.toString();
		if (result.length() > plain.length())
			return plain;
		return sb.toString();
	}

	public String decode(String komprimed) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < komprimed.length(); i++) {
			Character sign = komprimed.charAt(i);
			if (sign.equals('@')) {
				Character temp = komprimed.charAt(i + 1);
				Character numChar = komprimed.charAt(i + 2);
				int count = Integer.parseInt(numChar.toString());
				for (int j = 0; j < count; j++)
					sb.append(temp);
				i += 2;
			} else
				sb.append(sign);
		}
		return sb.toString();
	}
}
