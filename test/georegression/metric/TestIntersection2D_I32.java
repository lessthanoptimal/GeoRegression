package georegression.metric;

import georegression.struct.shapes.RectangleCorner2D_I32;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestIntersection2D_I32 {
	@Test
	public void intersects_rect_corners() {
		// check several positive cases

		// negative cases

		// edge cases
	}

	private void check( RectangleCorner2D_I32 a , RectangleCorner2D_I32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_I32.intersects(a,b));
	}
}
