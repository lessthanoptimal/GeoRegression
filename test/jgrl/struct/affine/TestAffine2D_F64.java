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

package jgrl.struct.affine;

import jgrl.struct.GenericInvertibleTransformTests_F64;
import jgrl.struct.InvertibleTransform;
import jgrl.struct.point.Point2D_F64;
import jgrl.transform.affine.AffinePointOps;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestAffine2D_F64 extends GenericInvertibleTransformTests_F64<Point2D_F64> {

	Random rand = new Random();

	@Override
	public Point2D_F64 createRandomPoint() {
		return new Point2D_F64(rand.nextGaussian()*3,
				rand.nextGaussian()*3);
	}

	@Override
	public Affine2D_F64 createRandomTransform() {

		double a11 = rand.nextGaussian()*3.0f;
		double a12 = rand.nextGaussian()*3.0f;
		double a21 = rand.nextGaussian()*3.0f;
		double a22 = rand.nextGaussian()*3.0f;
		double tx = rand.nextGaussian()*3.0f;
		double ty = rand.nextGaussian()*3.0f;

		return new Affine2D_F64(a11,a12,a21,a22,tx,ty);
	}

	@Override
	public Point2D_F64 apply(InvertibleTransform se, Point2D_F64 point, Point2D_F64 result) {
		return AffinePointOps.transform((Affine2D_F64)se,point,result);
	}
}
