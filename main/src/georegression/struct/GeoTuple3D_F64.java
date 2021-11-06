/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.struct;

import lombok.Getter;
import lombok.Setter;
import org.ejml.UtilEjml;
import org.ejml.ops.MatrixIO;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Generic Tuple for geometric objects that store (x,y,z)
 *
 * @author Peter Abeles
 */
@Getter @Setter
public abstract class GeoTuple3D_F64<T extends GeoTuple3D_F64> extends GeoTuple_F64<T> {
	public double x;
	public double y;
	public double z;

	protected GeoTuple3D_F64( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected GeoTuple3D_F64() {}

	@Override
	public int getDimension() {
		return 3;
	}

	protected void _setTo( GeoTuple3D_F64 a ) {
		x = a.x;
		y = a.y;
		z = a.z;
	}

	public void setTo( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void zero() {
		setTo(0, 0, 0);
	}

	public boolean isIdentical( double x, double y, double z ) {
		return (this.x == x && this.y == y && this.z == z);
	}

	public boolean isIdentical( double x, double y, double z, double tol ) {
		return (Math.abs(this.x - x) <= tol && Math.abs(this.y - y) <= tol && Math.abs(this.z - z) <= tol);
	}

	@Override
	public boolean isIdentical( GeoTuple3D_F64 t, double tol ) {
		return (Math.abs(this.x - t.x) <= tol && Math.abs(this.y - t.y) <= tol && Math.abs(this.z - t.z) <= tol);
	}

	@Override
	public double getIdx( int index ) {
		switch (index) {
			case 0:
				return x;

			case 1:
				return y;

			case 2:
				return z;

			default:
				throw new IllegalArgumentException("Invalid index");
		}
	}

	@Override
	public void setIdx( int index, double value ) {
		switch (index) {
			case 0:
				x = value;
				break;

			case 1:
				y = value;
				break;

			case 2:
				z = value;
				break;

			default:
				throw new IllegalArgumentException("Invalid index");
		}
	}

	/**
	 * <p>In-place addition</p>
	 *
	 * this.x = this.x + a.x;
	 *
	 * @param a value which is to be added
	 */
	public void plusIP( GeoTuple3D_F64 a ) {
		x += a.x;
		y += a.y;
		z += a.z;
	}

	/**
	 * <p>Addition</p>
	 *
	 * ret.x = this.x + a.x;
	 *
	 * @param a value which is to be added
	 */
	public T plus( GeoTuple3D_F64 a ) {
		T ret = createNewInstance();
		ret.x = x + a.x;
		ret.y = y + a.y;
		ret.z = z + a.z;
		return ret;
	}

	/**
	 * In-place scalar multiplication
	 *
	 * @param scalar value that it is multiplied by
	 */
	public void timesIP( double scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	/**
	 * In-place scalar multiplication
	 *
	 * @param scalar value that it is multiplied by
	 */
	public void scale( double scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	public void divideIP( double scalar ) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
	}

	/**
	 * Scalar multiplication
	 *
	 * @param scalar value which is it multiplied by
	 * @return new matrix which is the original scaled
	 */
	public T times( double scalar ) {
		T ret = createNewInstance();
		ret.x = x*scalar;
		ret.y = y*scalar;
		ret.z = z*scalar;
		return ret;
	}

	@Override
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}

	@Override
	public double normSq() {
		return x*x + y*y + z*z;
	}

	public double distance( double x, double y, double z ) {
		double dx = x - this.x;
		double dy = y - this.y;
		double dz = z - this.z;

		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

	@Override
	public void setTo( T src ) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
	}

	@Override
	public double distance( GeoTuple3D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;

		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

	@Override
	public double distance2( GeoTuple3D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;

		return dx*dx + dy*dy + dz*dz;
	}

	public double distance2( double x, double y, double z ) {
		double dx = x - this.x;
		double dy = y - this.y;
		double dz = z - this.z;

		return dx*dx + dy*dy + dz*dz;
	}

	public void print() {
		System.out.println(this);
	}

	public boolean isNaN() {
		return (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z));
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (this.getClass() != obj.getClass())
			return false;

		var o = (GeoTuple3D_F64)obj;
		return Double.compare(x, o.x) == 0 && Double.compare(y, o.y) == 0 && Double.compare(z, o.z) == 0;
	}

	public String toString( String name ) {
		DecimalFormat format = new DecimalFormat("#");
		String sx = UtilEjml.fancyString(x, format, MatrixIO.DEFAULT_LENGTH, 4);
		String sy = UtilEjml.fancyString(y, format, MatrixIO.DEFAULT_LENGTH, 4);
		String sz = UtilEjml.fancyString(z, format, MatrixIO.DEFAULT_LENGTH, 4);

		return name + "( " + sx + " , " + sy + " , " + sz + " )";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
