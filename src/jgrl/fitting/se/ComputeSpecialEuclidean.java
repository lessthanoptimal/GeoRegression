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

package jgrl.fitting.se;

import jgrl.fitting.MotionTransformPoint;
import jgrl.struct.point.Point2D_F64;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.se.Se2;
import jgrl.struct.se.Se3;

import java.util.List;


/**
 * Functions related to finding a {@link jgrl.struct.se.SpecialEuclidean} transform which
 * when applied to one list of points will minimize the difference between the other set.
 *
 * @author Peter Abeles
 */
public class ComputeSpecialEuclidean {

    public static MotionTransformPoint<Se2,Point2D_F64> fitPoints2D() {
        return new MotionSe2PointSVD();
    }

    public static MotionTransformPoint<Se3, Point3D_F64> fitPoints3D() {
        return new MotionSe3PointCrossCovariance();
    }

    public static Se2 fitPoints2D( List<Point2D_F64> from , List<Point2D_F64> to ) {
        MotionTransformPoint<Se2,Point2D_F64> alg = fitPoints2D();

        alg.process(from,to);

        return alg.getMotion();
    }

    public static Se3 fitPoints3D( List<Point3D_F64> from , List<Point3D_F64> to ) {
        MotionTransformPoint<Se3,Point3D_F64> alg = fitPoints3D();

        alg.process(from,to);

        return alg.getMotion();
    }
}
