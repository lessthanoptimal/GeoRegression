/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.struct.so.Quaternion_F64;
import georegression.struct.tuples.GeoTuple3D_F64;
import org.jetbrains.annotations.Nullable;

public class QuaternionMath_F64 {
	/// Multiplication for Hamilton quaternions. For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 multiply( Quaternion_F64 q1, Quaternion_F64 q2,
	                                       @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
		out.x = q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
		out.y = q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x;
		out.z = q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w;

		return out;
	}

	/// Multiplication for Hamilton quaternions. For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 mult(Quaternion_F64 q1, Quaternion_F64 q2,
	                                  @Nullable Quaternion_F64 out) {
		return multiply(q1, q2, out);
	}

	/// Multiplication for Hamilton quaternions with the first input conjugated, i.e. conj(q1)*q2.
	/// For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 multConjA( Quaternion_F64 q1, Quaternion_F64 q2,
	                                        @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.w = q1.w*q2.w + q1.x*q2.x + q1.y*q2.y + q1.z*q2.z;
		out.x = q1.w*q2.x - q1.x*q2.w - q1.y*q2.z + q1.z*q2.y;
		out.y = q1.w*q2.y + q1.x*q2.z - q1.y*q2.w - q1.z*q2.x;
		out.z = q1.w*q2.z - q1.x*q2.y + q1.y*q2.x - q1.z*q2.w;

		return out;
	}

	/// Multiplication for Hamilton quaternions with the second input conjugated, i.e. q1*conj(q2).
	/// For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 multConjB( Quaternion_F64 q1, Quaternion_F64 q2,
	                                        @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.w =  q1.w*q2.w + q1.x*q2.x + q1.y*q2.y + q1.z*q2.z;
		out.x = -q1.w*q2.x + q1.x*q2.w - q1.y*q2.z + q1.z*q2.y;
		out.y = -q1.w*q2.y + q1.x*q2.z + q1.y*q2.w - q1.z*q2.x;
		out.z = -q1.w*q2.z - q1.x*q2.y + q1.y*q2.x + q1.z*q2.w;

		return out;
	}

	/// Multiplication for Hamilton quaternions with both inputs conjugated, i.e. conj(q1)*conj(q2).
	/// Equivalently conj(q2*q1). For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 multConjAB( Quaternion_F64 q1, Quaternion_F64 q2,
	                                         @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.w =  q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
		out.x = -q1.w*q2.x - q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
		out.y = -q1.w*q2.y - q1.x*q2.z - q1.y*q2.w + q1.z*q2.x;
		out.z = -q1.w*q2.z + q1.x*q2.y - q1.y*q2.x - q1.z*q2.w;

		return out;
	}

	/**
	 * Rotates a 3D point/vector by the quaternion: out = q*v*conj(q). This is the quaternion equivalent of
	 * GeometryMath_F64.mult(R, src, dst), i.e. R*v. Assumes 'q' is a unit quaternion. src and dst can be the
	 * same instance.
	 *
	 * @param q Unit quaternion encoding the rotation. Not modified.
	 * @param src (Input) point/vector being rotated.
	 * @param dst (Output) storage for the result. Can be null.
	 * @return The rotated point/vector.
	 */
	public static <T extends GeoTuple3D_F64<T>> T mult( Quaternion_F64 q, T src, @Nullable T dst ) {
		if (dst == null)
			dst = src.createNewInstance();

		final double w = q.w, x = q.x, y = q.y, z = q.z;
		final double vx = src.x, vy = src.y, vz = src.z;

		// t = 2*(u x v), where u = (x,y,z) is the vector part of the quaternion
		final double tx = 2.0*(y*vz - z*vy);
		final double ty = 2.0*(z*vx - x*vz);
		final double tz = 2.0*(x*vy - y*vx);

		// out = v + w*t + u x t
		dst.x = vx + w*tx + (y*tz - z*ty);
		dst.y = vy + w*ty + (z*tx - x*tz);
		dst.z = vz + w*tz + (x*ty - y*tx);

		return dst;
	}

	/**
	 * Rotates a 3D point/vector by the inverse of the quaternion: out = conj(q)*v*q. This is the quaternion
	 * equivalent of GeometryMath_F64.multTran(R, src, dst), i.e. R^T*v. Assumes 'q' is a unit quaternion. src and
	 * dst can be the same instance.
	 *
	 * @param q Unit quaternion encoding the rotation. Not modified.
	 * @param src (Input) point/vector being rotated.
	 * @param dst (Output) storage for the result. Can be null.
	 * @return The rotated point/vector.
	 */
	public static <T extends GeoTuple3D_F64<T>> T multTran( Quaternion_F64 q, T src, @Nullable T dst ) {
		if (dst == null)
			dst = src.createNewInstance();

		final double w = q.w, x = q.x, y = q.y, z = q.z;
		final double vx = src.x, vy = src.y, vz = src.z;

		// inverse rotation uses the conjugate, i.e. -u for the vector part: t = 2*((-u) x v) = -2*(u x v)
		final double tx = -2.0*(y*vz - z*vy);
		final double ty = -2.0*(z*vx - x*vz);
		final double tz = -2.0*(x*vy - y*vx);

		// out = v + w*t + (-u) x t
		dst.x = vx + w*tx - (y*tz - z*ty);
		dst.y = vy + w*ty - (z*tx - x*tz);
		dst.z = vz + w*tz - (x*ty - y*tx);

		return dst;
	}

	/**
	 * Adds a point/vector to a rotated point/vector: out = a + q*b*conj(q). Quaternion equivalent of
	 * GeometryMath_F64.addMult(a, R, b, out), i.e. out = a + R*b. Assumes 'q' is a unit quaternion.
	 * 'out' may be the same instance as 'a' and/or 'b'.
	 *
	 * @param a (Input) point/vector added to the rotated result.
	 * @param q Unit quaternion encoding the rotation. Not modified.
	 * @param b (Input) point/vector being rotated.
	 * @param out (Output) storage for the result. Can be null.
	 * @return out = a + (rotation of b by q)
	 */
	public static <T extends GeoTuple3D_F64<T>> T addMult( T a, Quaternion_F64 q, T b, @Nullable T out ) {
		if (out == null)
			out = a.createNewInstance();

		final double ax = a.x, ay = a.y, az = a.z;
		final double w = q.w, x = q.x, y = q.y, z = q.z;
		final double vx = b.x, vy = b.y, vz = b.z;

		// t = 2*(u x v), where u = (x,y,z) is the vector part of the quaternion
		final double tx = 2.0*(y*vz - z*vy);
		final double ty = 2.0*(z*vx - x*vz);
		final double tz = 2.0*(x*vy - y*vx);

		// out = a + v + w*t + u x t
		out.x = ax + vx + w*tx + (y*tz - z*ty);
		out.y = ay + vy + w*ty + (z*tx - x*tz);
		out.z = az + vz + w*tz + (x*ty - y*tx);

		return out;
	}

	/**
	 * Adds a point/vector to an inverse-rotated point/vector: out = a + conj(q)*b*q. Quaternion equivalent of
	 * GeometryMath_F64.addMultTrans(a, R, b, out), i.e. out = a + R^T*b. Assumes 'q' is a unit quaternion.
	 * 'out' may be the same instance as 'a' and/or 'b'.
	 *
	 * @param a (Input) point/vector added to the rotated result.
	 * @param q Unit quaternion encoding the rotation. Not modified.
	 * @param b (Input) point/vector being rotated by the inverse.
	 * @param out (Output) storage for the result. Can be null.
	 * @return out = a + (inverse rotation of b by q)
	 */
	public static <T extends GeoTuple3D_F64<T>> T addMultTran( T a, Quaternion_F64 q, T b, @Nullable T out ) {
		if (out == null)
			out = a.createNewInstance();

		final double ax = a.x, ay = a.y, az = a.z;
		final double w = q.w, x = q.x, y = q.y, z = q.z;
		final double vx = b.x, vy = b.y, vz = b.z;

		// inverse rotation uses the conjugate, i.e. -u for the vector part: t = -2*(u x v)
		final double tx = -2.0*(y*vz - z*vy);
		final double ty = -2.0*(z*vx - x*vz);
		final double tz = -2.0*(x*vy - y*vx);

		// out = a + v + w*t + (-u) x t
		out.x = ax + vx + w*tx - (y*tz - z*ty);
		out.y = ay + vy + w*ty - (z*tx - x*tz);
		out.z = az + vz + w*tz - (x*ty - y*tx);

		return out;
	}
}
