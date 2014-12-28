import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*; 
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl

public class PA4_alex extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
{
	private final int DEFAULT_WINDOW_WIDTH=600;
	private final int DEFAULT_WINDOW_HEIGHT=600;
	private final float DEFAULT_LINE_WIDTH=1.0f;
	
	private FPSAnimator animator;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private BufferedImage buff_frame;
	private float[][] buff_depth;	// Use float[][] to store depth values
	private float zFar = -2000f;
	
	
	
	public int[] mList = {5, 12, 25, 50, 100};
	public int[] nList = {5, 12, 25, 50, 100};
	public int m = 0;
	public int n = 0;
	
	private boolean showWindow = true;
	
	private TestSuite test;
	
	/** The quaternion which controls the rotation of the world. */
    private Quaternion viewing_quaternion = new Quaternion();
    private Vector3D viewing_center = new Vector3D((float)(DEFAULT_WINDOW_WIDTH/2),(float)(DEFAULT_WINDOW_HEIGHT/2),(float)0.0);
    /** The last x and y coordinates of the mouse press. */
    private int last_x = 0, last_y = 0;
    /** Whether the world is being rotated. */
    private boolean rotate_world = false;
	
	public PA4_alex() 
	{
		// Set capabilities
		capabilities = new GLCapabilities(null);
	    capabilities.setDoubleBuffered(true);  // Enable Double buffering
	    
	    // Create Canvas
		canvas  = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
	    canvas.addMouseListener(this);
	    canvas.addMouseMotionListener(this);
	    canvas.addKeyListener(this);
	    canvas.setAutoSwapBufferMode(true); // true by default. Just to be explicit
	    canvas.setFocusable(true);
		getContentPane().add(canvas);
		
		// Create Animator
		animator = new FPSAnimator(canvas, 60); // drive the display loop @ 60 FPS
		
		// Window Settings
		if (showWindow)
		{
			setTitle("CS480/680 PA4");
			setSize( DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    setVisible(true);
		    setResizable(false);
		}
	}
	
	public static void main( String[] args )
	{
		 PA4_alex P = new PA4_alex();
		 P.run();
	}
	
	//*********************************************** 
	//  GLEventListener Interfaces
	//*********************************************** 
	public void init( GLAutoDrawable drawable) 
	{
		GL gl = drawable.getGL();
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f);
		gl.glLineWidth( DEFAULT_LINE_WIDTH );
		Dimension sz = this.getContentPane().getSize();
		buff_frame = new BufferedImage(sz.width,sz.height,BufferedImage.TYPE_3BYTE_BGR);
		
		// Initialize depth buffer
		buff_depth = new float[DEFAULT_WINDOW_WIDTH][DEFAULT_WINDOW_HEIGHT];
		
		
		
		clearPixelBuffer();
		
		test = new TestSuite();
	}
	
	// Redisplaying graphics
	public void display(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();
		WritableRaster wr = buff_frame.getRaster();
		DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
		byte[] data = dbb.getData();

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glDrawPixels (buff_frame.getWidth(), buff_frame.getHeight(),
				GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE,
				ByteBuffer.wrap(data));

		update();
	}

