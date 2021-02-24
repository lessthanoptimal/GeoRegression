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

package georegression.geometry.lines;

import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.linsol.svd.SolveNullSpaceSvd_DDRM;
import org.ejml.interfaces.SolveNullSpace;

import java.util.List;

/**
 * Finds the intersection of {@link LineGeneral2D_F64} by minimizing algebraic error. The output point
 * will be in homogenous coordinates and coule be at infinity.
 *
 * @author Peter Abeles
 */
public class IntersectionLinesGeneral_F64 {
	/** Linear solver used internally. Swap out if speed is needed more than accuracy */
	public SolveNullSpace<DMatrixRMaj> solver = new SolveNullSpaceSvd_DDRM();

	// Storage for stacked coefficients from the lines
	protected final DMatrixRMaj A = new DMatrixRMaj(1,1);
	// Storage for null space
	protected final DMatrixRMaj x = new DMatrixRMaj(3,1);

	/**
	 * Solves for the best fit point of intersection
	 *
	 * @param lines (Input) Set of lines
	 * @param intersection (Output) Found 2D homogenous coordinate
	 * @return true if nothing went horribly wrong
	 */
	public boolean process(List<LineGeneral2D_F64> lines, Point3D_F64 intersection) {
		if (lines.size()<2)
			throw new IllegalArgumentException("At least two lines are needed");

		// Stack the line coefficients
		A.reshape(lines.size(),3);
		for (int row = 0; row < lines.size(); row++) {
			LineGeneral2D_F64 l = lines.get(row);

			A.data[row*3]   = l.A;
			A.data[row*3+1] = l.B;
			A.data[row*3+2] = l.C;
		}

		// Solve for the null space
		if (!solver.process(A,1,x)) {
			return false;
		}

		// REturn the point
		intersection.setTo(x.get(0),x.get(1),x.get(2));

		return true;
	}
}
