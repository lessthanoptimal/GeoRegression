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

package georegression.fitting.ellipse;

import georegression.struct.point.Vector2D_F32;
import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

/**
 * Computes a containment ellipse given a covariance
 *
 * @author Peter Abeles
 */
public class CovarianceToEllipse_F32 {

	EigenDecomposition<DenseMatrix64F> eigen = DecompositionFactory.eig(2, true);
	DenseMatrix64F Q = new DenseMatrix64F(2,2);

	// major axis
	Vector2D_F32 x = new Vector2D_F32();
	// minor axis
	Vector2D_F32 y = new Vector2D_F32();

	// major and minor axis length (in that order)
	float lengthX,lengthY;

	// number of standard deviations the ellipse should be
	float numStdev = 1;

	public void setNumStdev(float numStdev) {
		this.numStdev = numStdev;
	}

	/**
	 * Specifies the covariance matrix. Q = [a11 a12; a12 a22]
	 * @param a11 element in covariance matrix.
	 * @param a12 element in covariance matrix.
	 * @param a22  element in covariance matrix.
	 * @return true if it was successful or false if something went wrong
	 */
	public boolean setCovariance( float a11 , float a12, float a22 ) {
		Q.data[0] = a11;
		Q.data[1] = a12;
		Q.data[2] = a12;
		Q.data[3] = a22;

		if( !eigen.decompose(Q) ) {
			System.err.println("Eigenvalue decomposition failed!");
			return false;
		}

		Complex64F v0 = eigen.getEigenvalue(0);
		Complex64F v1 = eigen.getEigenvalue(1);

		DenseMatrix64F a0,a1;

		if( v0.getMagnitude2() > v1.getMagnitude2() ) {
			a0 = eigen.getEigenVector(0);
			a1 = eigen.getEigenVector(1);
			lengthX = (float) v0.getMagnitude();
			lengthY = (float) v1.getMagnitude();
		} else {
			a0 = eigen.getEigenVector(1);
			a1 = eigen.getEigenVector(0);
			lengthX = (float) v1.getMagnitude();
			lengthY = (float) v0.getMagnitude();
		}

		if( a0 == null || a1 == null ) {
			System.err.println("Complex eigenvalues: "+v0+"  "+v1);
			return false;
		}

		lengthX = (float)Math.sqrt(lengthX);
		lengthY = (float)Math.sqrt(lengthY);

		x.set( (float) a0.get(0) , (float) a0.get(1));
		y.set( (float) a1.get(0) , (float) a1.get(1));

		return true;
	}

	/**
	 * Vector which defines the major axis
	 */
	public Vector2D_F32 getMajorVector() {
		return x;
	}

	/**
	 * Vector which defines the minor axis
	 */
	public Vector2D_F32 getMinorVector() {
		return y;
	}

	/**
	 * Angle between the major axis and the x-axis
	 */
	public float getAngle() {
		return (float)Math.atan2( x.y, x.x );
	}

	/**
	 * Length of the major axis
	 */
	public float getMajorAxis() {
		return numStdev*lengthX;
	}

	/**
	 * Length of the minor axis
	 */
	public float getMinorAxis() {
		return numStdev*lengthY;
	}
}
