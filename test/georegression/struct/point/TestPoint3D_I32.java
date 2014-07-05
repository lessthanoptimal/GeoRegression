package georegression.struct.point;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestPoint3D_I32 {

	@Test
	public void getDimension() {
		assertEquals(3,new Point3D_I32().getDimension());
	}

	@Test
	public void set() {
		Point3D_I32 p = new Point3D_I32();
		p.set(1,2,3);

		assertEquals(1,p.x);
		assertEquals(2,p.y);
		assertEquals(3,p.z);
	}

	@Test
	public void isIdentical() {
		Point3D_I32 a = new Point3D_I32(1,2,3);
		Point3D_I32 b = new Point3D_I32(1,2,3);

		assertTrue(a.isIdentical(b));
		assertFalse(a.isIdentical(new Point3D_I32(2,2,3)));
		assertFalse(a.isIdentical(new Point3D_I32(1,3,3)));
		assertFalse(a.isIdentical(new Point3D_I32(1,2,4)));
	}

	@Test
	public void createNewInstance() {
		assertTrue(new Point3D_I32().createNewInstance() instanceof Point3D_I32);
	}

	@Test
	public void copy() {
		Point3D_I32 p = new Point3D_I32(1,2,3).copy();

		assertEquals(1,p.x);
		assertEquals(2,p.y);
		assertEquals(3,p.z);
	}
}
