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

import georegression.struct.point.Point3D_F32;
import georegression.struct.shapes.Cube3D_F32;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint3D_F32 {


	public static float distance( float x0 , float y0 , float z0 ,
								   float x1 , float y1 , float z1) {
		return norm(x1-x0,y1-y0,z1-z0);
	}

	public static float distanceSq( float x0 , float y0 , float z0 ,
								   float x1 , float y1 , float z1) {
		float dx = x1-x0;
		float dy = y1-y0;
		float dz = z1-z0;

		return dx*dx + dy*dy + dz*dz;
	}

	public static float norm( float x , float y , float z ) {
		return (float)Math.sqrt(x*x + y*y + z*z);
	}

	public static List<Point3D_F32> copy( List<Point3D_F32> pts ) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		for( Point3D_F32 p : pts ) {
			ret.add( p.copy() );
		}

		return ret;
	}

	public static void noiseNormal( List<Point3D_F32> pts, float sigma, Random rand ) {
		for( Point3D_F32 p : pts ) {
			p.x += (float)rand.nextGaussian() * sigma;
			p.y += (float)rand.nextGaussian() * sigma;
			p.z += (float)rand.nextGaussian() * sigma;
		}
	}

	public static List<Point3D_F32> random( float min, float max, int num, Random rand ) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		float d = max - min;

		for( int i = 0; i < num; i++ ) {
			Point3D_F32 p = new Point3D_F32();
			p.x = rand.nextFloat() * d + min;
			p.y = rand.nextFloat() * d + min;
			p.z = rand.nextFloat() * d + min;

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
	public static Point3D_F32 mean( List<Point3D_F32> points , Point3D_F32 mean ) {
      if( mean == null )
         mean = new Point3D_F32();

		float x = 0, y = 0, z = 0;

		for( Point3D_F32 p : points ) {
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
	public static Point3D_F32 mean( List<Point3D_F32> points , int num , Point3D_F32 mean ) {
		if( mean == null )
			mean = new Point3D_F32();

		float x = 0, y = 0, z = 0;

		for( int i = 0; i < num; i++ ) {
			Point3D_F32 p = points.get(i);
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
	 * Finds the minimal volume {#link Cube3D_F32} which contains all the points.
	 *
	 * @param points Input: List of points.
	 * @param bounding Output: Bounding cube
	 */
	public static void boundingCube(List<Point3D_F32> points , Cube3D_F32 bounding) {
		float minX=Float.MAX_VALUE,maxX=-Float.MAX_VALUE;
		float minY=Float.MAX_VALUE,maxY=-Float.MAX_VALUE;
		float minZ=Float.MAX_VALUE,maxZ=-Float.MAX_VALUE;

		for( int i = 0; i < points.size(); i++ ) {
			Point3D_F32 p = points.get(i);
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
