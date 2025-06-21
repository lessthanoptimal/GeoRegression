/*
 * Copyright (C) 2025, Peter Abeles. All Rights Reserved.
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

package georegression.examples;

import georegression.delaunay.DelaunayIncrementalWalk;
import georegression.struct.Mesh2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Triangle2D_F64;
import org.ddogleg.struct.DogArray;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/// Show you how to triangulate points 2D using Delaunay triangulation
///
/// Creates a random set of points and passes it in
public class ExampleTriangulation {
	public static void main( String[] args ) {

		// Size of the image the points will be randomly put into
		int width = 400, height = 300;

		// Randomly generate points
		var random = new Random(0xF00D);

		// border that points won't be place inside of
		int border = 10;

		var points = new DogArray<>(Point2D_F64::new);
		for (int i = 0; i < 50; i++) {
			// Avoid generating points right on the image edge. This is for appearance only
			double x = border + random.nextDouble()*(width - 2*border);
			double y = border + random.nextDouble()*(height - 2*border);
			points.grow().setTo(x, y);
		}

		// Compute the Delaunay triangulation
		var delaunay = new DelaunayIncrementalWalk();
//		delaunay.setVerbose(System.out, null);

		// To make it more generic a function interface is used for the points array. This avoids making you
		// convert it into a specific array format
		delaunay.process(( idx, p ) -> {
			p.setTo(points.get(idx));
			return true;
		}, points.size);

		// Convert internal results into a standard mesh data structure
		Mesh2D_F64 mesh = delaunay.toMesh(null);

		visualizeWithSwing(width, height, mesh);
	}

	/// Visualize results using Swing. Draws the triangles and points.
	private static void visualizeWithSwing( int width, int height, Mesh2D_F64 mesh ) {
		// Visualize results
		var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();

		// Turn on antialiasing so that it looks less like graphics from the 1980s
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// White background
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		// Draw the triangles
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		var line = new Line2D.Double();
		var triangle = new Triangle2D_F64();
		for (int triangleID = 0; triangleID < mesh.triangles.size; triangleID += 3) {
			mesh.getTriangle(triangleID, triangle);

			for (int i = 0, j = 2; i < 3; j = i, i++) {
				Point2D_F64 a = triangle.get(j);
				Point2D_F64 b = triangle.get(i);
				line.setLine(a.x, a.y, b.x, b.y);
				g2.draw(line);
			}
		}

		// Draw the points
		g2.setColor(Color.RED);
		int r = 5;
		var oval = new Ellipse2D.Double();
		for (int pointID = 0; pointID < mesh.points.size(); pointID++) {
			Point2D_F64 p = mesh.points.getTemp(pointID);
			oval.setFrame(p.x - r, p.y - r, 2*r, 2*r);
			g2.fill(oval);
		}

		var panel = new JPanel() {
			@Override protected void paintComponent( Graphics g ) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		panel.setPreferredSize(new Dimension(width + 10, height + 10));

		var frame = new JFrame();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
