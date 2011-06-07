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

import jgrl.struct.GenericInvertibleTransformTests_F64;
import jgrl.struct.InvertibleTransform;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.point.Vector3D_F64;
import jgrl.transform.se.SePointOps;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestSe3 extends GenericInvertibleTransformTests_F64<Point3D_F64> {

    Random rand = new Random(234234);

    /**
     * Checks to see if the constructor correctly saves the reference or copies the values.
     */
    @Test
    public void constructor_assign() {
        DenseMatrix64F R = new DenseMatrix64F(3,3);
        Vector3D_F64 T = new Vector3D_F64(1,2,3);

        Se3 a = new Se3(R,T,false);
        assertTrue(R != a.getR());
        assertTrue(T != a.getT());

        a = new Se3(R,T,true);
        assertTrue(R == a.getR());
        assertTrue(T == a.getT());
    }

    @Override
    public Point3D_F64 createRandomPoint() {
        return new Point3D_F64(rand.nextGaussian()*3,
                rand.nextGaussian()*3,rand.nextGaussian()*3);
    }

    @Override
    public SpecialEuclidean createRandomTransform() {

        double rotX = (rand.nextDouble()-0.5)*2.0*Math.PI;
        double rotY = (rand.nextDouble()-0.5)*2.0*Math.PI;
        double rotZ = (rand.nextDouble()-0.5)*2.0*Math.PI;
        double x = rand.nextGaussian()*2;
        double y = rand.nextGaussian()*2;
        double z = rand.nextGaussian()*2;

        Se3 ret = new Se3();

        SpecialEuclideanOps.setEulerXYZ(rotX,rotY,rotZ,x,y,z,ret);

        return ret;
    }

    @Override
    public Point3D_F64 apply(InvertibleTransform se, Point3D_F64 point, Point3D_F64 result) {
        return SePointOps.transform((Se3)se,(Point3D_F64)point,(Point3D_F64)result);
    }
}
