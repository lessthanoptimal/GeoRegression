/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

import jgrl.struct.GeoTuple2D_F64;

/**
 * A vector in 2D composed of double
 */
@SuppressWarnings({"unchecked"})
public class Vector2D_F64 extends GeoTuple2D_F64<Vector2D_F64> {

    public Vector2D_F64( double x , double y ) {
        set(x,y);
    }

    public Vector2D_F64(){}

    public Vector2D_F64( Vector2D_F64 pt ) {
        set( pt.x , pt.y );
    }

    @Override
    public Vector2D_F64 createNewInstance() {
        return new Vector2D_F64();
    }

    public void set( Vector2D_F64 orig ) {
        _set(orig);
    }

    public Vector2D_F64 copy() {
        return new Vector2D_F64(this);
    }

    public String toString() {
        return "V( "+x+" "+y+" )";
    }
}