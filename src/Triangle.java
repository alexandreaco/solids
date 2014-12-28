import java.awt.image.BufferedImage;



public class Triangle {
	
	SketchBase pen;
	public Point2D p1, p2, p3;
	public Vector3D v1, v2, v3, triangle_normal, n1, n2, n3;


	
	Vector3D view_vector = new Vector3D((float)0.0,(float)0.0,(float)1.0);
	
//	Depreciated
//	public Triangle( Point2D _p1, Point2D _p2, Point2D _p3)
//	{
//		p1 = new Point2D(_p1);
//		p2 = new Point2D(_p2);
//		p3 = new Point2D(_p3);
//		pen = new SketchBase();
//	}
	
	public Triangle( Vector3D _v1, Vector3D _v2, Vector3D _v3 )
	{
		v1 = new Vector3D(_v1);
		v2 = new Vector3D(_v2);
		v3 = new Vector3D(_v3);
		
		triangle_normal = computeTriangleNormal(v1, v2, v3);
		
		pen = new SketchBase();
	}
	
	
	// helper function to bubble sort triangle vertices by x value
	private static Point2D[] sortTriangleVertsX(Point2D p1, Point2D p2, Point2D p3)
	{
		Point2D pts[] = {p1, p2, p3};
		Point2D tmp;
		int j=0;
		boolean swapped = true;
			     
		while (swapped) 
		{
			swapped = false;
			j++;
			for (int i = 0; i < 3 - j; i++) 
			{                                       
				if (pts[i].x > pts[i + 1].x) 
				{                          
					tmp = pts[i];
					pts[i] = pts[i + 1];
					pts[i + 1] = tmp;
					swapped = true;
				}
			}                
		}
		return(pts);
	}
	
	// helper function to bubble sort triangle vertices by y value
	private static Point2D[] sortTriangleVertsY(Point2D p1, Point2D p2, Point2D p3)
	{
		Point2D pts[] = {p1, p2, p3};
		Point2D tmp;
		int j=0;
		boolean swapped = true;
		
		while (swapped) 
		{
			swapped = false;
			j++;
			for (int i = 0; i < 3 - j; i++) 
			{                                       
				if (pts[i].y > pts[i + 1].y) 
				{                          
					tmp = pts[i];
					pts[i] = pts[i + 1];
					pts[i + 1] = tmp;
					swapped = true;
				}
			}                
		}
		return(pts);
	}
	
//	public void draw( BufferedImage buff ) {
//		
//		// sort the triangle vertices by ascending y value
//		Point2D p[] = sortTriangleVertsY(p1,p2,p3);
//		
//		pen.drawTriangle( buff, p1, p2, p3);
//	}
	
	/* Draws Triangle Outlines */
	public void drawOutline( BufferedImage buff )
	{
		// sort the triangle vertices by ascending x value
		Point2D p[] = sortTriangleVertsX(p1,p2,p3);
		
		// Draw Each Edge
		pen.drawLineDDA(buff, p[0], p[1]);
		pen.drawLineDDA(buff, p[1], p[2]);
		pen.drawLineDDA(buff, p[2], p[0]);
	}
	
