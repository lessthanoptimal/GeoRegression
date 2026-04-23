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

package georegression.struct.packed;

import georegression.struct.point.Point2D_F64;
import org.ejml.MapPrintFormat;

/**
 * Packed array of {@link Point2D_F64}. Internally the point is stored in an interleaved format.
 *
 * @author Peter Abeles
 */
public class PackedArrayPoint2D_F64 extends PackedArray_F64<Point2D_F64> {
	// tuple that the result is temporarily written to
	private final Point2D_F64 temp = new Point2D_F64();

	public PackedArrayPoint2D_F64() {
		super(2, 0);
	}

	public final void append( double x, double y ) {
		array.add(x);
		array.add(y);
	}

	public void set( int index, double x, double y ) {
		index *= 2;
		array.data[index++] = x;
		array.data[index] = y;
	}

	@Override public void append( Point2D_F64 element ) {
		append(element.x, element.y);
	}

	@Override public void set( int index, Point2D_F64 element ) {
		index *= 2;
		array.data[index++] = element.x;
		array.data[index] = element.y;
	}

	@Override public Point2D_F64 getTemp( int index ) {
		temp.x = array.data[index*2];
		temp.y = array.data[index*2 + 1];

		return temp;
	}

	@Override public void getCopy( int index, Point2D_F64 dst ) {
		final int offset = index*2;
		dst.x = array.data[offset];
		dst.y = array.data[offset + 1];
	}

	@Override public void copy( Point2D_F64 src, Point2D_F64 dst ) {
		dst.setTo(src);
	}

	@Override public Class<Point2D_F64> getElementType() {
		return Point2D_F64.class;
	}

	@Override public void forIdx( int idx0, int idx1, ProcessIndex<Point2D_F64> op ) {
		int pointIndex = idx0;
		idx0 *= DOF;
		idx1 *= DOF;
		for (int i = idx0; i < idx1; i += DOF) {
			temp.x = array.data[i];
			temp.y = array.data[i + 1];
			op.process(pointIndex++, temp);
			array.data[i] = temp.x;
			array.data[i + 1] = temp.y;
		}
	}

	public String format( MapPrintFormat format ) {
		return format(format, new String[]{"x", "y"});
	}

	/**
	 * Makes this array have a value identical to 'src'
	 *
	 * @param src original array being copies
	 * @return Reference to 'this'
	 */
	public PackedArrayPoint2D_F64 setTo( PackedArrayPoint2D_F64 src ) {
		array.setTo(src.array);
		return this;
	}
}
