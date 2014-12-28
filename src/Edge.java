/*
 * Author	: Alexandrea Defreitas
 * Date 	: Sept. 8, 2014
 * Course	: CS480 @ Boston University
 * 
 * Title 	: Edge.java
 * 
 * Purpose	: Blueprint to represent lines in small drawing application. 
 * 			Written for programming assignment 1.
 * 
 * Credit	: Line drawing function derived from Bresenham's line algorithm.
 * 
 */

public class Edge
{
	
	public ColorType Color1, Color2;
    public int X1, Y1, X2, Y2;
    public float Z1, Z2;

    public Edge (ColorType _color1, int _x1, int _y1, float _z1, ColorType _color2, int _x2, int _y2, float _z2) {
    	
    	if (_y1 < _y2)
    	{
    		Color1 = _color1;
    		X1 = _x1;
    		Y1 = _y1;
    		Z1 = _z1;
    		
    		Color2 = _color2;
    		X2 = _x2;
    		Y2 = _y2;
    		Z2 = _z2;
    	}
    	else
    	{
    		Color1 = _color2;
    		X1 = _x2;
    		Y1 = _y2;
    		Z1 = _z2;
    		
    		Color2 = _color1;
    		X2 = _x1;
    		Y2 = _y1;
    		Z2 = _z1;
    	}
    }
}