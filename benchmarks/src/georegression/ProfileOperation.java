/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression;

/**
 * @author Peter Abeles
 */
public class ProfileOperation {

    /**
     * See how long it takes to run the process 'num' times and print the results
     * to standard out
     *
     */
    public static void profile( Performer performer , int num ) {

        long deltaTime = measureTime(performer,num);

        System.out.printf("%30s time = %8d ms per frame = %8.3f\n",
                performer.getName(),deltaTime,(deltaTime/(double)num));
//        System.out.println(performer.getClass().getSimpleName()+
//                " time = "+deltaTime+"  ms per frame "+(deltaTime/(double)num));
    }

	public static void printOpsPerSec( Performer performer , long minTestTime )
	{
		try {
			double opsPerSecond = profileOpsPerSec(performer,minTestTime, false);

			String name = performer.getName() == null ? performer.getClass().getSimpleName() : performer.getName();
			System.out.printf("%30s  ops/sec = %7.3f\n",name,opsPerSecond);
		} catch( RuntimeException e ) {
			e.printStackTrace();
			System.out.printf("%30s  FAILED\n",performer.getClass().getSimpleName());
		}
	}

	public static double profileOpsPerSec(Performer performer, long minTestTime, boolean warmUp)
	{
		if( warmUp )
			performer.process();

		int N = 1;
		long elapsedTime;
		while( true ) {
			elapsedTime = measureTime(performer,N);
			if(elapsedTime >= minTestTime)
				break;
			N = N*2;
		}

		return (double)N/(elapsedTime/1000.0);
	}

	public static long measureTime( Performer performer , int num )
	{
		long startTime = System.nanoTime();
		for( int i = 0; i < num; i++ ) {
			performer.process();
		}
		long stopTime = System.nanoTime();

		return (stopTime-startTime)/1000000L;
	}
}
