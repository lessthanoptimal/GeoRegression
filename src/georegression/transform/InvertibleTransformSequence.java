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

package georegression.transform;

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
public class InvertibleTransformSequence<T extends InvertibleTransform> {

	// the path
	private List<Node<T>> path = new ArrayList<Node<T>>();

	/**
	 * Adds the next transform in the sequence.
	 *
	 * @param forward Does the transform work in the forward or reverse direction.
	 * @param tran	The transform.
	 */
	public void addTransform( boolean forward, T tran ) {
		path.add( new Node<T>( tran, forward ) );
	}

	/**
	 * Clears the sequence of transform.
	 */
	public void clear() {
		path.clear();
	}

	@SuppressWarnings({"unchecked"})
	public void computeTransform( T result ) {

		if( path.size() == 0 )
			return;

		T tmp0 = (T)result.createInstance();
		T tmp1 = (T)result.createInstance();
		T inv = (T)result.createInstance();

		Node<T> n = path.get( 0 );
		T nodeTran = (T)n.tran;

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
			T swap = tmp0;
			tmp0 = tmp1;
			tmp1 = swap;
		}
		result.set(tmp0);
	}

	public List<Node<T>> getPath() {
		return path;
	}

	public static class Node<T extends InvertibleTransform> {
		// the transform
		public T tran;
		// if the transform should be applied in the forward or reverse direction
		public boolean forward;

		public Node( T tran, boolean forward ) {
			this.tran = tran;
			this.forward = forward;
		}
	}
}
