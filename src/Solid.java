import java.awt.image.BufferedImage;

public class Solid
{
	public int m,n;	// Number of steps to take when generating Mesh3D
	public Vector3D center;	// Vector pointing to center of solid
	public Mesh3D[] surfaces;	// Array of surfaces
	
	public Material mat = new Material( "Plastic" , new ColorType(), new ColorType(), new ColorType(), 2);
	
	public Solid()
	{		
	}
	
	public void initMesh()
	{
		surfaces = new Mesh3D[1];
		surfaces[0] = new Mesh3D(m,n);
		fillMesh();  // set the mesh vertices and normals
	}
	
	public void set_center(float _x, float _y, float _z)
	{
		center.x=_x;
		center.y=_y;
		center.z=_z;
		fillMesh();  // update the triangle mesh
	}
	
	
	public void fillMesh()
	{	
		// Will be different for each solid
	}
	
	
	public void rotateMesh( Quaternion viewing_quaternion, Vector3D viewing_vector)
	{
		for (int i = 0; i < surfaces.length; i++)
		{
			surfaces[i].rotateMesh( viewing_quaternion, viewing_vector);
		}
	}
	
	
	
	/****** Setter and Getter Methods *********/
	public void set_m(int _m)
	{
		m = _m;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public void set_n(int _n)
	{
		n = _n;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public int get_n()
	{
		return n;
	}
	
	public int get_m()
	{
		return m;
	}
	
	
	/****** Translation Methods *********/
	
	public void transLeft() 
	{
		float newX = center.x + 50;
		center = new Vector3D( newX, center.y, center.z );
		initMesh();  // update the triangle mesh
		System.out.println("Center at " + center.x + " " + center.y + " " + center.z );
	}
	
	public void transRight() 
	{
		center = new Vector3D( center.x + 50, center.y, center.z );
		initMesh();  // update the triangle mesh
		System.out.println("Center at " + center.x + " " + center.y + " " + center.z );
	}
	
	public void transUp()
	{
		set_center( center.x, center.y + 100f, center.z );
		System.out.println("Center at " + center.x + " " + center.y + " " + center.z );
	}
	
	public void transDown()
	{
		set_center( center.x, center.y - 100f, center.z );
		System.out.println("Center at " + center.x + " " + center.y + " " + center.z );
	}
	
	
	public void draw( BufferedImage buff_frame, float[][] buff_depth, int illumination, Light[] lights, Boolean[] activeLights, Boolean[] activeK )
	{		
		for (int i = 0; i < surfaces.length; i++)
		{
			surfaces[i].draw( buff_frame, buff_depth, illumination, lights, activeLights, activeK, mat );	
		}
	}
}