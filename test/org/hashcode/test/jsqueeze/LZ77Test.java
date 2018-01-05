package org.hashcode.test.jsqueeze;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.log4j.Logger;
import org.hashcode.jsqueeze.LZ77;
import org.junit.Test;

public class LZ77Test {
	// algoritmus
	private static LZ77 alg = new LZ77();

	private static final Logger LOG = Logger.getLogger(LZ77Test.class);

	@Test
	public void test() {
		File dir = new File("data");
		for (File file : dir.listFiles()) {
			try {
				String text = Util.readFile(file.getAbsolutePath());
				byte[] source = text.getBytes();
				byte[] encoded = alg.encode(source);

				// informace o komprimaci
				System.out.println("Soubor: " + file.getName() + ", Ration: "
						+ ((double) encoded.length / text.getBytes().length) * 100.0);
			} catch (Exception e) {
				LOG.error("Vyjímka při testování komprese", e);
				fail();
			}
		}
	}
}
