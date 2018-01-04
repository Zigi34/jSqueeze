package org.hashcode.test.jsqueeze;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.hashcode.jsqueeze.RePair;
import org.junit.Test;

public class RePairTest {
	// algoritmus
	private static RePair alg = new RePair();

	private static final Logger LOG = Logger.getLogger(RePairTest.class);

	@Test
	public void test() {
		File dir = new File("data");
		for (File file : dir.listFiles()) {
			try {
				String text = Util.readFile(file.getAbsolutePath());
				byte[] source = text.getBytes();
				byte[] encoded = alg.encode(source);
				byte[] decoded = alg.decode(encoded);

				// informace o komprimaci
				LOG.info("Soubor: " + file.getName() + ", Ration: "
						+ ((double) encoded.length / text.getBytes().length) * 100.0);

				// kontrola délky
				if (encoded.length > decoded.length)
					fail();

				System.out.println(new String(decoded));
				// System.out.println(Arrays.equals(encoded, decoded));
				assertTrue(Arrays.equals(encoded, decoded));

			} catch (Exception e) {
				LOG.error("Vyjímka při testování komprese", e);
				fail();
			}
		}
	}
}
