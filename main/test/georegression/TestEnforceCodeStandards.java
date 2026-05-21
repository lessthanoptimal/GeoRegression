/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Makes sure all Configuration classes are compliant
 *
 * @author Peter Abeles
 */
@SuppressWarnings("StringConcatenationInLoop")
public class TestEnforceCodeStandards {
	public Set<String> ignoreSet = new HashSet<String>();

	public TestEnforceCodeStandards() {
		ignoreSet.add("GeoRegressionVersion");
		ignoreSet.add("EnforceCodeStandards");
	}

	@Test void unitTestsMustExtendGeoRegressionJUnit() {
		// Search for the root directory. This test might be run in a child directory by the test hardness
		var projectRoot = new File(".").getAbsoluteFile();
		while (true) {
			if (new File(projectRoot, "LICENSE-2.0.txt").exists())
				break;
			projectRoot = projectRoot.getParentFile();
		}

		// We only care about one module
		var moduleDirectories = new File[]{new File(projectRoot, "main")};

		int countChecked = 0;
		for (File module : moduleDirectories) {
//			System.out.println("module "+module.getPath());
			File dirTest = new File(module, "test");

			if (!dirTest.exists())
				continue;

			// Find all test files
			var files = new ArrayList<File>();
			try (Stream<Path> stream = Files.find(dirTest.toPath(), Integer.MAX_VALUE,
					( path, attr ) -> path.getFileName().toString().matches("Test[A-Z]\\S*.java"))) {
				stream.forEach(( p ) -> files.add(p.toFile()));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}

			boolean failed = false;
			for (File classFile : files) {
				Path f = dirTest.toPath().relativize(classFile.toPath());
				String classPath = f.toString().replace(File.separatorChar, '.').replace(".java", "");

				// Load the class
				Class<?> c;
				try {
					c = Class.forName(classPath);
				} catch (NoClassDefFoundError e) {
					System.err.println("loading " + classPath);
					e.printStackTrace(System.err);
					fail(e.getMessage());
					return;
				} catch (ClassNotFoundException e) {
					System.err.println("Not Found: " + classPath);
					failed = true;
					continue;
				}

				// Skip over classes that this test doesn't apply to
				if (ignoreSet.contains(c.getSimpleName().replace("Test", "")))
					continue;

				countChecked++;

				// See if it extends GeoRegressionJUnit
				boolean found = false;
				while (c != null) {
					c = c.getSuperclass();
					if (c == GeoRegressionJUnit.class) {
						found = true;
						break;
					}
				}
				if (!found) {
					System.err.println("Does not extend GeoRegressionJUnit");
					System.err.println(classFile.getAbsolutePath() + ":1"); // todo real line number
					failed = true;
				}
			}

			assertFalse(failed, "All tests must extend GeoRegressionJUnit. See stderr.");
		}

		assertTrue(countChecked > 20);
	}
}
