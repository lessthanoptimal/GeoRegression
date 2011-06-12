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

import jgrl.struct.GeoTuple2D_F32;
import jgrl.struct.point.Vector2D_F32;

/**
 * <p>
 * Special Euclidean transform that has been parameterized with three parameters:
 * translation (x,y) and rotation (yaw).  First the rotation is applied then the translation.
 * </p>
 * <p/>
 * <p>
 * Note that this data structure does not specify the units or coordinate frames.
 * </p>
 *
 * @author Peter Abeles
 */
public class Se2_F32 implements SpecialEuclidean<Se2_F32> {

	// the translational component
	private Vector2D_F32 tran = new Vector2D_F32();

	// rotational component parameterized for speed
	private float c; // cos(yaw)
	private float s; // sin(yaw)

	public Se2_F32(GeoTuple2D_F32 tran, float yaw) {
		this(tran.getX(), tran.getY(), yaw);
	}

	public Se2_F32(float x, float y, float yaw) {
		set(x, y, yaw);
	}

	public Se2_F32(float x, float y, float cosYaw, float sinYaw) {
		set(x, y, cosYaw, sinYaw);
	}

	public Se2_F32() {
	}

	public void set(float x, float y, float yaw) {
		this.tran.set(x, y);
		this.c = (float) Math.cos(yaw);
		this.s = (float) Math.sin(yaw);
	}

	public void set(float x, float y, float cosYaw, float sinYaw) {
		this.tran.set(x, y);
		this.c = cosYaw;
		this.s = sinYaw;
	}

	public void set(Se2_F32 target) {
		this.tran.set(target.tran);
		this.c = target.c;
		this.s = target.s;
	}

	public float getX() {
		return tran.getX();
	}

	public void setX(float x) {
		tran.setX(x);
	}

	public float getY() {
		return tran.getY();
	}

	public void setY(float y) {
		tran.setY(y);
	}

	public Vector2D_F32 getTranslation() {
		return tran;
	}

	public void setTranslation(Vector2D_F32 tran) {
		this.tran = tran;
	}

	public void setTranslation(float x, float y) {
		this.tran.set(x, y);
	}

	public float getYaw() {
		return (float) Math.atan2(s, c);
	}

	public void setYaw(float yaw) {
		this.c = (float) Math.cos(yaw);
		this.s = (float) Math.sin(yaw);
	}

	public float getCosineYaw() {
		return c;
	}

	public float getSineYaw() {
		return s;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Se2_F32 createInstance() {
		return new Se2_F32();
	}

	@Override
	public Se2_F32 concat(Se2_F32 second, Se2_F32 result) {
		if (result == null)
			result = new Se2_F32();

		result.setYaw(getYaw() + second.getYaw());

		result.tran.x = second.tran.x + second.c * tran.x - second.s * tran.y;
		result.tran.y = second.tran.y + second.s * tran.x + second.c * tran.y;

		return result;
	}

	@Override
	public Se2_F32 invert(Se2_F32 inverse) {
		if (inverse == null)
			inverse = new Se2_F32();

		float x = -tran.x;
		float y = -tran.y;

		inverse.s = -s;
		inverse.c = c;
		inverse.tran.x = c * x + s * y;
		inverse.tran.y = -s * x + c * y;

		return inverse;
	}

	@Override
	public void reset() {
		c = 1;
		s = 0;
		tran.set(0, 0);
	}

	public Se2_F32 copy() {
		return new Se2_F32(tran.x, tran.y, c, s);
	}

	public String toString() {
		return "Se2( x = " + tran.x + " y = " + tran.y + " yaw = " + getYaw() + " )";
	}
}
