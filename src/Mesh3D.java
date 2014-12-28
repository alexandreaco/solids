import java.awt.image.BufferedImage;

//****************************************************************************
//       3D triangle mesh with normals
//****************************************************************************
// History :
//   Nov 6, 2014 Created by Stan Sclaroff
//
public class Mesh3D
{
	public int cols,rows;
	public Vector3D[][] v;
	public Vector3D[][] n;
	
	public Mesh3D(int _rows, int _cols)
	{
		cols=_cols;
		rows=_rows;
		v = new Vector3D[rows][cols];	// Vectors
		n = new Vector3D[rows][cols];	// Normals to the vectors
		for(int i=0;i<rows;++i)
			for(int j=0;j<cols;++j)
			{
				v[i][j] = new Vector3D();
				n[i][j] = new Vector3D();
			}
	}
	
	public void rotateMesh(Quaternion q, Vector3D center)
	{
		Quaternion q_inv = q.conjugate();
		Vector3D vec;
		
		Quaternion p;
		
		for(int i=0;i<rows;++i)
			for(int j=0;j<cols;++j)
			{
				// apply pivot rotation to vertices, given center point
				p = new Quaternion((float)0.0,v[i][j].minus(center)); 
				p=q.multiply(p);
				p=p.multiply(q_inv);
				vec = p.get_v();
				v[i][j]=vec.plus(center);
				
				// rotate the normals
				p = new Quaternion((float)0.0,n[i][j]);
				p=q.multiply(p);
				p=p.multiply(q_inv);
				n[i][j] = p.get_v();
			}	
	}
	
	public void draw( BufferedImage buff_frame, float[][] buff_depth, int illumination, Light[] lights, Boolean[] activeLights, Boolean[] activeK, Material mat ) 
	{	
		Point2D[] tri = {new Point2D(), new Point2D(), new Point2D(), new Point2D()};
		
		for ( int i = 0; i < rows - 1 ; i++ )
		{
			for ( int j = 0; j < cols - 1; j++ )
			{
				
				Vector3D v0 = v[i][j];
				Vector3D v1 = v[i][j+1];
				Vector3D v2 = v[i+1][j+1];
				Vector3D v3 = v[i+1][j];
				
				Vector3D n0 = n[i][j];
				Vector3D n1 = n[i][j+1];
				Vector3D n2 = n[i+1][j+1];
				Vector3D n3 = n[i+1][j];
				
				Triangle t1, t2;
				
				// Generate color based on EACH vector normal
//				tri[0].x = (int)v0.x;
//				tri[0].y = (int)v0.y;
//				tri[0].c = new ColorType(1.0,1.0,1.0);
//				
//				tri[1].x = (int)v1.x;
//				tri[1].y = (int)v1.y;
//				tri[0].c = new ColorType(1.0,1.0,1.0);
//				
//				tri[2].x = (int)v2.x;
//				tri[2].y = (int)v2.y;
//				tri[0].c = new ColorType(1.0,1.0,1.0);  
//				
//				tri[3].x = (int)v3.x;
//				tri[3].y = (int)v3.y;
//				tri[0].c = new ColorType(1.0,1.0,1.0);
//				
//				Triangle t1 = new Triangle(tri[0], tri[3], tri[2]);
//				Triangle t2 = new Triangle(tri[0], tri[2], tri[1]);
				
//				if (illumination == 0)
//				{
//					t1.drawNoShade(buff_frame, buff_depth);
//					t2.drawNoShade(buff_frame, buff_depth);
//				}
				
				t1 = new Triangle(v0, v1, v3);
				t2 = new Triangle(v3, v1, v2);
				
				
				if (illumination == 1)
				{
//					t1 = new Triangle(v0, v1, v3);
//					t2 = new Triangle(v3, v1, v2);
					
					t1.drawFlatShade(buff_frame, buff_depth, lights, activeLights, activeK, mat);
					t2.drawFlatShade(buff_frame, buff_depth, lights, activeLights, activeK, mat);
				}
				else if ( illumination == 2)
				{
					t1.setNormals(n0, n1, n3);
					t2.setNormals(n3, n1, n2);
					
					t1.drawGorouad(buff_frame, buff_depth, lights, activeLights, activeK, mat);
					t2.drawGorouad(buff_frame, buff_depth, lights, activeLights, activeK, mat);
				}
			}
		}
	}
}