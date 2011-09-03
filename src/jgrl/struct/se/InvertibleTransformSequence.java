/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.se;

import jgrl.struct.InvertibleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Given a sequence of {@link jgrl.struct.InvertibleTransform} transforms it will compute a single
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
