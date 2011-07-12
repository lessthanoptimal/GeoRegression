/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package jgrl.struct.shapes;

/**
 * An axis aligned rectangle in 2D that is specified with integers
 */
public class Rectangle2D_I32 {
	private int tl_x;
	private int tl_y;
	private int width;
	private int height;

	public Rectangle2D_I32( int tl_x, int tl_y, int width, int height ) {
		this.tl_x = tl_x;
		this.tl_y = tl_y;
		this.width = width;
		this.height = height;
	}

	public final int getX() {
		return tl_x;
	}

	public final int getY() {
		return tl_y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}
}
