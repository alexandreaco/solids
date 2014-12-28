

public class LightInfinite extends Light
{
	
	public LightInfinite(ColorType _c, Vector3D _direction)
	{
		color = new ColorType(_c);
		direction = new Vector3D(_direction);
		isOn = true;
	}
	
	public ColorType getIllumination(Material mat, Vector3D v, Vector3D n, Boolean[] activeK){
		ColorType res = new ColorType();
		
		if (isOn)
		{		
			// dot product between light direction and normal
			// light must be facing in the positive direction
			// dot <= 0.0 implies this light is facing away (not toward) this point
			// therefore, light only contributes if dot > 0.0
			Vector3D checkN = n;
			Vector3D checkDirection = direction;
			
			double dot = direction.dotProduct(n);
			if(dot>0.0)
			{
				// diffuse component
				if(mat.diffuse && activeK[1])
				{
					res.r = (float)(dot*mat.kdiffuse.r*color.r);
					res.g = (float)(dot*mat.kdiffuse.g*color.g);
					res.b = (float)(dot*mat.kdiffuse.b*color.b);
				}
				// specular component
				if(mat.specular  && activeK[2])
				{
					Vector3D r = direction.reflect(n);
					dot = r.dotProduct(v);
					if(dot>0.0)
					{
						res.r += (float)Math.pow((dot*mat.kspecular.r*color.r),mat.ns);
						res.g += (float)Math.pow((dot*mat.kspecular.g*color.g),mat.ns);
						res.b += (float)Math.pow((dot*mat.kspecular.b*color.b),mat.ns);
					}
				}
						
				// clamp so that allowable maximum illumination level is not exceeded
				res.r = (float) Math.min(1.0, res.r);
				res.g = (float) Math.min(1.0, res.g);
				res.b = (float) Math.min(1.0, res.b);
			}
		}
		else
		{
			res.r = res.g = res.b = 0.0f;	// There is no light to be returned
			System.out.println("no light");
		}
			return(res);
	}
}