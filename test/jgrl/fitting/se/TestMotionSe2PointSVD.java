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

import jgrl.struct.point.Point2D_F64;
import jgrl.struct.point.UtilPoint2D;
import jgrl.struct.se.Se2;
import jgrl.test.GeometryUnitTest;
import jgrl.transform.se.SePointOps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestMotionSe2PointSVD {

    Random rand = new Random(434324);

    @Test
    public void noiseless() {
        Se2 tran = new Se2(2,-4,0.93);

        List<Point2D_F64> from = UtilPoint2D.random_F64(-10,10,30,rand);
        List<Point2D_F64> to = new ArrayList<Point2D_F64>();
        for( Point2D_F64 p : from ) {
            to.add(SePointOps.transform(tran,p,null));
        }

        MotionSe2PointSVD alg = new MotionSe2PointSVD();

        assertTrue(alg.process(from,to));

        Se2 tranFound = alg.getMotion();

//        tranFound.getTranslation().print();
//        tran.getTranslation().print();

        checkTransform(from, to, tranFound,1e-8);
    }

    public static void checkTransform(List<Point2D_F64> from, List<Point2D_F64> to, Se2 tranFound, double tol ) {
        Point2D_F64 foundPt = new Point2D_F64();
        for( int i = 0; i < from.size(); i++ ) {

            Point2D_F64 p = from.get(i);

            SePointOps.transform(tranFound,p,foundPt);

            GeometryUnitTest.assertEquals(to.get(i),foundPt,tol);
        }
    }
}
