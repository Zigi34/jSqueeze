package org.hashcode.test.jsqueeze;

import org.apache.log4j.Logger;
import org.hashcode.jsqueeze.Fibonacci;
import org.junit.Test;

public class FibonacciTest {
	// algoritmus
	private static Fibonacci alg = new Fibonacci();

	private static final Logger LOG = Logger.getLogger(FibonacciTest.class);

	@Test
	public void test() {
		String encoded = alg.encode(845584);
		System.out.println(encoded);
		int value = 0;
		String decoded = alg.decode(encoded, value);
		System.out.println(decoded);
	}
}
