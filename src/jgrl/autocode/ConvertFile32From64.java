/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.autocode;

import java.io.*;


/**
 * Converts a file written for 64bit numbers into 32bit numbers by replacing keywords.
 *
 * @author Peter Abeles
 */
public class ConvertFile32From64 {

	InputStream in;
	PrintStream out;

	public ConvertFile32From64(File inputFile) throws FileNotFoundException {
		in = new FileInputStream(inputFile);

		String inputName = inputFile.getAbsolutePath();
		String outputFileName = inputName.substring(0, inputName.length() - 8) + "F32.java";

		out = new PrintStream(outputFileName);
	}

	public void process() throws IOException {
		int n;
		StringBuffer s = new StringBuffer(1024);
		boolean prevChar = false;

		while ((n = in.read()) != -1) {
			if (Character.isWhitespace((char) n)) {
				if (prevChar) {
					handleToken(s.toString());
					s.delete(0, s.length());
					prevChar = false;
				}
				out.write(n);
			} else {
				prevChar = true;
				s.append((char) n);
			}
		}

		if (prevChar) {
			handleToken(s.toString());
		}

		out.close();
		in.close();
	}

	private void handleToken(String s) {
		s = s.replaceAll("double", "float");
		s = s.replaceAll("Double", "Float");
		s = s.replaceAll("_F64", "_F32");
		s = s.replaceAll("DOUBLE_TEST_TOL", "FLOAT_TEST_TOL");
		s = replaceStartString(s, "Math.", "(float)Math.");
		s = replaceStartString(s, "rand.nextGaussian", "(float)rand.nextGaussian");
		s = handleFloats(s);

		out.print(s);
	}

	/**
	 * Looks for a floating point constant number and tacks on a 'f' to the end
	 * to make it into a float and not a double.
	 */
	private String handleFloats(String input) {
		String regex = "\\d+\\.+\\d+";

		return input.replaceAll(regex, "$0f");
	}

	private String replaceStartString(String input, String from, String to) {

		if (input.startsWith(from)) {
			return to + input.substring(from.length());
		} else {
			return input;
		}
	}
}
