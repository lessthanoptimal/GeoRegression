/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.misc.autocode;

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

	public Generate32From64App( String sourceCodeRoot ) {
		rootDirectory = new File( sourceCodeRoot );

		if( !rootDirectory.isDirectory() ) {
			throw new IllegalArgumentException( "Must specify a directory" );
		}
	}


	public void process() {
		processDirectory( rootDirectory );
	}

	private void processDirectory( File directory ) {
		System.out.println( "---- Directory " + directory );

		// examine all the files in the directory first
		File[] files = directory.listFiles();

		for( File f : files ) {
			if( f.getName().endsWith( "_F64.java" ) ) {
				processFile( f );
			}
		}

		for( File f : files ) {
			if( f.isDirectory() && !f.isHidden() ) {
				processDirectory( f );
			}
		}
	}

	private void processFile( File f ) {
		try {
			System.out.println( "Examining " + f.getName() );
			ConvertFile32From64 convert = new ConvertFile32From64( f );
			convert.process();
		} catch( FileNotFoundException e ) {
			throw new RuntimeException( e );
		} catch( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	public static void main( String args[] ) {
		Generate32From64App app = new Generate32From64App( "src" );

		app.process();

		app = new Generate32From64App( "test" );

		app.process();
	}
}
