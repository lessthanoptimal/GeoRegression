/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

import georegression.struct.line.LinePolar2D_F32;
import org.ddogleg.fitting.modelset.ModelManager;

/**
 * Manages {@link georegression.struct.line.LinePolar2D_F32}.
 *
 * @author Peter Abeles
 */
public class ModelManagerLinePolar2D_F32 implements ModelManager<LinePolar2D_F32> {
	@Override
	public LinePolar2D_F32 createModelInstance() {
		return new LinePolar2D_F32();
	}

	@Override
	public void copyModel(LinePolar2D_F32 src, LinePolar2D_F32 dst) {
		dst.angle = src.angle;
		dst.distance = src.distance;
	}
}
