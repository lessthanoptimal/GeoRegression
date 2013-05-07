package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint2D_F64 {

	Random rand = new Random(234);

	@Test
	public void mean() {
		List<Point2D_F64> list = new ArrayList<Point2D_F64>();

		int X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			X += p.x = rand.nextDouble()*100-50;
			Y += p.y = rand.nextDouble()*100-50;

			list.add(p);
		}

		Point2D_F64 found = UtilPoint2D_F64.mean(list, null);

		assertEquals(X/20,found.x , GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(Y / 20, found.y , GrlConstants.DOUBLE_TEST_TOL);
	}

}
