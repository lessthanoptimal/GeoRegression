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

package georegression.fitting.affine;

import georegression.struct.affine.Affine2D_F64;
import org.ddogleg.fitting.modelset.ModelManager;

/**
 * Manages {@link Affine2D_F64}.
 *
 * @author Peter Abeles
 */
public class ModelManagerAffine2D_F64 implements ModelManager<Affine2D_F64> {
	@Override
	public Affine2D_F64 createModelInstance() {
		return new Affine2D_F64();
	}

	@Override
	public void copyModel(Affine2D_F64 src, Affine2D_F64 dst) {
		dst.set(src);
	}
}
