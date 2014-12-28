

public class LightAmbient extends Light 
{
	ColorType color;
	
	public LightAmbient()
	{
		// Standard ambient light
		color = new ColorType(1.0f, 1.0f, 1.0f);
	}
	
	public ColorType getIllumination(Material mat, Vector3D v, Vector3D n, Boolean[] activeK){
		
		ColorType res = new ColorType(0.0f,0.0f,0.0f);
		
		if (activeK[0])	// Ambient coefficient is on
		{
			res.r = (float)mat.kambient.r*color.r;	// Multiply red channel by ambient coefficient
			res.g = (float)mat.kambient.g*color.g;	// Multiply green channel by ambient coefficient
			res.b = (float)mat.kambient.b*color.b;	// Multiply blue channel by ambient coefficient
			
			
			// clamp so that allowable maximum illumination level is not exceeded
			res.r = (float) Math.min(1.0, res.r);
			res.g = (float) Math.min(1.0, res.g);
			res.b = (float) Math.min(1.0, res.b);
		}
		return res;
	}
}