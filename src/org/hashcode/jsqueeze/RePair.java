package org.hashcode.jsqueeze;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class RePair {

	public byte stopIndex = Byte.MIN_VALUE;
	private int size = 2;

	private Logger log = Logger.getLogger(RePair.class);
	private byte[] before = null;
	private byte[] after = null;
	private int beforeLength = 0;
	private int afterLength = 0;

	public RePair() {
		this(2);
	}

	public RePair(int size) {
		this.size = size;
	}

	/**
	 * Komprimace dat
	 * 
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] encode(byte[] data) throws UnsupportedEncodingException {
		log.debug("Komprimuji soubor (" + data.length + " B)");
		before = data;
		beforeLength = data.length;
		after = new byte[data.length];
		afterLength = after.length;

		List<Byte> neterminals = Util.getAlphabet(data, true);
		List<Symbol> dict = new LinkedList<Symbol>();

		while (neterminals.size() > 0) {
			int[] pairs = getAllPairs(before, beforeLength);
			int max = 0;
			int maxVal = 0;
			for (int i = 0; i < 256 * 256; i++) {
				if (pairs[i] > maxVal) {
					maxVal = pairs[i];
					max = i;
				}
			}
			if (maxVal == 0)
				break;

			byte[] value = new byte[2];
			value[0] = (byte) ((max >> 8) - 128);
			value[1] = (byte) ((max & 0xFF) - 128);

			byte neterminal = neterminals.remove(0);
			Symbol bestSymbol = new Symbol(value, neterminal);

			dict.add(bestSymbol);
			replace(before, beforeLength, bestSymbol, maxVal, neterminal);
			before = after;
			beforeLength = afterLength;
		}
		log.debug("Komprimováno s nahrazením " + dict.size() + " znaků");
		byte[] d = concate(dict, after, afterLength);
		log.debug("Komprimace ukončena (" + (d.length / (double) data.length) * 100 + " %)");
		return d;
	}

	private int[] getAllPairs(byte[] text, int byteLength) {
		int[] freq = new int[256 * 256];
		int prevValue = getSymbol(text, 0);
		freq[prevValue]++;
		for (int index = 1; index < byteLength - size; index++) {
			int value = getSymbol(text, index);

			if (prevValue == value) {
				prevValue = -1;
				continue;
			}

			freq[value]++;
			prevValue = value;
		}
		for (int index = 0; index < freq.length; index++) {
			if (freq[index] < 2)
				freq[index] = 0;
		}
		return freq;
	}

	private int getSymbol(byte[] text, int index) {
		int value = 0;
		for (int i = 0; i < size; i++) {
			value += text[index + i] + 128;
			value <<= 8;
		}
		value >>= 8;
		return value;
	}

	private byte[] concate(List<Symbol> symbols, byte[] textBytes, int byteLength) {
		byte[] result = new byte[byteLength + (size + 1) * symbols.size() + 1];
		int index = 0;
		result[index++] = (byte) symbols.size();
		for (Symbol s : symbols) {
			result[index++] = s.getSymbol();
			for (int i = 0; i < size; i++)
				result[index++] = s.getValue(i);
		}
		for (int i = 0; i < byteLength; i++)
			result[index++] = textBytes[i];
		return result;
	}

	private void replace(byte[] init, int initLength, Symbol symbol, int symbolCount, byte replaceWith) {
		int count = symbolCount;
		afterLength = initLength - (count * (size - 1));

		int newArrayIndex = 0;
		boolean isSame = true;
		for (int i = size - 1; i < initLength; i++) {
			// match with pair
			isSame = true;
			for (int j = size - 1; j >= 0; j--) {
				if (symbol.getValue(size - j - 1) != init[i - j]) {
					isSame = false;
					break;
				}
			}
			if (isSame) {
				after[newArrayIndex] = replaceWith;
				i += (size - 1);
			} else {
				after[newArrayIndex] = init[i - (size - 1)];
			}
			newArrayIndex++;
		}
		for (int i = (afterLength - newArrayIndex); i > 0; i--)
			after[newArrayIndex++] = init[initLength - (afterLength - newArrayIndex) - 1];
	}

	private byte[] replace(byte[] init, Symbol replaceWith) {
		int count = 0;
		for (int i = 0; i < init.length; i++)
			if (init[i] == replaceWith.getSymbol())
				count++;

		byte[] newArray = new byte[init.length + count * (size - 1)];
		int newArrayIndex = 0;
		for (int i = 0; i < init.length; i++) {
			// match with pair
			if (init[i] == replaceWith.getSymbol()) {
				for (int j = 0; j < size; j++) {
					newArray[newArrayIndex++] = replaceWith.getValue(j);
				}
			} else {
				newArray[newArrayIndex++] = init[i];
			}
		}
		return newArray;
	}

	private List<Symbol> getDictionary(byte[] textBytes) {
		List<Symbol> symbols = new LinkedList<>();
		int stop = (textBytes[0] & 0xff) * (size + 1) + 1;
		if (stop == 0)
			return symbols;

		for (int i = 1; i < stop; i += size + 1) {
			byte symbol = textBytes[i];
			byte[] value = new byte[size];
			for (int j = 0; j < size; j++) {
				value[j] = textBytes[i + j + 1];
			}
			Symbol s = new Symbol(value, symbol);
			symbols.add(s);
		}
		return symbols;
	}

	private byte[] getTextBytes(byte[] init, int stopIndex) {
		byte[] newArray = new byte[init.length - (stopIndex + 1)];
		int index = 0;
		for (int i = stopIndex + 1; i < init.length; i++) {
			newArray[index++] = init[i];
		}
		return newArray;
	}

	/**
	 * Dekomprimace dat
	 * 
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] decode(byte[] data) throws UnsupportedEncodingException {
		log.debug("Dekomprimuji soubor (" + data.length + " B)");
		List<Symbol> dict = getDictionary(data);
		Collections.reverse(dict);
		data = getTextBytes(data, dict.size() * (size + 1));
		for (Symbol symbol : dict) {
			data = replace(data, symbol);
		}
		log.debug("Dekomprimace ukončena");
		return data;
	}
}
