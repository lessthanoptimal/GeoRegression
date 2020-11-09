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

package georegression.fitting.line;

import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.linsol.qr.SolveNullSpaceQRP_DDRM;
import org.ejml.interfaces.SolveNullSpace;

import java.util.List;

/**
 * Fits a {@link LineParametric2D_F64} to a set of points by minimizing algebraic error. Points are normalized
 * to have zero mean and a stdev of 1 along each axis. Then the slope is found as the nullspace using SVD.
 *
 * @author Peter Abeles
 */
public class FitLineParametricSvd_F64 {

	// normalization parameters
	double mean_x,mean_y;
	double std_x,std_y;

	public SolveNullSpace<DMatrixRMaj> solver = new SolveNullSpaceQRP_DDRM();

	DMatrixRMaj A = new DMatrixRMaj(1,1);
	DMatrixRMaj AA = new DMatrixRMaj(1,1);
	DMatrixRMaj ns = new DMatrixRMaj(2,1);

	/**
	 * Fits a line to the points
	 * @param points Set of points being fit
	 * @param line (Output) best fit line
	 * @return true if successful
	 */
	public boolean fit( List<Point2D_F64> points , LineParametric2D_F64 line ) {
		computeNormalization(points);

		A.reshape(points.size(),2);
		for (int i = 0,idx=0; i < points.size(); i++) {
			Point2D_F64 p = points.get(i);

			// normalize points to have zero mean and std of 1
			A.data[idx++] = (p.x-mean_x)/std_x;
			A.data[idx++] = (p.y-mean_y)/std_y;
		}

		CommonOps_DDRM.multTransA(A,A,AA);

		if( solver.process(AA,1,ns)) {
			line.p.setTo(mean_x,mean_y);
			line.slope.x = -ns.data[1]*std_x;
			line.slope.y =  ns.data[0]*std_y;
			return true;
		} else {
			return false;
		}
	}

	private void computeNormalization( List<Point2D_F64> points ) {
		mean_x = mean_y = 0;
		std_x = std_y = 0;

		for (int i = 0; i < points.size(); i++) {
			Point2D_F64 p = points.get(i);
			mean_x += p.x;
			mean_y += p.y;
		}
		mean_x /= points.size();
		mean_y /= points.size();

		for (int i = 0; i < points.size(); i++) {
			Point2D_F64 p = points.get(i);
			double dx = p.x - mean_x;
			double dy = p.y - mean_y;

			std_x += dx*dx;
			std_y += dy*dy;
		}

		// if std is 0 then all the values are the same. set scale to mean so that they are all equal to 1
		std_x = std_x == 0.0 ? Math.abs(mean_x) : Math.sqrt(std_x/points.size());
		std_y = std_y == 0.0 ? Math.abs(mean_y) : Math.sqrt(std_y/points.size());
	}

}
