import java.awt.image.BufferedImage;


public class TestSuite
{
	private Material[] mats;	
	private int matActive;
	private Solid[] solids;
	private Light[] lights;
	
	public int[] mList = {5, 12, 25, 50 };
	public int[] nList = {5, 12, 25, 50 };
	private int m = 1;
	private int n = 1;
	private int sceneNum = 0;
	
	private int xOffset, yOffset, zOffset;
	private float scale;
	
	public int illumination;
	private boolean queueLight = false;
	public Boolean[] activeLights;
	public Boolean[] activeK;
	
	
	public TestSuite()
	{
		makeMaterials();
		makeScene0( m, n );
		getActiveLights();

		matActive = 0;
		illumination = 1;
		xOffset = 0;
		yOffset = 0;
		zOffset = 0;
		scale = 1;
		activeK = new Boolean[] {true, true, true};
	}
	
	public void cycleScenes()
	{
		sceneNum++;
		if ( sceneNum > 4 )
		{
			sceneNum = 0;
		}
	}
	
	public void cycleMaterials()
	{
		matActive++;
		if ( matActive >= mats.length )
		{
			matActive = 0;
		}
	}
	
	public void setIllumination( int i )
	{
		if ( i == 0 || i == 1 || i == 2 || i == 3 )
		{
			illumination = i;
		}
		writeCurrentScene();
	}
	
	public void makeMaterials()
	{
		mats = new Material[3];
		
		// Materials						// Ambient							// Diffuse					// Specular			// ns
		mats[0] = new Material("glass",   new ColorType(0.25,0.25,0.25), new ColorType(1.0,0.8,0.0), new ColorType(1.0,1.0,1.0), 1);
		mats[1] = new Material("plastic", new ColorType(0.25,0.25,0.25), new ColorType(0.0,1.0,0.0), new ColorType(0.0,0.0,0.0), 1);
		mats[2] = new Material("rubber",  new ColorType(0.25,0.25,0.25), new ColorType(0.0,0.0,1.0), new ColorType(1.0,1.0,1.0),  1);
	}
	
	public void writeCurrentScene( )
	{
		switch ( sceneNum )
		{
			case 0:
		    	makeScene0(m, n);
		    	break;
			
			case 1:
		    	makeScene1(m, n);
		    	break;
		    	
			case 2:
				makeScene2(m, n);
				break;
				
			case 3:
		    	makeScene3(m, n);
		    	break;
			
			case 4:
		    	makeScene4(m, n);
		    	break;
			
			default:
				break;
		}
	}
	

	/**************** SCENES ****************/
	
