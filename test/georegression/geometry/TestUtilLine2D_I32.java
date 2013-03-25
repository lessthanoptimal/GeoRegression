package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineSegment2D_I32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilLine2D_I32 {

	@Test
	public void acuteAngle() {
		LineSegment2D_I32 line0 = new LineSegment2D_I32(0,0,1,0);
		LineSegment2D_I32 line1 = new LineSegment2D_I32(0,0,0,1);

		assertEquals(Math.PI/2,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,3,2);
		line1.set(3,2,2,2);

		assertEquals(Math.PI, UtilLine2D_I32.acuteAngle(line0, line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,1,2);
		line1.set(3,2,4,2);

		assertEquals(Math.PI,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);

		line0.set(2,2,3,2);
		line1.set(3,2,4,2);

		assertEquals(0,UtilLine2D_I32.acuteAngle(line0,line1), GrlConstants.DOUBLE_TEST_TOL);
	}

}
