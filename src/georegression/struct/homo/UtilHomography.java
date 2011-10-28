package georegression.struct.homo;


import org.ejml.data.DenseMatrix64F;

/**
 * Various useful functions related to homographies.
 *
 * @author Peter Abeles
 */
public class UtilHomography {

	/**
	 * Converts the provided 3x3 matrix into a {@link Homography2D_F64}.
	 *
	 * @param m Input 3x3 matrix.
	 * @param ret Storage for output.  If null then a new instance is created.
	 * @return Equivalent homography.
	 */
	public static Homography2D_F64 convert( DenseMatrix64F m , Homography2D_F64 ret ) {
		if( m.numCols != 3 || m.numRows != 3)
			throw new IllegalArgumentException("Expected a 3 by 3 matrix.");

		if( ret == null )
			ret = new Homography2D_F64();

		ret.a11 = m.unsafe_get(0,0);
		ret.a12 = m.unsafe_get(0,1);
		ret.a13 = m.unsafe_get(0,2);
		ret.a21 = m.unsafe_get(1,0);
		ret.a22 = m.unsafe_get(1,1);
		ret.a23 = m.unsafe_get(1,2);
		ret.a31 = m.unsafe_get(2,0);
		ret.a32 = m.unsafe_get(2,1);
		ret.a33 = m.unsafe_get(2,2);

		return ret;
	}
}
