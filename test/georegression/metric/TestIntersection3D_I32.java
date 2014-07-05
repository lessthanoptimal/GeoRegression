package georegression.metric;

import georegression.struct.shapes.Cube3D_I32;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestIntersection3D_I32 {
	@Test
	public void contained_cube_cube() {
		Cube3D_I32 cube = new Cube3D_I32(2,3,4,4,6,8);

		// identical
		assertTrue(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,4,3,5,7)));
		// smaller
		assertTrue(Intersection3D_I32.contained(cube,new Cube3D_I32(3,4,5,3,5,7)));

		// partial x-axis
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(1,3,4,4,6,8)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,4,5,6,8)));
		// partial y-axis
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,4,4,4,6,8)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,4,4,7,8)));
		// partial z-axis
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,5,4,6,8)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,4,4,6,9)));
	}

	@Test
	public void intersect_cube_cube() {
		Cube3D_I32 cube = new Cube3D_I32(2,3,4,4,6,8);

		// identical
		assertTrue(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,4,4,6,8)));
		// outside
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(10,10,10,12,12,12)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(10,3,4, 12,6,8)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,10,4, 4,12,8)));
		assertFalse(Intersection3D_I32.contained(cube,new Cube3D_I32(2,3,10, 4,6,12)));

		// assume the 1D tests are sufficient.  the above tests do check to see if each axis is handled
		// individually
	}

	@Test
	public void intersect_1d() {
		// identical
		assertTrue(Intersection3D_I32.intersect(0,0,1,1));
		// bigger
		assertTrue(Intersection3D_I32.intersect(0,-1,1,2));
		assertTrue(Intersection3D_I32.intersect(-1,0,2,1));
		// shifted
		assertTrue(Intersection3D_I32.intersect(0,1,2,3));
		assertTrue(Intersection3D_I32.intersect(1,0,3,2));
		assertTrue(Intersection3D_I32.intersect(0,-1,2,1));
		assertTrue(Intersection3D_I32.intersect(-1,0,1,2));
		// graze
		assertFalse(Intersection3D_I32.intersect(0,1,1,2));
		assertFalse(Intersection3D_I32.intersect(1,0,2,1));
		// outside
		assertFalse(Intersection3D_I32.intersect(0,2,1,3));
	}
}
