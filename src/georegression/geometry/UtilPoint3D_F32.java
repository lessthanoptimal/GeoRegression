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

	public static Point3D_F32 mean( List<Point3D_F32> points ) {
		Point3D_F32 mean = new Point3D_F32();

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

	public static Point3D_F32 mean( List<Point3D_F32> points , int num ) {
		Point3D_F32 mean = new Point3D_F32();

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

		bounding.p.set(minX,minY,minZ);
		bounding.lengthX = maxX-minX;
		bounding.lengthY = maxY-minY;
		bounding.lengthZ = maxZ-minZ;
	}
}
