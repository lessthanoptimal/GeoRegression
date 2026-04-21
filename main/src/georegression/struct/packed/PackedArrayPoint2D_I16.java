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

import georegression.struct.point.Point2D_I16;
import lombok.Getter;
import org.ddogleg.struct.DogArray_I16;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;

/**
 * Packed array of {@link Point2D_I16}. Internally the point is stored in an interleaved format.
 *
 * @author Peter Abeles
 */
public class PackedArrayPoint2D_I16 implements PackedArray<Point2D_I16> {
	private static final int DOF = 2;

	/** Stores tuple in a single continuous array */
	@Getter private final DogArray_I16 array;

	// tuple that the result is temporarily written to
	private final Point2D_I16 temp = new Point2D_I16();

	public PackedArrayPoint2D_I16() {
		array = new DogArray_I16();
		array.resize(0);
	}

	@Override public PackedArrayPoint2D_I16 reset() {
		array.reset();
		return this;
	}

	@Override public void resize( int size ) {
		array.resize(size*DOF, (short)0);
	}

	@Override public PackedArrayPoint2D_I16 reserve( int numTuples ) {
		array.reserve(numTuples*2);
		return this;
	}

	@Override public void removeSwap( int index ) {
		int idx0 = index*DOF;
		int idx1 = idx0 + DOF;
		int tail = array.size - DOF;
		for (int j = idx0; j < idx1; j++) {
			array.data[j] = array.data[tail++];
		}
		array.size -= DOF;
	}

	public final void append( int x, int y ) {
		array.add(x);
		array.add(y);
	}

	public void set( int index, int x, int y ) {
		index *= 2;
		array.data[index++] = (short)x;
		array.data[index] = (short)y;
	}

	@Override public void append( Point2D_I16 element ) {
		append(element.x, element.y);
	}

	@Override public void set( int index, Point2D_I16 element ) {
		index *= 2;
		array.data[index++] = element.x;
		array.data[index] = element.y;
	}

	@Override public Point2D_I16 getTemp( int index ) {
		temp.x = array.data[index*2];
		temp.y = array.data[index*2 + 1];

		return temp;
	}

	@Override public void getCopy( int index, Point2D_I16 dst ) {
		dst.x = array.data[index*2];
		dst.y = array.data[index*2 + 1];
	}

	@Override public void copy( Point2D_I16 src, Point2D_I16 dst ) {
		dst.setTo(src);
	}

	@Override public int size() {
		return array.size/2;
	}

	@Override public Class<Point2D_I16> getElementType() {
		return Point2D_I16.class;
	}

	@Override public void forIdx( int idx0, int idx1, ProcessIndex<Point2D_I16> op ) {
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

	@Override public boolean isEquals( PackedArray<Point2D_I16> o ) {
		return this.array.isEquals(((PackedArrayPoint2D_I16)o).array);
	}

	public String format( MatrixPrintFormat format ) {
		var builder = new StringBuilder();
		builder.append(format.prefix);
		for (int i = 0; i < size(); i++) {
			int idxRow = i*DOF;

			builder.append(format.rowPrefix);
			for (int col = 0; col < DOF; col++) {
				short value = array.data[idxRow+col];
				builder.append(value);
				if (col < DOF-1)
					builder.append(format.colSeparator);
			}
			builder.append(format.rowSuffix);
			if (i < size() - 1)
				builder.append(format.rowSeparator);
		}
		builder.append(format.suffix);
		return builder.toString();
	}

	public String format( MapPrintFormat format ) {
		return format(format, new String[]{"x", "y"});
	}

	protected String format( MapPrintFormat format, String[] keys ) {
		if (keys.length != DOF)
			throw new IllegalArgumentException("Number of keys should match DOF");

		var builder = new StringBuilder();
		builder.append(format.listPrefix);
		for (int i = 0; i < size(); i++) {
			int idxRow = i*DOF;

			builder.append(format.itemPrefix);
			for (int col = 0; col < DOF; col++) {
				short value = array.data[idxRow+col];
				builder.append(keys[col]).append(format.valueSeparator);
				builder.append(value);
				if (col < DOF-1)
					builder.append(format.pairSeparator);
			}
			builder.append(format.itemSuffix);
			if (i < size() - 1)
				builder.append(format.itemSeparator);
		}
		builder.append(format.listSuffix);
		return builder.toString();
	}

	/**
	 * Makes this array have a value identical to 'src'
	 *
	 * @param src original array being copies
	 * @return Reference to 'this'
	 */
	public PackedArrayPoint2D_I16 setTo( PackedArrayPoint2D_I16 src ) {
		array.setTo(src.array);
		return this;
	}
}
