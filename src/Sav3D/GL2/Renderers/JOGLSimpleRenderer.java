package Sav3D.GL2.Renderers;

import Sav3D.GL2.Lights.SimplePointLight;
import Sav3D.GL2.Models.Cartesian3DAxis;
import Sav3D.GL2.Models.GW2Model.GW2Model;
import Sav3D.GL2.Models.Gr2Model.Gr2Model;
import Sav3D.GL2.Models.SimpleMesh;
import Sav3D.GL2.Shaders.JOGLSimpleShader;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.Screenshot;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/5/12
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class JOGLSimpleRenderer implements GLEventListener
{
    private final FPSAnimator fpsAnimator;
    private final GLCanvas canvas;

    // Shader Programs
    private int shaderProgram;    
    private JOGLSimpleShader vGL2Shader;    // Vertex Shader
    private JOGLSimpleShader fGL2Shader;    // Fragment Shader
    
    // Light
    private SimplePointLight light0;

    // Mesh
    //SimpleMesh mesh;
    Gr2Model swtor_model;
    GW2Model gw2_model;

    // rotating the scene
    public float view_rotx = 15.0f;
    public float view_roty = 0.0f;
    
    // Mouse Movement State
    private int prevMouseX = 0;
    private int prevMouseY = 0;
    private boolean leftMouseButtonDown = false;

    private boolean takeScreenShotPNG = false;
    
    

    private float[] fLightPosition= { 0.0f, 0.0f, 1.0f, 0.0f };

    public JOGLSimpleRenderer( Frame renderTargetFrame, int frameRate )
    {
        GLProfile profile = GLProfile.getDefault();

        GLCapabilities caps = new GLCapabilities(profile);

        // Set 2x Anti-Aliasing for now
        caps.setSampleBuffers(true);
        caps.setNumSamples(8);


        //caps.setDoubleBuffered(true);

        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        
        canvas.addKeyListener( new KeyAdapter()
        {
            // Apparently, windows does not generate a keypress event for
            // Print Screen.. go figure
            @Override
            public void keyReleased( KeyEvent e )
            {
                // Take Screenshot
                if ( e.getKeyCode() == KeyEvent.VK_PRINTSCREEN )
                   takeScreenShotPNG = true;
            }
        });
        canvas.addMouseListener( new MouseAdapter()
        {
            @Override
            public void mousePressed( MouseEvent e )
            {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
                
                if ( ( e.getModifiers() & e.BUTTON1_MASK ) != 0 )
                    leftMouseButtonDown = true;
            }

            @Override
            public void mouseReleased( MouseEvent e )
            {
                if ( (e.getModifiers() & e.BUTTON1_MASK ) != 0 )
                {
                    leftMouseButtonDown = false;
                    prevMouseX = e.getX();
                    prevMouseY = e.getY();
                }
            }
        } );

        canvas.addMouseMotionListener( new MouseAdapter()
        {

            @Override
            public void mouseDragged( MouseEvent e )
            {

                int x = e.getX();
                int y = e.getY();

                if ( leftMouseButtonDown )
                {
                    
                    float thetaY = 360.0f * ( (float)( x - prevMouseX ) / (float)canvas.getWidth() );
                    float thetaX = 360.0f * ( (float)( y - prevMouseY ) / (float)canvas.getHeight() );

                    view_rotx += thetaX;
                    view_roty += thetaY;

                }
                prevMouseX = x;
                prevMouseY = y;
            }
        });
        
        // Add the canvas to the render target frame
        renderTargetFrame.add(canvas);

        // Our game loop will run at frameRate fps max
        this.fpsAnimator = new FPSAnimator(canvas, frameRate);
    }

    private void TakeScreenShotPNG()
    {
        this.takeScreenShotPNG = false;

        BufferedImage ScreenShotData = Screenshot.readToBufferedImage(canvas.getX(),canvas.getY(),
                canvas.getWidth(),
                canvas.getHeight(), false);

        String fileName = String.valueOf(System.nanoTime()) + ".png";

        File ScreenShotFile = new File( fileName  );

        try { ImageIO.write( ScreenShotData, "png", ScreenShotFile ); }
        catch ( IOException e1 ) { e1.printStackTrace(); }

        System.out.println("Screenshot saved: " + fileName);
    }

    public boolean StartRenderingLoop()
    {
        // true if started due to this call, otherwise false, ie started already or unable to start.
        return this.fpsAnimator.start();
    }

    public boolean StopRenderingLoop()
    {
        // true if stopped due to this call, otherwise false, ie not started or unable to stop.
        return this.fpsAnimator.stop();
    }

    @Override
    public void init( GLAutoDrawable glAutoDrawable )
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        
        //gl.glShadeModel( gl.GL_SMOOTH );
        //gl.glEnable( gl.GL_BLEND );
        //gl.setSwapInterval(1);

        gl.glClear( gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT | gl.GL_STENCIL_BUFFER_BIT );


        // Set culling options
        gl.glFrontFace( gl.GL_CW );
        gl.glCullFace( gl.GL_FRONT );
        gl.glEnable( gl.GL_CULL_FACE );

        // Initialize the light
        //gl.glEnable( gl.GL_LIGHTING );
        gl.glLightModeli( gl.GL_LIGHT_MODEL_LOCAL_VIEWER, gl.GL_TRUE);
        this.light0 = new SimplePointLight( gl, gl.GL_LIGHT0 );
        //this.light0.Enable();

        // Initialize Depth Buffer Test
        gl.glClearDepthf(10.f);
        gl.glEnable(gl.GL_DEPTH_TEST );
        
        // Set the clear color for the color buffer to light gray (around (217,217,217) or #D9D9D9)
        gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);

        // Load, compile and validate the shader programs
        //ShaderLoader(gl, "vshader.glsl", "fshader.glsl" );
        //ShaderLoader(gl, "gw2_vshader.glsl", "gw2_fshader.glsl" );
        ShaderLoader(gl, "swtor_vshader.glsl", "swtor_fshader.glsl" );
        
        // init vertex buffers, index buffers, texture buffers
        // aka, load the model mesh data :)
        //mesh = new SimpleMesh( gl );
        swtor_model = new Gr2Model( gl, this.shaderProgram, "rancor.gr2" );
        //gw2_model = new GW2Model( gl, this.shaderProgram, "1AA44C.dat");
        //gw2_model = new GW2Model( gl, this.shaderProgram, "10D952.dat");
        //gw2_model = new GW2Model( gl, this.shaderProgram, "227BDD.dat");
        //gw2_model = new GW2Model( gl, this.shaderProgram, "52539.dat");
        
        // Enable textures
        gl.glEnable( gl.GL_TEXTURE_2D );
    }

    private void ShaderLoader( GL2 gl, String vsFileName, String fsFileName )
    {
        // Load the shaders
        /////////////////////////
        try
        {
            this.vGL2Shader = new JOGLSimpleShader( gl, vsFileName, gl.GL_VERTEX_SHADER );
            this.fGL2Shader = new JOGLSimpleShader( gl, fsFileName, gl.GL_FRAGMENT_SHADER );
        }
        catch ( IOException ex )
        {
            System.err.println( ex.getMessage() );
            StopRenderingLoop();
            System.exit(1);
        }

        // Create the shader program
        this.shaderProgram = gl.glCreateProgram();

        gl.glAttachShader( this.shaderProgram, this.vGL2Shader.GetShaderId() );
        gl.glAttachShader( this.shaderProgram, this.fGL2Shader.GetShaderId() );

        // Bind tangent attribute to vertex shader
        gl.glBindAttribLocation( this.shaderProgram, 1, "tangent");

        gl.glLinkProgram( this.shaderProgram );

        // Linking Error Checking

        IntBuffer shaderLog = Buffers.newDirectIntBuffer( 1 );
        IntBuffer shaderLogLength = Buffers.newDirectIntBuffer(1);
        ByteBuffer shaderLogText;

        gl.glGetProgramiv( shaderProgram, gl.GL_LINK_STATUS, shaderLog );

        if ( shaderLog.get(0) > 1)
        {
            shaderLogText = Buffers.newDirectByteBuffer( shaderLog.get(0) );
            gl.glGetProgramInfoLog( shaderProgram, shaderLog.get( 0 ), shaderLogLength, shaderLogText );

            while ( shaderLogText.hasRemaining() )
                System.out.print( Character.toChars( shaderLogText.get() ));
        }

        gl.glValidateProgram( this.shaderProgram );

        shaderLog.clear();
        shaderLogLength.clear();

        gl.glGetProgramiv( shaderProgram, gl.GL_VALIDATE_STATUS, shaderLog );

        if ( shaderLog.get(0) > 1)
        {
            shaderLogText = Buffers.newDirectByteBuffer( shaderLog.get(0) );
            gl.glGetProgramInfoLog( shaderProgram, shaderLog.get( 0 ), shaderLogLength, shaderLogText );

            while ( shaderLogText.hasRemaining() )
                System.out.print( Character.toChars( shaderLogText.get() ));
        }

        gl.glUseProgram( this.shaderProgram );
    }

    @Override
    public void dispose( GLAutoDrawable glAutoDrawable )
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void display( GLAutoDrawable glAutoDrawable )
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT );

        // RENDERING THE SCENE
        //////////////////////

