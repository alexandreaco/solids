


public class Light
{
	public Vector3D direction;
	public ColorType color;
	public boolean isOn;
	
	// apply this light source to the vertex / normal, given material
	// return resulting color value
	public ColorType getIllumination(Material mat, Vector3D v, Vector3D n, Boolean[] activeK){
				
		return new ColorType();
	}
	
	public void toggleLight()
	{
		if (isOn)
		{
			isOn = false;
			System.out.println("turning off");
			return;
		}
		else 
		{
			isOn = true;
		}
	}
	
}