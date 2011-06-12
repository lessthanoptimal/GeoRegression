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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Auto generates 32bit code from 64bit code.
 *
 * @author Peter Abeles
 */
public class Generate32From64App {

	// source code root directory
	File rootDirectory;

	public Generate32From64App(String sourceCodeRoot) {
		rootDirectory = new File(sourceCodeRoot);

		if (!rootDirectory.isDirectory()) {
			throw new IllegalArgumentException("Must specify a directory");
		}
	}


	public void process() {
		processDirectory(rootDirectory);
	}

	private void processDirectory(File directory) {
		System.out.println("---- Directory " + directory);

		// examine all the files in the directory first
		File[] files = directory.listFiles();

		for (File f : files) {
			if (f.getName().endsWith("_F64.java")) {
				processFile(f);
			}
		}

		for (File f : files) {
			if (f.isDirectory() && !f.isHidden()) {
				processDirectory(f);
			}
		}
	}

	private void processFile(File f) {
		try {
			System.out.println("Examining " + f.getName());
			ConvertFile32From64 convert = new ConvertFile32From64(f);
			convert.process();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) {
		Generate32From64App app = new Generate32From64App("src");

		app.process();

		app = new Generate32From64App("test");

		app.process();
	}
}
