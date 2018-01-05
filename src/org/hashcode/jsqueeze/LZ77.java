package org.hashcode.jsqueeze;

import java.util.ArrayList;
import java.util.List;

public class LZ77 implements Compression {

	public static final int DEFAULT_SEARCH_BUFFER_SIZE = 6;
	public static final int DEFAULT_LOOKAHEAD_BUFFER_SIZE = 4;

	private int searchBufferSize = 1024;
	private int lookAheadBufferSize = 32;
	private int startIndex = 0;

	private List<Symbols> symbols = new ArrayList<>();

	private class Symbols {
		public byte first;
		public byte second;
		public byte data;

		public Symbols(byte first, byte second, byte data) {
			this.first = first;
			this.second = second;
			this.data = data;
		}
	}

	public LZ77(int searchSize, int lookAheadSize) {
		this.searchBufferSize = searchSize;
		this.lookAheadBufferSize = lookAheadSize;
		this.startIndex = 0;
	}

	public LZ77() {
		this(DEFAULT_SEARCH_BUFFER_SIZE, DEFAULT_LOOKAHEAD_BUFFER_SIZE);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void shift(int size) {
		this.startIndex += size;
	}

	public int getStartWindowIndex() {
		return startIndex - searchBufferSize;
	}

	public byte[] encode(byte[] data) {
		symbols.clear();
		this.startIndex = 0;

		// první znak hned zakódujeme
		symbols.add(new Symbols((byte) 0, (byte) 0, data[0]));

		shift(1);

		while (getStartIndex() < data.length) {
			byte firstFoundByte = data[getStartIndex()];

			List<Integer> matchIndex = getMatchIndex(data, firstFoundByte);
			List<Byte> foundSize = getMatchSize(data, matchIndex);

			int bestIndex = 0;
			int bestSize = 1;
			for (int i = 0; i < foundSize.size(); i++)
				if (bestSize < foundSize.get(i)) {
					bestSize = foundSize.get(i);
					bestIndex = i;
				}

			// žádná shoda nenalezena
			if (matchIndex.isEmpty()) {
				symbols.add(new Symbols((byte) 0, (byte) 0, firstFoundByte));
			} else {
				int startIndex = getStartIndex();
				if (startIndex + foundSize.get(bestIndex) < data.length) {
					symbols.add(new Symbols((byte) (startIndex - matchIndex.get(bestIndex)),
							(byte) foundSize.get(bestIndex), data[startIndex + foundSize.get(bestIndex)]));
				} else {
					symbols.add(new Symbols((byte) 0, (byte) 0, data[data.length - 1]));
				}
			}

			if (matchIndex.isEmpty())
				shift(1);
			else
				shift(foundSize.get(bestIndex) + 1);
		}

		return symbolsToByte(symbols);
	}

	private byte[] symbolsToByte(List<Symbols> symbols) {
		byte[] data = new byte[symbols.size() * 3];
		int i = 0;
		for (Symbols s : symbols) {
			data[i] = s.first;
			data[i + 1] = s.second;
			data[i + 2] = s.data;
			i += 3;
		}
		return data;
	}

	private List<Integer> getMatchIndex(byte[] data, byte sample) {
		List<Integer> indexes = new ArrayList<>();
		int endIndex = getStartWindowIndex() + searchBufferSize;
		for (int i = getStartWindowIndex(); i < endIndex && i < data.length; i++) {
			if (i < 0)
				continue;

			if (data[i] == sample)
				indexes.add(i);
		}
		return indexes;
	}

	private List<Byte> getMatchSize(byte[] data, List<Integer> sampleIndex) {
		List<Byte> sizes = new ArrayList<>();

		if (sampleIndex.isEmpty())
			return sizes;

		int startIndex = getStartIndex();
		for (Integer y : sampleIndex) {
			byte size = 1;
			for (int i = y + 1; i < startIndex && (startIndex + size) < data.length
					&& i < startIndex + lookAheadBufferSize; i++) {

				if (data[startIndex + size] == data[i])
					size += 1;
				else
					break;

			}
			sizes.add(size);
		}
		return sizes;
	}

	public byte[] decode(byte[] data) {
		return null;
	}

}
