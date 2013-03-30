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

package georegression.fitting.ellipse;

import georegression.PerformerBase;
import georegression.ProfileOperation;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Peter Abeles
 */
public class BenchmarkClosestPointEllipse {

	long TIME = 1000;

	Random rand = new Random(234);

	List<EllipseRotated_F64> ellipses = new ArrayList<EllipseRotated_F64>();
	Point2D_F64 where = new Point2D_F64(1,3);

	public class Angle extends PerformerBase {

		ClosestPointEllipseAngle_F64 alg = new ClosestPointEllipseAngle_F64(1e-8,100);

		@Override
		public void process() {
			for( int i = 0; i < ellipses.size(); i++ ){
				alg.setEllipse(ellipses.get(i));
				alg.process(where);
			}
		}
	}

	public void process() {
		for( int i = 0; i < 100; i++ ) {
			EllipseRotated_F64 ellipse = new EllipseRotated_F64();
			ellipse.center.x = (rand.nextDouble()-0.5)*5;
			ellipse.center.y = (rand.nextDouble()-0.5)*5;
			ellipse.b = rand.nextDouble()*4+0.1;
			ellipse.a = ellipse.b + rand.nextDouble()*2;
			ellipse.phi = (rand.nextDouble()-0.5)*Math.PI;

			ellipses.add(ellipse);
		}

		ProfileOperation.printOpsPerSec(new Angle(),TIME);
	}

	public static void main( String args[] ) {
		BenchmarkClosestPointEllipse closest = new BenchmarkClosestPointEllipse();
		closest.process();
	}
}