//        gl.glMatrixMode( gl.GL_MODELVIEW );
//        gl.glLoadIdentity();
//        gl.glScalef (1.0f, 1.0f, -1.0f);

        gl.glPushMatrix();

            // Move the whole scene 10 units away from us, so we can actually see it
            //gl.glTranslatef(0.0f, -35.0f,-115.f);
            gl.glTranslatef(0.0f, -0.5f,-2.5f);

            // Set the light
            this.light0.SetPosition(this.fLightPosition);
            //Cartesian3DAxis.draw( gl, 0.5f, this.fLightPosition );
            //this.light0.DrawLightPoint();

            gl.glPushMatrix();
                // Rotate
                gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);

                // Draw the axes
                gl.glUseProgram(0);
                Cartesian3DAxis.draw(gl, 100.f);

                //gl.glTranslatef(0.0f, -0.0f,20.0f);

                // Draw the box
                // Moa Bird needs to be realigned
                //gl.glRotatef(90.f, 1.0f, 0.0f, 0.0f);
                //gl.glRotatef(180.f, 0.0f, 0.0f, 1.0f);
                gl.glUseProgram(this.shaderProgram);
                swtor_model.drawModel();
                //gw2_model.drawModel();

            gl.glPopMatrix();
        gl.glPopMatrix();

        if ( this.takeScreenShotPNG )
            TakeScreenShotPNG();
        
    }

    @Override
    public void reshape( GLAutoDrawable glAutoDrawable, int x, int y, int width, int height )
    {
        // If the canvas display size changes, we have to recalculate our
        // projection matrices

        GL2 gl = glAutoDrawable.getGL().getGL2();
        GLU glu = new GLU();

        // Adjust the viewport to the new dimensions of the render target
        gl.glViewport(0,0,width,height);

        if (height <= 0) {
            height = 1;
        }
        float ratio = (float) width / (float) height;
        System.out.println("Display Aspect Ratio: " +ratio);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(50.0f, ratio, 1.0, 1000.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
