import java.awt.image.BufferedImage;

public class Box3D extends Solid
{
	private float width, height, depth;
	
	// Create a new Box with a vector pointing to the center, 
	// an x, y, z width, and an m and n. 
	public Box3D(float _x, float _y, float _z, float _width, float _height, float _depth, Material _mat)
	{
		//System.out.println("making new box");
		center = new Vector3D(_x,_y,_z);
		width = _width;
		height = _height;
		depth = _depth;

		mat = _mat;
		
		initMesh();
	}

	public void initMesh()
	{
		surfaces = new Mesh3D[3];
		surfaces[0] = new Mesh3D(2,5);	// wrap around edge
		surfaces[1] = new Mesh3D(2,2); // Top
		surfaces[2] = new Mesh3D(2,2); // Bottom
		fillMesh();  // set the mesh vertices and normals
	}
	
	public void set_size(float _width, float _height, float _depth)
	{
		width = _width;
		height = _height;
		depth = _depth;
		fillMesh(); // update the triangle mesh
	}
	
	
	// Create a Box object	
	public void fillMesh()
	{
		// Compute locations relative to box's center 
		float top = center.y - height/2;
		float bottom = center.y + height/2;
		float front = center.z + depth/2;
		float back = center.z - depth/2;
		float left = center.x + width/2;
		float right = center.x - width/2;
		
		/* Create the sides */
		
		// top front left
		surfaces[0].v[0][0].x = left;
		surfaces[0].v[0][0].y = top;
		surfaces[0].v[0][0].z = front;
		
		// top front right
		surfaces[0].v[0][1].x = right;
		surfaces[0].v[0][1].y = top;
		surfaces[0].v[0][1].z = front;
		
		// top back right
		surfaces[0].v[0][2].x = right;
		surfaces[0].v[0][2].y = top;
		surfaces[0].v[0][2].z = back;
		
		// top back left
		surfaces[0].v[0][3].x = left;
		surfaces[0].v[0][3].y = top;
		surfaces[0].v[0][3].z = back;
		
		// top front left (repeated)
		surfaces[0].v[0][4].x = left;
		surfaces[0].v[0][4].y = top;
		surfaces[0].v[0][4].z = front;
		
		// bottom front left
		surfaces[0].v[1][0].x = left;
		surfaces[0].v[1][0].y = bottom;
		surfaces[0].v[1][0].z = front;
				
		// bottom front right
		surfaces[0].v[1][1].x = right;
		surfaces[0].v[1][1].y = bottom;
		surfaces[0].v[1][1].z = front;
				
		// bottom back right
		surfaces[0].v[1][2].x = right;
		surfaces[0].v[1][2].y = bottom;
		surfaces[0].v[1][2].z = back;
				
		// bottom back left
		surfaces[0].v[1][3].x = left;
		surfaces[0].v[1][3].y = bottom;
		surfaces[0].v[1][3].z = back;
		
		// bottom front left (repeat)
		surfaces[0].v[1][4].x = left;
		surfaces[0].v[1][4].y = bottom;
		surfaces[0].v[1][4].z = front;
		
		/* Create the Top */
		
		// top front left
		surfaces[1].v[0][0].x = left;
		surfaces[1].v[0][0].y = top;
		surfaces[1].v[0][0].z = front;
				
		// top front right
		surfaces[1].v[0][1].x = right;
		surfaces[1].v[0][1].y = top;
		surfaces[1].v[0][1].z = front;
		
		// top back right
		surfaces[1].v[1][0].x = right;
		surfaces[1].v[1][0].y = top;
		surfaces[1].v[1][0].z = back;
		
		// top back left
		surfaces[1].v[1][1].x = left;
		surfaces[1].v[1][1].y = top;
		surfaces[1].v[1][1].z = back;
		
		/* Create the Bottom */
		
		// bottom front left
		surfaces[2].v[0][0].x = left;
		surfaces[2].v[0][0].y = bottom;
		surfaces[2].v[0][0].z = front;
				
		// bottom front right
		surfaces[2].v[0][1].x = right;
		surfaces[2].v[0][1].y = bottom;
		surfaces[2].v[0][1].z = front;
		
		// bottom back right
		surfaces[2].v[1][0].x = right;
		surfaces[2].v[1][0].y = bottom;
		surfaces[2].v[1][0].z = back;
		
		// bottom back left
		surfaces[2].v[1][1].x = left;
		surfaces[2].v[1][1].y = bottom;
		surfaces[2].v[1][1].z = back;
	}
	
	
	public void rotateMesh( Quaternion viewing_quaternion, Vector3D viewing_vector)
	{
		for (int i = 0; i < surfaces.length; i++)
		{
			surfaces[i].rotateMesh( viewing_quaternion, viewing_vector);
		}
	}
}

