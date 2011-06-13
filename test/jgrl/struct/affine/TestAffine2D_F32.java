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

import jgrl.struct.GenericInvertibleTransformTests_F32;
import jgrl.struct.InvertibleTransform;
import jgrl.struct.point.Point2D_F32;
import jgrl.transform.affine.AffinePointOps;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestAffine2D_F32 extends GenericInvertibleTransformTests_F32<Point2D_F32> {

	Random rand = new Random();

	@Override
	public Point2D_F32 createRandomPoint() {
		return new Point2D_F32( (float) (float)rand.nextGaussian() * 3,
				(float) (float)rand.nextGaussian() * 3 );
	}

	@Override
	public Affine2D_F32 createRandomTransform() {

		float a11 = (float) (float)rand.nextGaussian() * 3.0f;
		float a12 = (float) (float)rand.nextGaussian() * 3.0f;
		float a21 = (float) (float)rand.nextGaussian() * 3.0f;
		float a22 = (float) (float)rand.nextGaussian() * 3.0f;
		float tx = (float) (float)rand.nextGaussian() * 3.0f;
		float ty = (float) (float)rand.nextGaussian() * 3.0f;

		return new Affine2D_F32( a11, a12, a21, a22, tx, ty );
	}

	@Override
	public Point2D_F32 apply( InvertibleTransform se, Point2D_F32 point, Point2D_F32 result ) {
		return AffinePointOps.transform( (Affine2D_F32) se, point, result );
	}
}
