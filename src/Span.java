public class Span
{
	
	public ColorType C0;
	public ColorType C1;
	
	public int X0;
	public int X1;
	
	public float z0, z1;
	
	public Span(ColorType _c0, int _x0, float _z0, ColorType _c1, int _x1, float _z1 ) 
	{
		if (_x0 < _x1)	// Order x values from left to right of window
		{
			C0 = _c0;
			X0 = _x0;
			z0  = _z0;
			C1 = _c1;
			X1 = _x1;
			z1 = _z1;
		}
		else
		{
			C0 = _c1;
			X0 = _x1;
			z0 = _z1;
			C1 = _c0;
			X1 = _x0;
			z1 = _z0;
		}
	}
}