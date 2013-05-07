package georegression.metric;

import georegression.geometry.UtilTrig_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.line.LineSegment2D_I32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestDistance2D_I32 {
	@Test
	public void distance_line_segment() {
		// test inside the line
		double found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( 2, 0 ) );
		double expected = (double) UtilTrig_F64.distance(0, 2, 2, 0);
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );

		// test before the first end point
		found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( -5, -5 ) );
		expected = UtilTrig_F64.distance( -2, 0, -5, -5 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );

		// test after the second end point
		found = Distance2D_I32.distance( new LineSegment2D_I32( -2, 0, 3, 5 ), new Point2D_I32( 10, 0 ) );
		expected = UtilTrig_F64.distance( 3, 5, 10, 0 );
		assertEquals( expected, found, GrlConstants.DOUBLE_TEST_TOL );
	}
}
