/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.geometry.polygon;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Rectangle2D_F64;

/**
 * <p>
 * Computes the area of intersection between two convex polygons. Port of code found at [1] and Java version by Lagado.
 * </p>
 *
 * WARNING: No effort has been made to reduce the number of calls to new internally from original code.
 *
 * [1] http://www.cap-lore.com/MathPhys/IP/
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public class AreaIntersectionPolygon2D_F64 {
	static final double gamut = (double)500000000.0;
	static final double mid = gamut / (double)2.0;

	private long ssss;
	private double sclx;
	private double scly;

	/**
	 * Computes the area of the intersection between the two polygons.
	 *
	 * Note: the area result has little more accuracy than a float
	 *  This is true even if the polygon is specified with doubles.
	 *
	 * @param a Polygon A
	 * @param b Polygon B
	 * @return area of area of intersection. Negative if the order (CW vs CCW) do not match.
	 */
	public double computeArea(Polygon2D_F64 a , Polygon2D_F64 b ) {
		ssss = 0;
		sclx = 0;
		scly = 0;
		return inter(a,b);
	}

	//--------------------------------------------------------------------------

	static class Rng {
		int mn; int mx;
		Rng(int mn, int mx) { this.mn = mn; this.mx = mx; }
	}
	static class Vertex { Point2D_I32 ip; Rng rx; Rng ry; int in; }

	//--------------------------------------------------------------------------

	private static void range(Polygon2D_F64 points, Rectangle2D_F64 bbox)
	{
		UtilPolygons2D_F64.bounding(points,bbox);
	}

	private static long area(Point2D_I32 a, Point2D_I32 p, Point2D_I32 q) {
		return (long)p.x * q.y - (long)p.y * q.x +
				(long)a.x * (p.y - q.y) + (long)a.y * (q.x - p.x);
	}

	private static boolean ovl(Rng p, Rng q) {
		return p.mn < q.mx && q.mn < p.mx;
	}

	private void cntrib(int f_x, int f_y, int t_x, int t_y, int w) {
		ssss += (long)w * (t_x - f_x) * (t_y + f_y) / 2;
	}

	private void fit(Polygon2D_F64 x, Vertex[] ix, int fudge, Rectangle2D_F64 B)
	{
		int c = x.size();
		while (c-- > 0) {
			ix[c] = new Vertex();
			ix[c].ip = new Point2D_I32();
			ix[c].ip.x = ((int)((x.get(c).getX() - B.p0.x) * sclx - mid) & ~7)
					| fudge | (c & 1);
			ix[c].ip.y = ((int)((x.get(c).getY() - B.p0.y) * scly - mid) & ~7)
					| fudge;
		}

		ix[0].ip.y += x.size() & 1;
		ix[x.size()] = ix[0];

		c = x.size();
		while (c-- > 0) {
			ix[c].rx = ix[c].ip.x < ix[c + 1].ip.x ?
					new Rng(ix[c].ip.x, ix[c + 1].ip.x) :
					new Rng(ix[c + 1].ip.x, ix[c].ip.x);
			ix[c].ry = ix[c].ip.y < ix[c + 1].ip.y ?
					new Rng(ix[c].ip.y, ix[c + 1].ip.y) :
					new Rng(ix[c + 1].ip.y, ix[c].ip.y);
			ix[c].in = 0;
		}
	}

	private void cross(Vertex a, Vertex b, Vertex c, Vertex d,
		  double a1, double a2, double a3, double a4)
	{
		double r1 = a1 / ((double) a1 + a2);
		double r2 = a3 / ((double) a3 + a4);

		cntrib((int)(a.ip.x + r1 * (b.ip.x - a.ip.x)),
				(int)(a.ip.y + r1 * (b.ip.y - a.ip.y)),
				b.ip.x, b.ip.y, 1);
		cntrib(d.ip.x, d.ip.y,
				(int)(c.ip.x + r2 * (d.ip.x - c.ip.x)),
				(int)(c.ip.y + r2 * (d.ip.y - c.ip.y)),
				1);
		++a.in;
		--c.in;
	}

	private void inness(Vertex[] P, int cP, Vertex[] Q, int cQ)
	{
		int s = 0;
		int c = cQ;
		Point2D_I32 p = P[0].ip;

		while (c-- > 0) {
			if (Q[c].rx.mn < p.x && p.x < Q[c].rx.mx) {
				boolean sgn = 0 < area(p, Q[c].ip, Q[c + 1].ip);
				s += (sgn != Q[c].ip.x < Q[c + 1].ip.x) ? 0 : (sgn ? -1 : 1);
			}
		}
		for (int j = 0; j < cP; ++j) {
			if (s != 0)
				cntrib(P[j].ip.x, P[j].ip.y,
						P[j + 1].ip.x, P[j + 1].ip.y, s);
			s += P[j].in;
		}
	}

	//-------------------------------------------------------------------------

	private double inter(Polygon2D_F64 a, Polygon2D_F64 b)
	{
		if (a.size() < 3 || b.size() < 3)
			return 0;

		//		int na = a.size();
//		int nb = b.size();
		Vertex[] ipa = new Vertex[a.size() + 1];
		Vertex[] ipb = new Vertex[b.size()+ 1];
		Rectangle2D_F64 bbox = new Rectangle2D_F64(
				Double.MAX_VALUE, Double.MAX_VALUE,
				-Double.MAX_VALUE, -Double.MAX_VALUE);


		range(a, bbox);
		range(b, bbox);

		double rngx = bbox.p1.x - bbox.p0.x;
		sclx = gamut / rngx;
		double rngy = bbox.p1.y - bbox.p0.y;
		scly = gamut / rngy;
		double ascale = sclx * scly;

		fit(a, ipa, 0, bbox);
		fit(b, ipb, 2, bbox);

		for (int j = 0; j < a.size(); ++j) {
			for (int k = 0; k < b.size(); ++k) {
				if (ovl(ipa[j].rx, ipb[k].rx) && ovl(ipa[j].ry, ipb[k].ry)) {
					long a1 = -area(ipa[j].ip, ipb[k].ip, ipb[k + 1].ip);
					long a2 = area(ipa[j + 1].ip, ipb[k].ip, ipb[k + 1].ip);
					boolean o = a1 < 0;
					if (o == a2 < 0) {
						long a3 = area(ipb[k].ip, ipa[j].ip, ipa[j + 1].ip);
						long a4 = -area(ipb[k + 1].ip, ipa[j].ip,
								ipa[j + 1].ip);
						if (a3 < 0 == a4 < 0) {
							if (o)
								cross(ipa[j], ipa[j + 1], ipb[k], ipb[k + 1],
										a1, a2, a3, a4);
							else
								cross(ipb[k], ipb[k + 1], ipa[j], ipa[j + 1],
										a3, a4, a1, a2);
						}
					}
				}
			}
		}

		inness(ipa, a.size(), ipb, b.size());
		inness(ipb, b.size(), ipa, a.size());

		return ssss / ascale;
	}
}