	private void makeScene0(int m, int n)
	{
		solids = new Solid[3];
		lights = new Light[3];
		
		// Solids
		solids[0] = new Ellipsoid3D(250 + xOffset, 200 + yOffset, 0 + zOffset, 100*scale, 100*scale, 100*scale, mList[m], nList[n], mats[matActive]);
		solids[1] = new Cylinder3D(150f + xOffset, 300f + yOffset, 150f + zOffset, 50*scale, 150*scale, 25*scale, mList[m], nList[n], mats[matActive]);
		solids[2] = new Box3D(300 + xOffset, 400 + yOffset, -10 + zOffset, 50*scale, 100*scale, 75*scale, mats[matActive]);
		
		// Lights
		lights[0] = new LightAmbient( );
		lights[1] = new LightInfinite( new ColorType( 1.0, 1.0, 0.0 ), new Vector3D((float)0.0,(float)(1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		lights[2] = new LightInfinite( new ColorType( 1.0, 0.0, 1.0 ), new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(-1.0/Math.sqrt(2.0))));
		
	}
	
	private void makeScene1(int m, int n)
	{
		solids = new Solid[3];
		lights = new Light[2];
		
		// Surfaces
		solids[0] = new Torus3D(150 + xOffset, 300 + yOffset, 0 + zOffset, 10f*scale, 25f*scale, mList[m], nList[n], mats[matActive]);
		solids[1] = new Box3D(350 + xOffset, 400 + yOffset, -10 + zOffset, 50*scale, 100*scale, 75*scale, mats[matActive]);
		solids[2] = new Ellipsoid3D(250 + xOffset, 200 + yOffset, 0 + zOffset, 100*scale, 100*scale, 100*scale, mList[m], nList[n], mats[matActive]);
		
		// Lights
		lights[0] = new LightInfinite( new ColorType( 1.0, 0.0, 1.0 ), new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		lights[1] = new LightInfinite( new ColorType( 1.0, 1.0, 0.0 ), new Vector3D((float)0.0,(float)(1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		
	}
	
	private void makeScene2(int m, int n)
	{
		solids = new Solid[3];
		lights = new Light[2];

		// Surfaces
		// Solids
		solids[0] = new Ellipsoid3D(250 + xOffset, 200 + yOffset, 0 + zOffset, 40*scale, 100*scale, 60*scale, mList[m], nList[n], mats[matActive]);
		solids[1] = new Torus3D(300 + xOffset, 400 + yOffset, 0 + zOffset, 25f*scale, 50f*scale, mList[m], nList[n], mats[matActive]);
		solids[2] = new Box3D(400 + xOffset, 500 + yOffset, -10 + zOffset, 50*scale, 100*scale, 75*scale, mats[matActive]);
		
		// Lights
		lights[0] = new LightInfinite( new ColorType( 1.0, 0.0, 1.0 ), new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		lights[1] = new LightInfinite( new ColorType( 1.0, 1.0, 0.0 ), new Vector3D((float)0.0,(float)(1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));

	}
	
	private void makeScene3(int m, int n)
	{
		solids = new Solid[3];
		lights = new Light[2];

		// Surfaces
		// Solids
		solids[0] = new Ellipsoid3D(250 + xOffset, 200 + yOffset, 0 + zOffset, 100*scale, 100*scale, 100*scale, mList[m], nList[n], mats[matActive]);
		solids[1] = new Torus3D(450 + xOffset, 500 + yOffset, 0 + zOffset, 25f*scale, 50f*scale, mList[m], nList[n], mats[matActive]);
		solids[2] = new Box3D(300 + xOffset, 400 + yOffset, -10 + zOffset, 50*scale, 100*scale, 75*scale, mats[matActive]);
		
		// Lights
		lights[0] = new LightInfinite( new ColorType( 1.0, 0.0, 1.0 ), new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		lights[1] = new LightInfinite( new ColorType( 1.0, 1.0, 0.0 ), new Vector3D((float)0.0,(float)(1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));

	}
	
	private void makeScene4(int m, int n)
	{
		solids = new Solid[3];
		lights = new Light[2];

		// Surfaces
		// Solids
		solids[0] = new Ellipsoid3D(250 + xOffset, 100 + yOffset, 0 + zOffset, 70*scale, 100*scale, 70*scale, mList[m], nList[n], mats[matActive]);
		solids[1] = new Box3D(300 + xOffset, 400 + yOffset, -10 + zOffset, 150*scale, 50*scale, 25*scale, mats[matActive]);
		solids[2] = new Cylinder3D(150f + xOffset, 200f + yOffset, 100f + zOffset, 50*scale, 150*scale, 25*scale, mList[m], nList[n], mats[matActive]);
		
		// Lights
		lights[0] = new LightInfinite( new ColorType( 1.0, 0.0, 1.0 ), new Vector3D((float)0.0,(float)(-1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));
		lights[1] = new LightInfinite( new ColorType( 1.0, 1.0, 0.0 ), new Vector3D((float)0.0,(float)(1.0/Math.sqrt(2.0)),(float)(1.0/Math.sqrt(2.0))));

	}
	
	
	/*
	 * Rotate each solid within the scene by the viewing_quaternion
	 * 
	 */
	public void rotateScene( Quaternion viewing_quaternion, Vector3D viewing_center) 
	{		
		for (int i = 0; i < solids.length; i++)
		{
			solids[i].rotateMesh(viewing_quaternion, viewing_center);
		}	
	}
	
	public void drawScene( BufferedImage buff_frame, float[][] buff_depth )
	{
		for (int i = 0; i < solids.length; i++)
		{
			solids[i].draw(buff_frame, buff_depth, illumination, lights, activeLights, activeK); 
		}
	}
	
	public void getActiveLights()
	{
		activeLights = new Boolean[lights.length];
		for (int i = 0; i < activeLights.length; i++)
		{
			activeLights[i] = true;
		}
	}
	

	/************ Property Switches ************/
	
	public void toggleSpecular()
	{
		if (!activeK[2])
		{
			activeK[2] = true;
			System.out.println("Turning on Specular");
		} else {
			activeK[2] = false;
			System.out.println("Turning off Specular");
		}
	}
	
	public void toggleDiffuse()
	{
		if (!activeK[1])
		{
			activeK[1] = true;
			System.out.println("Turning on Diffuse");
		}
		else
		{
			activeK[1] = false;
			System.out.println("Turning off Diffuse");
		}
	}
	
	public void toggleAmbient()
	{
		if (!activeK[0])
		{
			activeK[0] = true;
			System.out.println("Turning on Ambient");
		}
		else
		{
			activeK[0] = false;
			System.out.println("Turning of Ambient");
		}
	}
	
	public void toggleLight( int lightId )
	{
		System.out.println("turning off a light");
		switch ( lightId )
		{
			case 1:
		    	if (lights.length > 0 )
		    	{
		    		if(!activeLights[0])
		    		{
		    			activeLights[0] = true;
		    		}
		    		else
		    		{
		    			activeLights[0] = false;
		    		}
		    	}
		    	break;
			
			case 2:
				if (lights.length > 1 )
		    	{
		    		if(!activeLights[1])
		    		{
		    			activeLights[1] = true;
		    		}
		    		else
		    		{
		    			activeLights[1] = false;
		    		}
		    	}
		    	break;
		    	
			case 3:
				if (lights.length > 2 )
		    	{
					if(!activeLights[2])
		    		{
		    			activeLights[2] = true;
		    		}
		    		else
		    		{
		    			activeLights[2] = false;
		    		}
		    	}
				break;
			
			default:
				break;
		}
		System.out.println(activeLights[0] + " " + activeLights[1] + " " + activeLights[2]);
	}
	
	private void toggleCoefficient(int k)
	{
		switch( k )
		{
		case 1:
			if(!activeK[0])
    		{
    			activeK[0] = true;
    		}
    		else
    		{
    			activeK[0] = false;
    		}
			break;
		case 2:
			if(!activeK[1])
    		{
    			activeK[1] = true;
    		}
    		else
    		{
    			activeK[1] = false;
    		}
			break;
		case 3:
			if(!activeK[2])
    		{
    			activeK[2] = true;
    		}
    		else
    		{
    			activeK[2] = false;
    		}
			break;
		
		default:
			break;
		}
	}
	
	public void incremenetM() 
	{
		m = m+1;
		if (m > 3)
		{
			m = 0;
		}
		System.out.println("M: " + mList[m]);
	}

	public void incremenetN() 
	{
		n = (n+1)%nList.length;
		if (n > 3)
		{
			n = 0;
		}
		System.out.println("N: " + nList[n]);
	}
	
	
	
	
	/************************ Transformations ***********************/
	
	public void transUp()
	{
		yOffset -= 50;
	}
	
	public void transDown()
	{
		yOffset += 50;
	}
	
	public void transLeft()
	{
		xOffset -= 50;
	}
	
	public void transRight()
	{
		xOffset += 50;
	}
	
	public void scaleUp()
	{
		scale += .25;
	}
	
	public void scaleDown()
	{
		scale -= .25;
		
		if (scale < .25 )
		{
			scale = .25f;
		}
	}

}
	