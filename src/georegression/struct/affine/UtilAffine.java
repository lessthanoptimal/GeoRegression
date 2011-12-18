package georegression.struct.affine;

/**
 * @author Peter Abeles
 */
public class UtilAffine {

	public static Affine2D_F32 convert( Affine2D_F64 m , Affine2D_F32 ret ) {
		if( ret == null )
			ret = new Affine2D_F32();

		ret.a11 = (float)m.a11;
		ret.a12 = (float)m.a12;
		ret.a21 = (float)m.a21;
		ret.a22 = (float)m.a22;
		ret.tx = (float)m.tx;
		ret.ty = (float)m.ty;

		return ret;
	}
}
