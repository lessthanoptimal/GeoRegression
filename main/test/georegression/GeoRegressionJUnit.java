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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Adds tests to enforce standards, such as no printing to stdout or stderr, unless it's an error.
 */
public class GeoRegressionJUnit {
	// Always provide a random number generator since it's needed so often
	protected final Random rand = new Random(345);

	// Override output streams to keep log spam to a minimum
	protected final MirrorStream out = new MirrorStream(System.out);
	protected final MirrorStream err = new MirrorStream(System.err);
	protected final PrintStream systemOut = System.out;
	protected final PrintStream systemErr = System.err;

	@BeforeEach
	public void captureStreams() {
		System.setOut(new PrintStream(out));
		System.setErr(new PrintStream(err));
	}

	@AfterEach
	public void revertStreams() {
		assertFalse(out.used, "stdout was written to which is forbidden by default");
		assertFalse(err.used, "stderr was written to which is forbidden by default");
		System.setOut(systemOut);
		System.setErr(systemErr);
	}

	@Test public void setTo() {
		// See if the test is applicable
		Class targetClass = lookupTestClass();
		if (Modifier.isAbstract(targetClass.getModifiers()))
			return;
		try {
			targetClass.getMethod("setTo", targetClass);
		} catch (NoSuchMethodException e) {
			return;
		}

		// Test the function
		checkSetTo(targetClass);
	}

	protected Class lookupTestClass() {
		// if it's a member class it might be a mirroring the actual class structure. Let's test that theory
		if (getClass().isMemberClass()) {
			String parentName = getClass().getCanonicalName();
			String path = parentName.replace("Test", "");
			int lastIndex = path.lastIndexOf('.');
			path = path.substring(0, lastIndex) + "$" + path.substring(lastIndex + 1);
			try {
				return Class.forName(path);
			} catch (Exception e) {
				// Well that failed. Go on to the usual approach
			}
		}

		try {
			String className = getClass().getSimpleName().replace("Test", "");
			return Class.forName(getClass().getPackageName() + "." + className);
		} catch (Exception e) {
			System.err.println("Failed to look up the class this test belongs to. If this is expected then either" +
					"override the unit test which is failing or override lookupTestClass() to give it the correct one");
			throw new RuntimeException(e);
		}
	}

	/// Checks that setTo() copies all fields correctly and returns 'this'
	protected void checkSetTo( Class<?> type ) {
		try {
			// Find setTo(SameType) method
			Method setTo = null;
			for (Method m : type.getMethods()) {
				if (!m.getName().equals("setTo")) continue;
				Class<?>[] params = m.getParameterTypes();
				if (params.length == 1 && params[0].isAssignableFrom(type)) {
					setTo = m;
					break;
				}
			}
			assertNotNull(setTo, "No setTo(" + type.getSimpleName() + ") method found");

			assertNotSame(void.class, setTo.getReturnType(), "setTo() can't return void");
			assertTrue(setTo.getReturnType().isAssignableFrom(type), "Return type must be assignable. Found "+setTo.getReturnType().getSimpleName());

			// Create src with non-default values, and a fresh dst
			Object src = createNotDefault(type);
			Object dst = type.getConstructor().newInstance();

			// Verify src actually differs from dst (i.e. createNotDefault worked)
			for (Field f : type.getFields()) {
				if (Modifier.isStatic(f.getModifiers())) continue;
				assertNotEquals(f.get(src), f.get(dst),
						"createNotDefault did not change field: " + f.getName());
			}

			// Invoke setTo and check it returns 'this'
			Object ret = setTo.invoke(dst, src);
			assertNotNull(ret, "setTo()");
			assertSame(dst, ret, "setTo() must return 'this' for chaining");

			// All fields must now match
			for (Field f : type.getFields()) {
				if (Modifier.isStatic(f.getModifiers())) continue;
				assertEquals(f.get(src), f.get(dst),
						"Field not copied by setTo(): " + f.getName());
			}
		} catch (Exception e) {
			fail("checkSetTo failed: " + e.getMessage());
		}
	}

	/// Creates an instance where all non-static fields differ from their default values
	protected Object createNotDefault( Class<?> type ) throws Exception {
		Object ret = type.getConstructor().newInstance();
		for (Field f : type.getFields()) {
			if (Modifier.isStatic(f.getModifiers())) continue;

			Object fv = f.get(ret);

			if (boolean.class == f.getType()) {
				fv = !((boolean)fv);
			} else if (byte.class == f.getType()) {
				fv = (byte)(((byte)fv) + 1);
			} else if (char.class == f.getType()) {
				fv = (char)(((char)fv) + 1);
			} else if (short.class == f.getType()) {
				fv = ((short)fv) + (short)1;
			} else if (int.class == f.getType()) {
				fv = (((int)fv) + 1);
			} else if (long.class == f.getType()) {
				fv = ((long)fv) + 1;
			} else if (float.class == f.getType()) {
				float before = (float)fv;
				float value = rand.nextFloat();
				while (value == before)
					value = rand.nextFloat();
				fv = value;
			} else if (double.class == f.getType()) {
				double before = (double)fv;
				double value = rand.nextDouble();
				while (value == before)
					value = rand.nextDouble();
				fv = value;
			} else if (f.getType() == String.class) {
				fv = (String)fv + "_modofied";
			} else {
				// if final and it has a setTo(), then create a random instance and call setTo
				try {
					Method m = findCompatibleSetTo(f.getType());
					// f.getType() could be an abstract class. To avoid that ambiguity we create
					// an object based on the specific instance's type
					m.invoke(fv, createNotDefault(fv.getClass()));
				} catch (NoSuchMethodException | SecurityException | InvocationTargetException |
				         IllegalArgumentException | IllegalAccessException ignore) {
				}
			}
			f.set(ret, fv);
		}
		return ret;
	}

	private static Method findCompatibleSetTo( Class<?> type ) throws NoSuchMethodException {
		Method[] methods = type.getMethods();
		for (Method m : methods) {
			if (!m.getName().equals("setTo"))
				continue;
			Class<?>[] params = m.getParameterTypes();
			if (params.length != 1)
				continue;

			if (params[0].isAssignableFrom(type))
				return m;
		}
		throw new NoSuchMethodException();
	}

	public static class MirrorStream extends OutputStream {

		public PrintStream out;
		public boolean used = false;

		public MirrorStream( PrintStream out ) {
			this.out = out;
		}

		@Override public void write( int b ) throws IOException {
			used = true;
			out.write(b);
		}

		@Override public void write( byte[] b, int off, int len ) throws IOException {
			used = true;
			out.write(b, off, len);
		}

		@Override public void flush() throws IOException {
			out.flush();
		}

		@Override public void close() throws IOException {
			out.close();
		}
	}
}
