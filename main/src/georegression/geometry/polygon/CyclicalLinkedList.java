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

package georegression.geometry.polygon;

import org.ddogleg.struct.DogLinkedList;

/**
 * Modified version of the linked list to provide support for being cyclical.
 *
 * WARNING: This does not yet support all functions.
 *
 * @author Peter Abeles
 */
@SuppressWarnings("ConstantConditions")
public class CyclicalLinkedList<T> extends DogLinkedList<T> {
	@Override public Element<T> pushHead( T object ) {
		Element<T> e = requestNew();
		e.object = object;

		if( first == null ) {
			first = last = e;
			e.next = e.prev = e;
		} else {
			e.next = first;
			e.prev = last;
			first.prev = e;
			last.next = e;
			first = e;
		}
		size++;

		return e;
	}

	@Override public Element<T> pushTail( T object ) {
		Element<T> e = requestNew();
		e.object = object;

		if( last == null ) {
			first = last = e;
			e.next = e.prev = e;
		} else {
			e.next = first;
			e.prev = last;
			first.prev = e;
			last.next = e;
			last = e;
		}
		size++;

		return e;
	}

	@Override public void remove( Element<T> e ) {
		if (size==1) {
			first = last = null;
		} else {
			Element<T> p = e.prev;
			Element<T> n = e.next;

			p.next = n;
			n.prev = p;

			if (first == e)
				first = e.next;
			if (last == e)
				last = e.prev;
		}
		size--;
		e.clear();
		available.push(e);
	}
}
