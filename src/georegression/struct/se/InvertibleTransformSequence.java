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

package georegression.struct.se;

import georegression.struct.InvertibleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Given a sequence of {@link georegression.struct.InvertibleTransform} transforms it will compute a single
 * equivalent transform.
 * </p>
 *
 * @author Peter Abeles
 */
public class InvertibleTransformSequence {

	// the path
	private List<Node> path = new ArrayList<Node>();

	/**
	 * Adds the next transform in the sequence.
	 *
	 * @param forward Does the transform work in the forward or reverse direction.
	 * @param tran	The transform.
	 */
	public void addTransform( boolean forward, SpecialEuclidean tran ) {
		path.add( new Node( tran, forward ) );
	}

	/**
	 * Clears the sequence of transform.
	 */
	public void clear() {
		path.clear();
	}

	@SuppressWarnings({"unchecked"})
	public void computeTransform( SpecialEuclidean result ) {

		if( path.size() == 0 )
			return;

		InvertibleTransform tmp0 = result.createInstance();
		InvertibleTransform tmp1 = result.createInstance();
		InvertibleTransform inv = result.createInstance();

		InvertibleTransformSequence.Node n = path.get( 0 );
		InvertibleTransform nodeTran = n.tran;

		if( n.forward ) {
			tmp0.set( nodeTran );
		} else {
			nodeTran.invert( tmp0 );
		}

		for( int i = 1; i < path.size(); i++ ) {
			n = path.get( i );
			nodeTran = n.tran;

			if( n.forward ) {
				tmp0.concat( nodeTran, tmp1 );
			} else {
				nodeTran.invert( inv );
				tmp0.concat( inv, tmp1 );
			}
			InvertibleTransform swap = tmp0;
			tmp0 = tmp1;
			tmp1 = swap;
		}
		result.set(tmp0);
	}

	public List<Node> getPath() {
		return path;
	}

	public static class Node {
		// the transform
		public InvertibleTransform tran;
		// if the transform should be applied in the forward or reverse direction
		public boolean forward;

		public Node( InvertibleTransform tran, boolean forward ) {
			this.tran = tran;
			this.forward = forward;
		}
	}
}
