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

package georegression.geometry;

import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cube3D_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint3D_F64 {


	public static double distance( double x0 , double y0 , double z0 ,
								   double x1 , double y1 , double z1) {
		return norm(x1-x0,y1-y0,z1-z0);
	}

	public static double distanceSq( double x0 , double y0 , double z0 ,
								   double x1 , double y1 , double z1) {
		double dx = x1-x0;
		double dy = y1-y0;
		double dz = z1-z0;

		return dx*dx + dy*dy + dz*dz;
	}

	public static double norm( double x , double y , double z ) {
		return Math.sqrt(x*x + y*y + z*z);
	}

	public static List<Point3D_F64> copy( List<Point3D_F64> pts ) {
		List<Point3D_F64> ret = new ArrayList<Point3D_F64>();

		for( Point3D_F64 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point3D_F64> pts, double sigma, Random rand ) {
		for( Point3D_F64 p : pts ) {
			p.x += rand.nextGaussian() * sigma;
			p.y += rand.nextGaussian() * sigma;
			p.z += rand.nextGaussian() * sigma;
		}
	}

	public static List<Point3D_F64> random( double min, double max, int num, Random rand ) {
		List<Point3D_F64> ret = new ArrayList<Point3D_F64>();

		double d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point3D_F64 p = new Point3D_F64();
			p.x = rand.nextDouble() * d + min;
			p.y = rand.nextDouble() * d + min;
			p.z = rand.nextDouble() * d + min;

			ret.add( p );
		}

		return ret;
	}

	/**
	 * Computes the mean of the list of points.
	 *
	 * @param points List of points
	 * @param mean (Optional) storage for the mean.  Can be null
	 * @return Mean
	 */
	public static Point3D_F64 mean( List<Point3D_F64> points , Point3D_F64 mean ) {
      if( mean == null )
         mean = new Point3D_F64();

		double x = 0, y = 0, z = 0;

		for( Point3D_F64 p : points ) {
			x += p.x;
			y += p.y;
			z += p.z;
		}

		mean.x = x / points.size();
		mean.y = y / points.size();
		mean.z = z / points.size();

		return mean;
	}

	/**
	 * Computes the mean of the list of points up to element num.
	 *
	 * @param points List of points
	 * @param num use points up to num, exclusive.
	 * @param mean (Optional) storage for the mean.  Can be null
	 * @return Mean
	 */
	public static Point3D_F64 mean( List<Point3D_F64> points , int num , Point3D_F64 mean ) {
		if( mean == null )
			mean = new Point3D_F64();

		double x = 0, y = 0, z = 0;

		for( int i = 0; i < num; i++ ) {
			Point3D_F64 p = points.get(i);
			x += p.x;
			y += p.y;
			z += p.z;
		}

		mean.x = x / num;
		mean.y = y / num;
		mean.z = z / num;

		return mean;
	}

	/**
	 * Finds the minimal volume {#link Cube3D_F64} which contains all the points.
	 *
	 * @param points Input: List of points.
	 * @param bounding Output: Bounding cube
	 */
	public static void boundingCube(List<Point3D_F64> points , Cube3D_F64 bounding) {
		double minX=Double.MAX_VALUE,maxX=-Double.MAX_VALUE;
		double minY=Double.MAX_VALUE,maxY=-Double.MAX_VALUE;
		double minZ=Double.MAX_VALUE,maxZ=-Double.MAX_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F64 p = points.get(i);
			if( p.x < minX )
				minX = p.x;
			if( p.x > maxX )
				maxX = p.x;
			if( p.y < minY )
				minY = p.y;
			if( p.y > maxY )
				maxY = p.y;
			if( p.z < minZ )
				minZ = p.z;
			if( p.z > maxZ )
				maxZ = p.z;
		}

		bounding.p0.set(minX,minY,minZ);
		bounding.p1.set(maxX, maxY, maxZ);
	}
}
