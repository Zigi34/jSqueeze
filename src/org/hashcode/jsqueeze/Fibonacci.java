package org.hashcode.jsqueeze;

public class Fibonacci {

	int[] FibonacciNumbers = new int[] { 1, 2, 3, 5, 8, 13, 21, 34, 55, 89,
			144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711,
			28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040,
			1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817,
			39088169 };

	public String encode(int value) {
		int index = 0;
		while (FibonacciNumbers[index] <= value)
			index++;
		index--;

		StringBuilder sb = new StringBuilder();

		sb.append('1');
		while (index >= 0) {
			if (FibonacciNumbers[index] <= value) {
				sb.append('1');
				value -= FibonacciNumbers[index];
			} else
				sb.append('0');
			index--;
		}

		String s = sb.toString();
		StringBuilder sb2 = new StringBuilder();
		for (int i = s.length() - 1; i >= 0; i--)
			sb2.append(s.charAt(i));
		return sb2.toString();
	}

	public String decode(String data, int value) {
		value = 0;
		int index = 0;
		boolean wasOne = false;
		while (!(wasOne && data.charAt(index) == '1')) {
			if (data.charAt(index) == '1') {
				value += FibonacciNumbers[index];
				wasOne = true;
			} else
				wasOne = false;
			index++;
		}

		return data.substring(index + 1);
	}
}
