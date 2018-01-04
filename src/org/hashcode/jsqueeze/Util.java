package org.hashcode.jsqueeze;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Util {
	private static byte[] data = new byte[1];
	private static StringBuilder sb = new StringBuilder();

	public static String toString(byte character) {
		data[0] = character;
		try {
			return new String(data, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static byte toByte(String data) {
		try {
			return data.getBytes("US-ASCII")[0];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0x0;
	}

	public static String printBytes(byte[] bytes, int start) {
		sb.delete(0, sb.length());
		sb.append("[");
		for (int i = start; i < bytes.length; i++)
			sb.append(bytes[i] + ",");
		sb.append("]");
		return sb.toString();
	}

	public static String printBytes(byte[] bytes, int start, int end) {
		sb.delete(0, sb.length());
		sb.append("[");
		for (int i = start; i < end; i++)
			sb.append(bytes[i] + ",");
		sb.append("]");
		return sb.toString();
	}

	public static String printSymbols(Set<Symbol> symbols) {
		sb.delete(0, sb.length());
		sb.append("[");
		for (Symbol sym : symbols)
			sb.append(sym.toString() + ",");
		sb.append("]");
		return sb.toString();
	}

	public static String printBytes(List<Byte> bytes, int start) {
		sb.delete(0, sb.length());
		sb.append("[");
		for (byte cislo : bytes)
			sb.append(cislo + ",");
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Vrac� bu� abecedu nebo dopln�k abecedy
	 * 
	 * @param data
	 *            analyzovan� data
	 * @param complements
	 *            m� se vr�tit dopln�k?
	 * @return
	 */
	public static List<Byte> getAlphabet(byte[] data, boolean complements) {
		List<Byte> list = new LinkedList<>();
		for (int i = 0; i < data.length; i++) {
			if (!list.contains(data[i]))
				list.add(data[i]);
		}
		if (complements) {
			List<Byte> result = new LinkedList<>();
			for (byte i = Byte.MIN_VALUE + 1; i < Byte.MAX_VALUE; i++)
				if (!list.contains(i))
					result.add(i);
			return result;
		}
		return list;
	}
}
