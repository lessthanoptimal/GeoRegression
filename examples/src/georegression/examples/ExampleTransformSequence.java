/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

package georegression.examples;

import georegression.struct.se.Se2_F64;
import georegression.transform.InvertibleTransformSequence;

/**
 * Demonstrates how to easily compute the result of applying multiple transforms in a sequence
 *
 * @author Peter Abeles
 */
public class ExampleTransformSequence {

	public static void main(String[] args) {
		InvertibleTransformSequence<Se2_F64> sequence = new InvertibleTransformSequence<Se2_F64>();

		// add a few easy to understand transforms
		sequence.addTransform(true,new Se2_F64(2,0,0));
		sequence.addTransform(true,new Se2_F64(3,0,0));
		sequence.addTransform(false,new Se2_F64(0,4,0));

		Se2_F64 result = new Se2_F64();
		sequence.computeTransform(result);

		// should be (x=5,y=-4,yaw=0)
		result.print();
	}
}
