package org.hashcode.test.jsqueeze;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.log4j.Logger;
import org.hashcode.jsqueeze.RLE;
import org.junit.Test;

public class RLETest {
	// algoritmus
	private static RLE alg = new RLE();

	private static final Logger LOG = Logger.getLogger(RLETest.class);

	@Test
	public void test() {
		File dir = new File("data");
		for (File file : dir.listFiles()) {
			try {
				String text = Util.readFile(file.getAbsolutePath());
				String encoded = alg.encode(text);
				String decoded = alg.decode(encoded);

				// informace o komprimaci
				LOG.info("Soubor: " + file.getName() + ", Ration: "
						+ ((double) encoded.length() / text.length()) * 100.0);

				// kontrola délky
				if (encoded.length() > decoded.length())
					fail();

				assertTrue(text.equals(decoded));

			} catch (Exception e) {
				LOG.error("Vyjímka při testování komprese", e);
				fail();
			}
		}
	}
}
