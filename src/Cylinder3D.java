

public class Cylinder3D extends Solid
{
	private float rx, ry, rz;
	
	public Cylinder3D(float _x, float _y, float _z, float _rx, float _ry, float _rz, int _m, int _n, Material _mat)
	{
		//System.out.println("making new Cylinder")
		center = new Vector3D(_x,_y,_z);
		rx = _rx;
		ry = _ry;
		rz = _rz;
		m = _m;
		n = 4;

		mat = _mat;
		
		initMesh();
	}
	
	/*
	 * Math for algorithm from here http://www.cs.uiuc.edu/class/sp06/cs418/notes/17-SmoothSurfaces.pdf
	 * 
	 * 
	 */
	public void fillMesh()
	{
		int i,j;		
		float theta, phi ;
		float d_theta=(float)(2.0*Math.PI)/ ((float)(m-1));
//		float d_phi= ry/(float)n;
		float c_theta,s_theta;	// cos
		float c_phi, s_phi;		// sin
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		
		// For each point along the circle top
		for( i=0, theta=-(float)Math.PI; i<m; ++i, theta += d_theta)
		{
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			
			for( j=0, phi=-ry/2; j<2; ++j, phi += ry)
			{
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				surfaces[0].v[i][j].x=center.x+rx*c_theta;
				surfaces[0].v[i][j].y=center.y+phi;
				surfaces[0].v[i][j].z=center.z+rz*s_theta;	
			}
		}
		// For each point along the circle bottom
		for( i=0, theta=(-(float)Math.PI) + d_theta/2; i<m; ++i, theta += d_theta)
		{
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			
			for( j=2, phi=ry/2; j<4; ++j, phi -= ry)
			{
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				surfaces[0].v[i][j].x=center.x+rx*c_theta;
				surfaces[0].v[i][j].y=center.y+phi;
				surfaces[0].v[i][j].z=center.z+rz*s_theta;	

			}
		}
	}
	
	
	
	
	/****** Setter and Getter Methods *********/
	
	public void set_radius(float _rx, float _ry, float _rz)
	{
		rx = _rx;
		ry = _ry;
		rz = _rz;
		fillMesh();		// update the triangle mesh
	}
}