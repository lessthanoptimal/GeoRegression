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

import jgrl.geometry.GeometryMath_F64;
import jgrl.struct.point.Vector3D_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;


/**
 * A coordinate system transform composed of a rotation and translation.  This transform is
 * a rigid body transform and is a member of the special euclidean group.
 *
 * @author Peter Abeles
 */
public class Se3_F64 implements SpecialEuclidean<Se3_F64> {
	// rotation matrix
	private DenseMatrix64F R;
	// translation vector
	private Vector3D_F64 T;

	/**
	 * Creates a new transform that does nothing.
	 */
	public Se3_F64() {
		R = CommonOps.identity(3);
		T = new Vector3D_F64();
	}

	/**
	 * Initializes the transform with the provided rotation and translation.
	 *
	 * @param R Rotation matrix.
	 * @param T Translation.
	 */
	public Se3_F64(DenseMatrix64F R, Vector3D_F64 T) {
		this(R, T, false);
	}

	/**
	 * Initializes the Se3_F64 with the rotation matrix and translation vector.  If assign
	 * is true the reference to the provided parameters is saved, otherwise a copy is made.
	 *
	 * @param R	  Rotation matrix.
	 * @param T	  Translation.
	 * @param assign If a reference is saved (true) or a copy made (false).
	 */
	public Se3_F64(DenseMatrix64F R, Vector3D_F64 T, boolean assign) {
		if (assign) {
			this.R = R;
			this.T = T;
		} else {
			this.R = R.copy();
			this.T = T.copy();
		}
	}

	/**
	 * Set's 'this' Se3_F64 to be identical to the provided transform.
	 *
	 * @param se The transform that is being copied.
	 */
	public void set(Se3_F64 se) {
		R.set(se.getR());
		T.set(se.getT());
	}

	public void setRotation(DenseMatrix64F R) {
		this.R.set(R);
	}

	public void setTranslation(Vector3D_F64 T) {
		this.T.set(T);
	}

	public void setTranslation(double x, double y, double z) {
		this.T.set(x, y, z);
	}

	public DenseMatrix64F getR() {
		return R;
	}

	public Vector3D_F64 getT() {
		return T;
	}

	public double getX() {
		return T.getX();
	}

	public double getY() {
		return T.getY();
	}

	public double getZ() {
		return T.getZ();
	}

	@Override
	public int getDimension() {
		return 3;
	}

	@Override
	public Se3_F64 createInstance() {
		return new Se3_F64();
	}

	@Override
	public Se3_F64 concat(Se3_F64 second, Se3_F64 result) {
		if (result == null)
			result = new Se3_F64();

		CommonOps.mult(second.getR(), getR(), result.getR());
		GeometryMath_F64.mult(second.getR(), getT(), result.getT());
		GeometryMath_F64.add(second.getT(), result.getT(), result.getT());

		return result;
	}

	@Override
	public Se3_F64 invert(Se3_F64 inverse) {

		if (inverse == null)
			inverse = new Se3_F64();

		// To derive the inverse transform solve for P
		// R*P+T = P'
		// P = R^T*P' - R^T*T

		// -R^T*T
		GeometryMath_F64.multTran(R, T, inverse.T);
		GeometryMath_F64.changeSign(inverse.T);

		// R^T
		CommonOps.transpose(R, inverse.R);

		return inverse;
	}

	@Override
	public void reset() {
		CommonOps.setIdentity(R);
		T.set(0, 0, 0);
	}

	public Se3_F64 copy() {
		Se3_F64 ret = new Se3_F64();
		ret.set(this);

		return ret;
	}

	public void print() {
		System.out.println("SpecialEuclidean:");
		R.print();
		T.print();
		System.out.println();
	}
}
