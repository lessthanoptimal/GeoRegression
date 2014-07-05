package georegression.metric;

import georegression.struct.point.Point3D_I32;
import georegression.struct.shapes.Cube3D_I32;

/**
 * @author Peter Abeles
 */
public class Intersection3D_I32 {
	/**
	 * Returns true if the point is contained inside the cube. The point is considered to be inside the cube
	 * if the following test passes for each dimension.  cube.p0.x <= point.x < cube.p1.x + cube.lengthX
	 *
	 * @param cube Cube
	 * @param point Point which is tested to see if it is inside the cube
	 * @return true for inside and false for not
	 */
	public static boolean contained( Cube3D_I32 cube , Point3D_I32 point ) {

		return( cube.p0.x <= point.x && point.x < cube.p1.x &&
				cube.p0.y <= point.y && point.y < cube.p1.y &&
				cube.p0.z <= point.z && point.z < cube.p1.z );
	}

	/**
	 * Returns true if cubeB is contained inside of or is identical to cubeA.
	 *
	 * @param cubeA Cube
	 * @param cubeB Cube which is being tested to see if it is inside of cubeA
	 * @return true if inside/identical or false if outside
	 */
	public static boolean contained( Cube3D_I32 cubeA , Cube3D_I32 cubeB ) {
		return( cubeA.p0.x <= cubeB.p0.x && cubeA.p1.x >= cubeB.p1.x &&
				cubeA.p0.y <= cubeB.p0.y && cubeA.p1.y >= cubeB.p1.y &&
				cubeA.p0.z <= cubeB.p0.z && cubeA.p1.z >= cubeB.p1.z );
	}

	/**
	 * Returns true if the two cubes intersect each other. p0 is inclusive and p1 is exclusive.
	 * So if the p0 edge and p1 edge overlap perfectly there is no intersection.
	 *
	 * @param cubeA Cube
	 * @param cubeB Cube
	 * @return true for intersection and false if no intersection
	 */
	public static boolean intersect( Cube3D_I32 cubeA , Cube3D_I32 cubeB ) {
		return( intersect(cubeA.p0.x , cubeB.p0.x , cubeA.p1.x , cubeB.p1.x ) &&
				intersect(cubeA.p0.y , cubeB.p0.y , cubeA.p1.y , cubeB.p1.y ) &&
				intersect(cubeA.p0.z , cubeB.p0.z , cubeA.p1.z , cubeB.p1.z ) );
	}

	protected static boolean intersect( int a0 , int b0 , int a1, int b1 ) {
		if( a0 <= b0 ) {
			return b0 < a1;
		} else {
			return a0 < b1;
		}
	}
}
