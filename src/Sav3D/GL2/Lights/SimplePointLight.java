package Sav3D.GL2.Lights;

import javax.media.opengl.GL2;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/12/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimplePointLight
{
    private GL2 gl;

    // The light Id used to refer to this light GL_LIGHT<id>
    private int iLightId;

    // Light that comes from all directions equally and is scattered in all directions equally
    // Good approximation for uniform light that affects the whole scene equally.
    // Because of these properties, this light can not be used to define shapes (everything is equally lit).
    private float[] fAmbient;

    // Light that has a source. The intensity depends whether the surface it hits faces the light or not.
    // It scatters equally in all directions. This light defines the shapes of 3d objects.
    private float[] fDiffuse;

    // Similar to diffuse lights, specular lights also have a source, however they are reflected in a particular
    // direction based on the surface's shape. This light produces shiney highlights and also good for distinguishing
    // plaster/metal/plastic surfaces based on their reflective properties.
    private float[] fSpecular;
    
    private float[] vLightPosition;

    private boolean  bIsEnabled;

    public SimplePointLight( GL2 gl, int GL2_LIGHT_ID )
    {
        this.gl = gl;
        
        // Set default light values
        // The colors should be defined by the material of the surfaces
        this.fAmbient  = new float[] { 0.4f, 0.4f, 0.4f, 1.0f };
        this.fDiffuse  = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        this.fSpecular = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

        // Set default position to origin
        this.vLightPosition = new float[] { 0.f, 0.f, 0.f, 0.0f };

        this.iLightId = GL2_LIGHT_ID;

        // Initialize the light
        gl.glLightfv( this.iLightId, gl.GL_AMBIENT, this.fAmbient, 0);
        gl.glLightfv( this.iLightId, gl.GL_DIFFUSE, this.fDiffuse, 0);
        gl.glLightfv( this.iLightId, gl.GL_SPECULAR, this.fSpecular, 0);
        gl.glLightfv( this.iLightId, gl.GL_POSITION, this.vLightPosition, 0);

        //gl.glLightfv( this.iLightId, gl.GL_CONSTANT_ATTENUATION, fDiffuse,0);
        //gl.glLightfv( this.iLightId, gl.GL_LINEAR_ATTENUATION, fDiffuse,0);
        //gl.glLightfv( this.iLightId, gl.GL_QUADRATIC_ATTENUATION, fDiffuse,0);

        // Disable the light by default
        this.bIsEnabled = false;
        gl.glDisable( this.iLightId );
    }

    public void SetPosition( float[] vNewPosition )
    {
        this.vLightPosition = vNewPosition;
        gl.glLightfv( this.iLightId, gl.GL_POSITION, this.vLightPosition, 0);
    }

    public void SetPositionCopy( float[] vNewPosition )
    {
        this.vLightPosition = (float[])vNewPosition.clone();
        gl.glLightfv( this.iLightId, gl.GL_POSITION, this.vLightPosition, 0);
    }

    public void SetPosition ( float v1, float v2, float v3, float v4 )
    {
        this.vLightPosition = new float[] { v1, v2, v3, v4 };
        gl.glLightfv( this.iLightId, gl.GL_POSITION, this.vLightPosition, 0);
    }
    
    public float[] GetPositionCopy()
    {
        return (float[])this.vLightPosition.clone();
    }

    public float[] GetPosition()
    {
        return this.vLightPosition;
    }
    
    public boolean isEnabled()
    {
        return this.bIsEnabled;
    }
    
    public void Enable()
    {
        this.bIsEnabled = true;
        this.gl.glEnable( this.iLightId );
    }

    public void Disable()
    {
        this.bIsEnabled = false;
        this.gl.glDisable( this.iLightId );
    }

    public void DrawLightPoint()
    {
        gl.glPointSize( 8.0f );
        gl.glBegin( gl.GL_POINTS );
        gl.glVertex4fv( this.vLightPosition, 0 );
        gl.glEnd();
    }
}
