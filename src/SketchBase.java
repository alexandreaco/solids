//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		if(p.x>=0 && p.x<buff.getWidth() && p.y>=0 && p.y < buff.getHeight())
			buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
		
	/*
	 *  Source for DDA algorithm http://www.w3professors.com/Data/Courses/Computer-Graphics/Programs/program-to-draw-a-line-using-dda-algorithm.pdf
	 *  
	 */	
	public static void drawLineDDA(BufferedImage buff, Point2D p1, Point2D p2)
	{
		float xStart;
		float yStart;
		float xEnd;
		float yEnd;
		
		// Make sure you're moving from left to right
		if(p1.x < p2.x)
		{
			xStart = p1.x;
			yStart = p1.y;
			xEnd = p2.x;
			yEnd = p2.y;
				
		}
		else 
		{
			xStart = p2.x;
			yStart = p2.y;
			xEnd = p1.x;
			yEnd = p1.y;
		}
		
		float dx = (xEnd - xStart);
		float dy = (yEnd - yStart);
		float m = dy/Math.abs(dx);
		ColorType c = new ColorType(p1.c);
			
		// Draw Line
		for ( float i = xStart; i < xEnd; i++)
		{
			if ( m <= 1)
			{
				dx = 1;
				dy = m * dx;
			}
			else
			{
				dy = 1;
				dx = dy / m;
			}
				
			xStart = xStart + dx;
			yStart = yStart + dy;
			drawPoint( buff, new Point2D((int)xStart, (int)yStart, c));
		}	
	}
		
		
		
	/* 
	 * Algorithm Source : http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html
	 * 
	 */
	public static void drawTriangle( BufferedImage buff, Point2D _p1, Point2D _p2, Point2D _p3)
	{
		// Vertices are ordered from top to bottom
		float v1_x = _p1.x;
		float v1_y = _p1.y;
		float v2_x = _p2.x;
		float v2_y = _p2.y;
		float v3_x = _p3.x;
		float v3_y = _p3.y;
		
		//System.out.println(v1_y + " " + v2_y + " " + v3_y);
		
		ColorType c = new ColorType(_p1.c);
		
//		if ( v2_y == v3_y )	// Flat Bottom Triangle
//		{
//			
//		}
//		else if ( v1_y == v2_y)	// Flat Top Triangle
//		{
//			
//		}
		
		float invslope1 = (v2_x - v1_x) / (v2_y - v1_y);
		float invslope2 = (v3_x - v1_x) / (v3_y - v1_y);

		float curx1 = v1_x;
		float curx2 = v1_x;

		for (int scanlineY = (int)v1_y; scanlineY <= v2_y; scanlineY++)
		{
			drawLineDDA(buff, new Point2D((int)curx1, scanlineY, c), new Point2D((int)curx2, scanlineY, c));
		    curx1 += invslope1;
		    curx2 += invslope2;
		}
		
	}	
	
	/* e0 is long edge, e1 is a short edge */
	public static void drawSpanBetweenEdges(Edge e0, Edge e1, BufferedImage buff_frame, float[][] buff_depth, boolean doSmooth)
	{
		float e0ydiff = (float)(e0.Y2 - e0.Y1);
		
		if (e0ydiff == 0.0f)	// If the long edge is 0 long
		{
			return;
		}
		
		float e1ydiff = (float)(e1.Y2 - e1.Y1);	
		
		if (e1ydiff == 0.0f)	// If the short edge is 0 long i.e. if there is no upper/lower triangle
		{
			return;
		}
		
		float e0xdiff = (float)(e0.X2 - e0.X1);	// Difference in x values for Long Edge
		float e1xdiff = (float)(e1.X2 - e1.X1);	// Difference in x values for Short Edge
		float e0zdiff = (float)(e0.Z2 - e0.Z1); // Difference in z values for Long Edge
		float e0zstep = e0zdiff / e1ydiff;
		float e1zdiff = (float)(e1.Z2 - e1.Z1); // Difference in z values for short edge
		float e1zstep = e1zdiff / e1ydiff;
		
		float e0rdiff;	// Difference in R values for Long Edge
		float e0gdiff;	// Difference in G values for Long Edge
		float e0bdiff;	// Difference in B values for Long Edge

		float e1rdiff;	// Difference in R values for Short Edge
		float e1gdiff;	// Difference in G values for Short Edge
		float e1bdiff;	// Difference in B values for Short Edge
		
		float r0;
		float g0;
		float b0;
		int x0;
		float z0 = e0.Z1;
		
		float r1;
		float g1;
		float b1;
		int x1;
		float z1 = e1.Z1;

		float factor1 = (float)(e1.Y1 - e0.Y1) / e0ydiff;
		float factorStep1 = 1.0f / e0ydiff;
		
		float factor2 = 0.0f;
		float factorStep2 = 1.0f / e1ydiff;
		
		if ( !doSmooth )
		{
			e0.Color2 = new ColorType(e0.Color1);
		}
				
		// For the length of the short edge, 
		// Draw spans between the two edges
		for (int y = e1.Y1; y < e1.Y2; y++)
		{
			x0 = e0.X1 + (int)(e0xdiff * factor1);
			x1 = e1.X1 + (int)(e1xdiff * factor2);
			
			z0 = z0 + e0zstep;
			z1 = z1 + e1zstep;
			
			Span s = new Span(e0.Color1, x0, z0, e0.Color2, x1, z1);	// Create span with x and color values for this scan line
			
			drawSpan(s, y, buff_frame, buff_depth);
			
			factor1 += factorStep1;	
			factor2 += factorStep2;
		}
	}
		
	public static void drawSpan(Span s, int y, BufferedImage buff_frame, float[][] buff_depth )
	{
		SketchBase pen = new SketchBase();
		int xdiff = s.X1 - s.X0;
		
		
		if(xdiff == 0.0f) {	// If span has no width.
			return;
		}
				
		float zdiff = s.z0 - s.z1;
		float rdiff = s.C1.r - s.C0.r;	// delta r
		float gdiff = s.C1.g - s.C0.g;	// delta g
		float bdiff = s.C1.b - s.C0.b;	// delta b
		
		float r0;
		float g0;
		float b0;
			
		float factor = 0.0f;
		float factorStep = 1.0f / (float)xdiff;
		float zStep = zdiff/xdiff;
		
		float z0 = s.z0;
		
		for (int x = s.X0; x < s.X1; x++)
		{
			r0 = s.C0.r + (rdiff * factor);
			g0 = s.C0.g + (gdiff * factor);
			b0 = s.C0.b + (gdiff * factor);
			
			ColorType c = new ColorType(r0, g0, b0);
			Point2D p = new Point2D(x, y, c);
			
			z0 = z0 + zStep;

			// If point in within screen bounds
			if(p.x>=0 && p.x<buff_frame.getWidth() && p.y>=0 && p.y < buff_frame.getHeight())
			{
				// If depth buffer location has a higher z value than the new point
				if ( z0 > buff_depth[p.x][p.y] )
				{
					pen.drawPoint(buff_frame, p);
					buff_depth[p.x][p.y] = z0;
				}
			}
			
			factor += factorStep;
		}
	}
}
