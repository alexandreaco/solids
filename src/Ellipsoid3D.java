//****************************************************************************
//      Sphere class
//****************************************************************************
// History :
//   Nov 6, 2014 Created by Stan Sclaroff
//

public class Ellipsoid3D extends Solid
{
	private float rx, ry, rz;
	
	public Ellipsoid3D(float _x, float _y, float _z, float _rx, float _ry, float _rz, int _m, int _n, Material _mat)
	{
		//System.out.println("making new ellipsoid");
		center = new Vector3D(_x,_y,_z);
		rx = _rx;
		ry = _ry;
		rz = _rz;
		m = _m;
		n = _n;
		
		mat = _mat;
		
		initMesh();
	}
	
	public void set_radius(float _rx, float _ry, float _rz)
	{
		rx = _rx;
		ry = _ry;
		rz = _rz;
		fillMesh(); // update the triangle mesh
	}
	
	// fill the triangle mesh vertices and normals
	// using the current parameters for the sphere
	public void fillMesh()
	{
		int i,j;		
		float theta, phi;
		float d_theta=(float)(2.0*Math.PI)/ ((float)(m-1));
		float d_phi=(float)Math.PI / ((float)n-1);
		float c_theta,s_theta;	// cos
		float c_phi, s_phi;		// sin
		Vector3D du = new Vector3D();
		Vector3D dv = new Vector3D();
		
		for(i=0,theta=-(float)Math.PI;i<m;++i,theta += d_theta)
	    {
			c_theta=(float)Math.cos(theta);
			s_theta=(float)Math.sin(theta);
			
			for(j=0,phi=(float)(-0.5*Math.PI);j<n;++j,phi += d_phi)
			{
				// vertex location
				c_phi = (float)Math.cos(phi);
				s_phi = (float)Math.sin(phi);
				surfaces[0].v[i][j].x=center.x+rx*c_phi*c_theta;
				surfaces[0].v[i][j].y=center.y+ry*c_phi*s_theta;
				surfaces[0].v[i][j].z=center.z+rz*s_phi;
				
				// compute partial derivatives
				// then use cross-product to get the normal
				// and normalize to produce a unit vector for the normal
				du.x = -(rx*c_phi)*s_theta;
				du.y = (ry*c_phi)*c_theta;
				du.z = 0;
				
				dv.x = -rx*s_phi*c_theta;
				dv.y = -ry*s_phi*s_theta;
				dv.z = rz*c_phi;
				
				du.crossProduct(dv, surfaces[0].n[i][j]);
				surfaces[0].n[i][j].normalize();
				
//				// unit normal to sphere at this vertex
				surfaces[0].n[i][j].x = c_phi*c_theta;
				surfaces[0].n[i][j].y = c_phi*s_theta;
				surfaces[0].n[i][j].z=s_phi;
			}
	    }
	}
}