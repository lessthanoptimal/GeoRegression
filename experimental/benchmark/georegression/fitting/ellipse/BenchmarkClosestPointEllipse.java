/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.ellipse;

import georegression.PerformerBase;
import georegression.ProfileOperation;
import georegression.fitting.curves.ClosestPointEllipseAngle_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.point.Point2D_F64;

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
