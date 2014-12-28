public class Material
{
	public String name;
	public ColorType kambient, kdiffuse, kspecular;
	public int ns;
	public boolean specular, diffuse, ambient;
	
	public Material(String _name, ColorType _a, ColorType _d, ColorType _s, int _ns)
	{
		name = _name;
		kspecular = new ColorType(_s); // specular coefficient for r,g,b
		kambient = new ColorType(_a);  // ambient coefficient for r,g,b
		kdiffuse = new ColorType(_d);  // diffuse coefficient for r,g,b
		ns = _ns;  // specular exponent
		
		// set boolean variables 
		specular = (ns>0 && (kspecular.r > 0.0 || kspecular.g > 0.0 || kspecular.b > 0.0));
		diffuse = (kdiffuse.r > 0.0 || kdiffuse.g > 0.0 || kdiffuse.b > 0.0);
		ambient = (kambient.r > 0.0 || kambient.g > 0.0 || kambient.b > 0.0);
		
	}
	
	public void print() 
	{
		System.out.println(name);
	}
	
}