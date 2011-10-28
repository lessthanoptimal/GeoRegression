package georegression.struct.homo;


import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilHomography {

	@Test
	public void convert() {
		DenseMatrix64F a = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);

		Homography2D_F64 h = UtilHomography.convert(a,null);

		assertTrue(h.a11 == a.get(0,0));
		assertTrue(h.a12 == a.get(0,1));
		assertTrue(h.a13 == a.get(0,2));
		assertTrue(h.a21 == a.get(1,0));
		assertTrue(h.a22 == a.get(1,1));
		assertTrue(h.a23 == a.get(1,2));
		assertTrue(h.a31 == a.get(2,0));
		assertTrue(h.a32 == a.get(2,1));
		assertTrue(h.a33 == a.get(2,2));
	}
}