	public void update()
	{
		clearPixelBuffer();
		
		// Create Solids, Materials, and Lights
		test.writeCurrentScene();
		// Apply Transformations
		test.rotateScene(viewing_quaternion, viewing_center);
		// Draw Pixels
		test.drawScene(buff_frame, buff_depth);
	}
	
	
	// Window size change
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		// deliberately left blank
	}
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
		      boolean deviceChanged)
	{
		// deliberately left blank
	}
		
	void clearPixelBuffer()
	{
		Graphics2D gF = buff_frame.createGraphics();
		gF.setColor(Color.WHITE);
		gF.fillRect(0, 0, buff_frame.getWidth(), buff_frame.getHeight());
		gF.dispose();
		
		for (int i = 0; i < DEFAULT_WINDOW_WIDTH; i++)
		{
			for (int j = 0; j < DEFAULT_WINDOW_HEIGHT; j++)
			{
				buff_depth[i][j] = zFar;
			}
		}
	}
	
	public void run() 
	{
		animator.start();
	}
	
	
	
	//*********************************************** 
	//          KeyListener Interfaces
	//*********************************************** 
	public void keyTyped(KeyEvent key)
	{
	//      Q,q: quit 
	//		M,m: incrementM
	// 		N,n: incrementN

	    switch ( key.getKeyChar() ) 
	    {
	    // Quit
	    case 'Q' :
	    case 'q' : 
	    	new Thread()
	    	{
	          	public void run() { animator.stop(); }
	        }.start();
	        System.exit(0);
	        break;
	    // Test  
	    case 'T':
	    case 't':
	    	test.cycleScenes();
	    	System.out.println("Drawing New Scene");
	    	break;
	    // Columns Increment    
	    case 'M':
	    case 'm':
	    	test.incremenetM();
	    	break;
	    // Rows Increment	
	    case 'N':
	    case 'n':
	    	test.incremenetN();
	    	break;
	    // Scale Down
	    case '<':
	    	test.scaleDown();
	    	break;
	    // Scale Up	
	    case '>':
	    	test.scaleUp();
	    	break;
	    // No Illumination	
	    case '0':
	    	test.setIllumination(0);
	    	System.out.println("Turning off Shading");
	    	break;
	    // Flat Shading	
	    case 'F':
	    case 'f':
	    	test.setIllumination(1);
	    	System.out.println("Turning on Flat Shading");
	    	break;
	    // Gouraud Shading	
	    case 'G':
	    case 'g':
	    	test.setIllumination(2);
	    	System.out.println("Turning on Gouraud Shading");
	    	break;
	    // Phong Shading	
	    case 'P':
	    case 'p':
	    	test.setIllumination(3);
	    	System.out.println("Turning on Phong Shading");
	    	break;
	    // Specular
	    case 'S':
	    case 's':
	    	test.toggleSpecular();
	    	break;
	    // Diffuse
	    case 'D':
	    case 'd':
	    	test.toggleDiffuse();
	    	break;
	    // Ambient
	    case 'A':
	    case 'a':
	    	test.toggleAmbient();	
	    	break;
	    // Toggle Light 1	
	    case '1':
	    	test.toggleLight(1);
	    	break;
	    // Toggle Light 2	    	
	    case '2':
	    	test.toggleLight(2);
	    	break;
		// Toggle Light 3	
	    case '3':
	    	test.toggleLight(3);
	    	break;
	    // Toggle Material
	    case 'C':
	    case 'c':
	    	test.cycleMaterials();
	    	System.out.println("Generating new material");
	    	break;
	    	
	    default :
	        break;
	    }
	}
	
	 /*********************************************** 
	   *       ArrowKeyListener Interfaces
	   ***********************************************
	   *
	   *	up-arrow : Move Solids up 50px
	   *
	   *	down-arrow : Move Solids down 50px
	   *
	   *	left-arrow : Move Solids left 50px
	   *
	   *	right-arrow : Move Solids right 50px
	   *
	   */
	public void keyPressed(KeyEvent key)
	{
	    switch (key.getKeyCode()) 
	    {
	    case KeyEvent.VK_ESCAPE:
	    	new Thread()
	        {
	    		public void run()
	    		{
	    			animator.stop();
	    		}
	        }.start();
	        System.exit(0);
	        break;
	        
	    // Up arrow key
	    case KeyEvent.VK_KP_UP :
	    case KeyEvent.VK_UP    :
	        test.transUp();
	        update();
	        break;
	     
	    // Down arrow key
	    case KeyEvent.VK_KP_DOWN :
	    case KeyEvent.VK_DOWN :
	    	test.transDown();
	    	update();
	    	break;
	    
	    // Left arrow key
	    case KeyEvent.VK_KP_LEFT :
	    case KeyEvent.VK_LEFT    :
	        test.transLeft();
	        update();
	        break;
	      
	    // Right arrow key
	    case KeyEvent.VK_KP_RIGHT :
	    case KeyEvent.VK_RIGHT    :
	        test.transRight();
	        update();
	        break;
	    	
	    default:
	        break;
	    }
	}

	public void keyReleased(KeyEvent key)
	{
		// deliberately left blank
	}

	//************************************************** 
	// MouseListener and MouseMotionListener Interfaces
	//************************************************** 
	public void mouseClicked(MouseEvent mouse)
	{
		// deliberately left blank
	}

	public void mousePressed(MouseEvent mouse)
	{
		int button = mouse.getButton();
		if ( button == MouseEvent.BUTTON1 )
		{
			last_x = mouse.getX();
			last_y = mouse.getY();
			rotate_world = true;
		}
	}

	public void mouseReleased(MouseEvent mouse)
	{
		int button = mouse.getButton();
	    if ( button == MouseEvent.BUTTON1 )
	    {
	      rotate_world = false;
	    }
	}

	public void mouseMoved( MouseEvent mouse)
	{
		// Deliberately left blank
	}

	public void mouseDragged( MouseEvent mouse )
	{
		 if (this.rotate_world) {
		      // get the current position of the mouse
		      final int x = mouse.getX();
		      final int y = mouse.getY();

		      // get the change in position from the previous one
		      final int dx = x - this.last_x;
		      final int dy = y - this.last_y;

		      // create a unit vector in the direction of the vector (dy, dx, 0)
		      final float magnitude = (float)Math.sqrt(dx * dx + dy * dy);
		      if(magnitude > 0.0001)
		      {
		    	  // define axis perpendicular to (dx,-dy,0)
		    	  // use -y because origin is in upper lefthand corner of the window
		    	  final float[] axis = new float[] { -(float) (dy / magnitude),
		    			  (float) (dx / magnitude), 0 };

		    	  // calculate appropriate quaternion
		    	  final float viewing_delta = 3.1415927f / 180.0f;
		    	  final float s = (float) Math.sin(0.5f * viewing_delta);
		    	  final float c = (float) Math.cos(0.5f * viewing_delta);
		    	  final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
		    			  * axis[2]);
		    	  this.viewing_quaternion = Q.multiply(this.viewing_quaternion);

		    	  // normalize to counteract acccumulating round-off error
		    	  this.viewing_quaternion.normalize();

		    	  // save x, y as last x, y
		    	  this.last_x = x;
		    	  this.last_y = y;
		          
		    	  update();
		      }
		 }
	}

	public void mouseEntered( MouseEvent mouse)
	{
		// Deliberately left blank
	}

	public void mouseExited( MouseEvent mouse)
	{
		// Deliberately left blank
	} 

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub		
	}
	
	
}