	/* Draws Triangles with a flat color. Does not use the illumination equation */
	public void drawNoShade( BufferedImage buff_frame, float[][] buff_depth )
	{
		//pen = new SketchBase();
		
		Edge e1 = new Edge(p1.c, p1.x, p1.y, 0.0f, p2.c, p2.x, p2.y, 0.0f);
		Edge e2 = new Edge(p2.c, p2.x, p2.y, 0.0f, p3.c, p3.x, p3.y, 0.0f);
		Edge e3 = new Edge(p3.c, p3.x, p3.y, 0.0f, p1.c, p1.x, p1.y, 0.0f);
		
		Edge[] edges = {e1, e2, e3};
		
		int maxLength = 0;
		int longEdge = 0;
		
		// Determine Long Edge
		for ( int i = 0 ; i < 3 ; i++ )
		{
			int length = edges[i].Y2 - edges[i].Y1;
			
			if( length > maxLength )
			{
				maxLength = length;
				longEdge = i;
			}
		}
		
		// Remaining two edges are short
		int shortEdge1 = (longEdge + 1) % 3;
		int shortEdge2 = (longEdge + 2) % 3;

		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge1], buff_frame, buff_depth, false);		// draw upper triangle
		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge2], buff_frame, buff_depth, false); 		// draw lower triangle
	}
	
	public void drawFlatShade( BufferedImage buff_frame, float[][] buff_depth, Light[] lights, Boolean[] activeLights, Boolean[] activeK, Material mat )
	{
		// For each light in the scene, Add light to paint bucket.		
		ColorType paint = new ColorType();
		
		for (int i = 0; i < lights.length; i ++)
		{
			if (activeLights[i])	// If the light is turned on
				paint.addColor(lights[i].getIllumination( mat, view_vector, triangle_normal, activeK ));					
		}
		
		// Flood Fill triangle with single color
		Edge e1 = new Edge(paint, (int)v1.x, (int)v1.y, v1.z, paint, (int)v2.x, (int)v2.y, v2.z);
		Edge e2 = new Edge(paint, (int)v2.x, (int)v2.y, v2.z, paint, (int)v3.x, (int)v3.y, v2.z);
		Edge e3 = new Edge(paint, (int)v3.x, (int)v3.y, v3.z, paint, (int)v1.x, (int)v1.y, v3.z);
		
		Edge[] edges = {e1, e2, e3};
		
		int maxLength = 0;
		int longEdge = 0;
		
		// Determine Long Edge
		for ( int i = 0 ; i < 3 ; i++ )
		{
			int length = edges[i].Y2 - edges[i].Y1;
			
			if( length > maxLength )
			{
				maxLength = length;
				longEdge = i;
			}
		}
		
		// Remaining two edges are short
		int shortEdge1 = (longEdge + 1) % 3;
		int shortEdge2 = (longEdge + 2) % 3;

		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge1], buff_frame, buff_depth, false);		// draw upper triangle
		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge2], buff_frame, buff_depth, false);	 	// draw lower triangle
		
	}
	
	public void drawGorouad( BufferedImage buff_frame, float[][] buff_depth, Light[] lights, Boolean[] activeLights, Boolean[] activeK, Material mat )
	{
		// For each light in the scene, Add light to paint bucket. One for each vertex.
		ColorType paint1 = new ColorType();
		ColorType paint2 = new ColorType();
		ColorType paint3 = new ColorType();
		
		for (int i = 0; i < lights.length; i ++)
		{
			if (activeLights[i])	// If the light is turned on
			{
				paint1.addColor( lights[i].getIllumination( mat, view_vector, n1, activeK ));
				paint2.addColor( lights[i].getIllumination( mat, view_vector, n2, activeK ));
				paint3.addColor( lights[i].getIllumination( mat, view_vector, n3, activeK ));
			}
		}
		
		// Create edges with computed vertex colors
		Edge e1 = new Edge(paint1, (int)v1.x, (int)v1.y, v1.z, paint2, (int)v2.x, (int)v2.y, v2.z);
		Edge e2 = new Edge(paint2, (int)v2.x, (int)v2.y, v2.z, paint3, (int)v3.x, (int)v3.y, v2.z);
		Edge e3 = new Edge(paint3, (int)v3.x, (int)v3.y, v3.z, paint1, (int)v1.x, (int)v1.y, v3.z);
		
		Edge[] edges = {e1, e2, e3};
		
		int maxLength = 0;
		int longEdge = 0;
		
		// Determine Long Edge
		for ( int i = 0 ; i < 3 ; i++ )
		{
			int length = edges[i].Y2 - edges[i].Y1;
			
			if( length > maxLength )
			{
				maxLength = length;
				longEdge = i;
			}
		}
		
		// Remaining two edges are short
		int shortEdge1 = (longEdge + 1) % 3;
		int shortEdge2 = (longEdge + 2) % 3;

		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge1], buff_frame, buff_depth, true);		// draw upper triangle
		pen.drawSpanBetweenEdges(edges[longEdge], edges[shortEdge2], buff_frame, buff_depth, true);	 	// draw lower triangle
				
	}
	
	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Vector3D computeTriangleNormal(Vector3D v0, Vector3D v1, Vector3D v2)
	{
		Vector3D e0 = v1.minus(v2);
		Vector3D e1 = v0.minus(v2);
		Vector3D norm = e0.crossProduct(e1);
			
		if(norm.magnitude()>0.000001)
			norm.normalize();
		else 	// detect degenerate triangle and set its normal to zero
			norm.set((float)0.0,(float)0.0,(float)0.0);

		return norm;
	}
	
	public void setNormals( Vector3D _n1, Vector3D _n2, Vector3D _n3)
	{
		n1 = new Vector3D(_n1);
		n2 = new Vector3D(_n2);
		n3 = new Vector3D(_n3);
	}
	
}