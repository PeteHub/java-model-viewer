package Sav3D.GL2.Models;

import javax.media.opengl.GL2;
import java.nio.FloatBuffer;

public class SimpleBox
{

    // Vertices
    public static final float[] v1 =  {   1.000000f, -1.000000f, -1.000000f };
    public static final float[] v2 =  {   1.000000f, -1.000000f,  1.000000f };
    public static final float[] v3 =  {  -1.000000f, -1.000000f,  1.000000f };
    public static final float[] v4 =  {  -1.000000f, -1.000000f, -1.000000f };
    public static final float[] v5 =  {   1.000000f,  1.000000f, -1.000000f };
    public static final float[] v6 =  {   1.000000f,  1.000000f,  1.000001f };
    public static final float[] v7 =  {  -1.000000f,  1.000000f,  1.000000f };
    public static final float[] v8 =  {  -1.000000f,  1.000000f, -1.000000f };

    // Normals
    public static final float[] vn1 = {  0.000000f, -1.000000f,  0.000000f };
    public static final float[] vn2 = {  0.000000f,  1.000000f,  0.000000f };
    public static final float[] vn3 = {  1.000000f,  0.000000f,  0.000000f };
    public static final float[] vn4 = { -0.000000f, -0.000000f,  1.000000f };
    public static final float[] vn5 = { -1.000000f, -0.000000f, -0.000000f };
    public static final float[] vn6 = {  0.000000f,  0.000000f, -1.000000f };

    // Material
    public static final float[] mAmbient  = { 0.2f, 0.2f, 0.2f, 1.0f };
    public static final float[] mDiffuse =  { 0.7f, 0.7f, 0.7f, 1.0f };
    public static final float[] mSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    public static final float[] mNone     = { 0.0f, 0.0f, 0.0f, 1.0f };

    public static final float[] mXAxis = { 1.0f, 0.0f, 0.0f, 1.0f };
    public static final float[] mYAxis = { 0.0f, 1.0f, 0.0f, 1.0f };
    public static final float[] mZAxis = { 0.0f, 0.0f, 1.0f, 1.0f };



    public static void draw( GL2 gl )
    {
        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn1,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn1,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn2,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn2,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn3,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn3,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glVertex3fv( v2, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn4,0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn4,0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn5,0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn5,0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn6,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn6,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glEnd();

    }

    public static void drawColored( GL2 gl )
    {

        // Initialize materials
        // glMaterial will define specular and emission

        // Just in case the shader program is disabled we set the color here
        // This should not be taken into account normally
        gl.glColor3fv( mDiffuse, 0);

        gl.glMaterialfv( gl.GL_FRONT, gl.GL_AMBIENT,  FloatBuffer.wrap( mAmbient ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mDiffuse ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_SPECULAR, FloatBuffer.wrap( mSpecular ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_EMISSION, FloatBuffer.wrap( mNone ) );
        gl.glMaterialf(  gl.GL_FRONT, gl.GL_SHININESS, 25.0f);

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn1,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn1,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn2,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn2,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn3,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn3,0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glVertex3fv( v2, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn4,0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v6, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn4,0 );
        gl.glVertex3fv( v2, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v3, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn5,0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v7, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn5,0 );
        gl.glVertex3fv( v3, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn6,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v1, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glEnd();

        gl.glBegin( gl.GL_TRIANGLES );
        gl.glNormal3fv( vn6,0 );
        gl.glVertex3fv( v5, 0 );
        gl.glVertex3fv( v4, 0 );
        gl.glVertex3fv( v8, 0 );
        gl.glEnd();

    }
}