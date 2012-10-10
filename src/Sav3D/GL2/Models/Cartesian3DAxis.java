package Sav3D.GL2.Models;

import javax.media.opengl.GL2;
import java.nio.FloatBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/17/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cartesian3DAxis
{

    // Material
    public static final float[] mAmbient  = { 0.2f, 0.2f, 0.2f, 1.0f };
    public static final float[] mDiffuse =  { 0.7f, 0.7f, 0.7f, 1.0f };
    public static final float[] mSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    public static final float[] mNone     = { 0.0f, 0.0f, 0.0f, 1.0f };

    public static final float[] mXAxis = { 1.0f, 0.0f, 0.0f, 1.0f };
    public static final float[] mYAxis = { 0.0f, 1.0f, 0.0f, 1.0f };
    public static final float[] mZAxis = { 0.0f, 0.0f, 1.0f, 1.0f };

    public static void draw( GL2 gl, float scale )
    {
        draw( gl, scale, new float[] { 0.0f, 0.0f, 0.0f, 1.0f } );
    }
    
    public static void draw ( GL2 gl, float axesLength, float[] origin)
    {

        // Box X-axis line
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mXAxis ) );
        gl.glColor3fv(mXAxis,0);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f( origin[0]-axesLength, origin[1], origin[2] );
        gl.glVertex3f( origin[0]+axesLength, origin[1], origin[2] );
        gl.glEnd();

        // Y axis line
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mYAxis ) );
        gl.glColor3fv(mYAxis,0);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f( origin[0], origin[1]-axesLength, origin[2] );
        gl.glVertex3f( origin[0], origin[1]+axesLength, origin[2] );
        gl.glEnd();

        // Box Z-axis line
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mZAxis ) );
        gl.glColor3fv(mZAxis,0);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f( origin[0], origin[1], origin[2]-axesLength );
        gl.glVertex3f( origin[0], origin[1], origin[2]+axesLength );
        gl.glEnd();        
        
    }
}
