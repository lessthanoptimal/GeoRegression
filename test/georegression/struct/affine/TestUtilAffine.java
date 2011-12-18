package georegression.struct.affine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilAffine {

	@Test
	public void convert_F64_F32() {
		Affine2D_F64 a = new Affine2D_F64(1,2,3,4,5,6);
		Affine2D_F32 b = UtilAffine.convert(a,null);

		assertEquals(a.a11,b.a11,1e-4);
		assertEquals(a.a12,b.a12,1e-4);
		assertEquals(a.a21,b.a21,1e-4);
		assertEquals(a.a22,b.a22,1e-4);
		assertEquals(a.tx,b.tx,1e-4);
		assertEquals(a.ty,b.ty,1e-4);
	}
}
