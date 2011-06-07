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

package jgrl.struct.se;

import jgrl.geometry.GeometryMath;
import jgrl.geometry.RotationMatrixGenerator;
import jgrl.struct.point.Point2D_F64;
import jgrl.struct.point.Point3D_F64;
import jgrl.transform.se.SePointOps;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestSpecialEuclideanOps {
    @Test
    public void toHomogeneous_3D() {
        Se3 se = SpecialEuclideanOps.setEulerXYZ(0.1,2,-0.3,2,-3,4.4,null);

        DenseMatrix64F H = SpecialEuclideanOps.toHomogeneous(se,null);

        assertEquals(4,H.numCols);
        assertEquals(4,H.numRows);

        DenseMatrix64F R = se.getR();

        for( int i = 0; i < 3; i++ ) {
            for( int j = 0; j < 3; j++ ) {
                assertTrue(R.get(i,j) == H.get(i,j));
            }
            assertTrue(0==H.get(3,i));
        }

        assertTrue(se.getX()==H.get(0,3));
        assertTrue(se.getY()==H.get(1,3));
        assertTrue(se.getZ()==H.get(2,3));
    }

    @Test
    public void toHomogeneous_2D() {
        Point2D_F64 pt = new Point2D_F64(3.4,-9.21);
        Se2 se = new Se2(-3,6.9,-1.3);

        DenseMatrix64F H = SpecialEuclideanOps.toHomogeneous(se,null);

        Point2D_F64 expected = SePointOps.transform(se,pt,null);

        // convert the point into homogeneous matrix notation
        DenseMatrix64F pt_m = new DenseMatrix64F(3,1);
        pt_m.set(0,0,pt.x);
        pt_m.set(1,0,pt.y);
        pt_m.set(2,0,1);

        DenseMatrix64F found = new DenseMatrix64F(3,1);
        CommonOps.mult(H,pt_m,found);

        assertEquals(expected.x,found.get(0,0),1e-8);
        assertEquals(expected.y,found.get(1,0),1e-8);
        assertEquals(1,found.get(2,0),1e-8);
    }

    @Test
    public void toSe3() {
        Se3 se = SpecialEuclideanOps.setEulerXYZ(0.1,2,-0.3,2,-3,4.4,null);

        DenseMatrix64F H = SpecialEuclideanOps.toHomogeneous(se,null);

        Se3 found = SpecialEuclideanOps.toSe3(H,null);

        assertEquals(se.getX(),found.getX(),1e-8);
        assertEquals(se.getY(),found.getY(),1e-8);
        assertEquals(se.getZ(),found.getZ(),1e-8);

        assertTrue(MatrixFeatures.isIdentical(se.getR(),found.getR(),1e-8));
    }

    @Test
    public void toSe2() {
        Se2 se = new Se2(-3,6.9,-1.3);

        DenseMatrix64F H = SpecialEuclideanOps.toHomogeneous(se,null);

        Se2 found = SpecialEuclideanOps.toSe2(H,null);

        assertEquals(se.getX(),found.getX(),1e-8);
        assertEquals(se.getY(),found.getY(),1e-8);
        assertEquals(se.getCosineYaw(),found.getCosineYaw(),1e-8);
        assertEquals(se.getSineYaw(),found.getSineYaw(),1e-8);
    }

    @Test
    public void setEulerXYZ() {
        Point3D_F64 orig = new Point3D_F64(1,2,3);

        Se3 se = SpecialEuclideanOps.setEulerXYZ(0.1,2,-0.3, 2,-3,4.4,null);

        Point3D_F64 expected = SePointOps.transform(se,orig,null);

        DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ(0.1,2,-0.3,se.getR());

        Point3D_F64 found = GeometryMath.mult(R,orig,(Point3D_F64)null);
        found.x += 2;
        found.y += -3;
        found.z += 4.4;

        assertTrue(found.isIdentical(expected,1e-8));
    }
}
