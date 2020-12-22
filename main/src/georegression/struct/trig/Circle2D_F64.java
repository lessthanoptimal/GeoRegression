/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.trig;

import georegression.struct.point.Point2D_F64;

/**
 * Describes a circle in 2D space using it's center and radius.
 *
 * @author Peter Abeles
 */
public class Circle2D_F64 {
	/**
	 * Radius of the circle
	 */
	public double radius;
	/**
	 * Center of the circle
	 */
	public Point2D_F64 center = new Point2D_F64();

	public Circle2D_F64(double radius, Point2D_F64 center) {
		this.radius = radius;
		this.center.setTo(center);
	}

	public Circle2D_F64(double radius, double x , double y) {
		this.radius = radius;
		this.center.setTo(x,y);
	}

	public Circle2D_F64() {
	}

	public void setTo( Circle2D_F64 src ) {
		this.radius = src.radius;
		this.center.setTo(src.center);
	}
}
