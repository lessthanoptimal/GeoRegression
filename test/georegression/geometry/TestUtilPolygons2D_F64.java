package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.RectangleCorner2D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPolygons2D_F64 {
	@Test
	public void bounding_quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(3,0,2,-3,-2,3,1,5);
		RectangleCorner2D_F64 out = new RectangleCorner2D_F64();

		UtilPolygons2D_F64.bounding(q,out);

		assertEquals(-2,out.x0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-3,out.y0, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 3,out.x1, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals( 5,out.y1, GrlConstants.DOUBLE_TEST_TOL);
	}
}
