package Sav3D.GL2.Models;

import Sav3D.GL2.DataStructures.Vertex;

import javax.media.opengl.GL2;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/18/12
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMesh
{
    GL2 gl;
    private Vertex[] vertexData;
    private short[] indexData;
    
    private int[] VertexVBOID;
    private int[] IndexVBOID;


    private final float[] mAmbient  = { 0.2f, 0.2f, 0.2f, 1.0f };
    private final float[] mDiffuse =  { 0.7f, 0.7f, 0.7f, 1.0f };
    private final float[] mSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mNone     = { 0.0f, 0.0f, 0.0f, 1.0f };

    public SimpleMesh( GL2 gl )
    {
        LoadMeshData();
        
        this.gl = gl;

        
        // VERTEX BUFFER OBJECT
        // ====================
        
        this.VertexVBOID = new int[1];

        // Generate a VBO and bind it for vertices
        gl.glGenBuffers( 1, VertexVBOID, 0);
        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, VertexVBOID[0] );

        // Generate Vertex Data Buffer
        // This includes the normals as well
        long sizeInBytes = vertexData.length*6*4;  // vertices+normals * float
        FloatBuffer vertexDataBuffer = FloatBuffer.allocate( vertexData.length * 6); // vertices+normals
        
        for( Vertex v : vertexData)
        {
            vertexDataBuffer.put( v.getVertices() );
            vertexDataBuffer.put( v.getNormals() );
        }

        vertexDataBuffer.rewind();

        gl.glBufferData( gl.GL_ARRAY_BUFFER, sizeInBytes, vertexDataBuffer , gl.GL_STATIC_DRAW );

        //INDEX BUFFER OBJECT
        //===================


        this.IndexVBOID = new int[1];

        //Generate a VBO and bind it for indices
        gl.glGenBuffers( 1, IndexVBOID, 0);
        gl.glBindBuffer( gl.GL_ELEMENT_ARRAY_BUFFER, IndexVBOID[0] );

        //Generate Index Data Buffer
        //This includes the normals as well
        sizeInBytes = indexData.length*2; // Short
        ShortBuffer indexDataBuffer = ShortBuffer.allocate( indexData.length );

        for( short s : indexData)
            indexDataBuffer.put(s);
        indexDataBuffer.rewind();

        gl.glBufferData( gl.GL_ELEMENT_ARRAY_BUFFER, sizeInBytes, indexDataBuffer , gl.GL_STATIC_DRAW );

        // AT THIS POINT WE HAVE UPLOADED ALL THE VERTEX,NORMAL and INDEX DATA INTO THE GPU VIDEO MEMORY
        // WE COULD NULL-OUT THE LOCAL DATA STRUCTURES
    }

    public void drawMesh()
    {
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_AMBIENT,  FloatBuffer.wrap( mAmbient ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mDiffuse ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_SPECULAR, FloatBuffer.wrap( mSpecular ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_EMISSION, FloatBuffer.wrap( mNone ) );
        gl.glMaterialf(  gl.GL_FRONT, gl.GL_SHININESS, 25.0f);

        // Enable Client States
        gl.glEnableClientState( gl.GL_VERTEX_ARRAY );
        gl.glEnableClientState( gl.GL_NORMAL_ARRAY );

        // Bind the Vertex Buffer
        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, VertexVBOID[0] );

        // Vertex data starts at 0 offset and repeats every 24 bytes
        // Normal data starts 12 bytes in and repeats every 24 bytes
        gl.glVertexPointer( 3, gl.GL_FLOAT, 24, 0 );
        gl.glNormalPointer( gl.GL_FLOAT, 24, 12 );
        
        // Bind the Index Buffer
        gl.glBindBuffer( gl.GL_ELEMENT_ARRAY_BUFFER, IndexVBOID[0] );

        // Draw indexed vertices
        gl.glDrawElements( gl.GL_TRIANGLES, indexData.length, gl.GL_UNSIGNED_SHORT, 0 );

        // Disable client states
        gl.glDisableClientState( gl.GL_VERTEX_ARRAY );
        gl.glDisableClientState( gl.GL_NORMAL_ARRAY );
    }

    private void LoadMeshData()
    {
        vertexData = new Vertex[12*3];

        // Vertices
        float[] v1 =  {   1.0f, -1.0f, -1.0f };
        float[] v2 =  {   1.0f, -1.0f,  1.0f };
        float[] v3 =  {  -1.0f, -1.0f,  1.0f };
        float[] v4 =  {  -1.0f, -1.0f, -1.0f };
        float[] v5 =  {   1.0f,  1.0f, -1.0f };
        float[] v6 =  {   1.0f,  1.0f,  1.0f };
        float[] v7 =  {  -1.0f,  1.0f,  1.0f };
        float[] v8 =  {  -1.0f,  1.0f, -1.0f };

        float[] vn1 = {  0.0f, -1.0f,  0.0f };
        float[] vn2 = {  0.0f,  1.0f,  0.0f };
        float[] vn3 = {  1.0f,  0.0f,  0.0f };
        float[] vn4 = { -0.0f, -0.0f,  1.0f };
        float[] vn5 = { -1.0f, -0.0f, -0.0f };
        float[] vn6 = {  0.0f,  0.0f, -1.0f };
        
        vertexData[0]  = new Vertex(v1,vn1);
        vertexData[1]  = new Vertex(v2,vn1);
        vertexData[2]  = new Vertex(v3,vn1);
        vertexData[3]  = new Vertex(v1,vn1);
        vertexData[4]  = new Vertex(v3,vn1);
        vertexData[5]  = new Vertex(v4,vn1);
              
        vertexData[6]  = new Vertex(v5,vn2);
        vertexData[7]  = new Vertex(v8,vn2);
        vertexData[8]  = new Vertex(v7,vn2);
        vertexData[9]  = new Vertex(v5,vn2);
        vertexData[10] = new Vertex(v7,vn2);
        vertexData[11] = new Vertex(v6,vn2);
              
        vertexData[12] = new Vertex(v1,vn3);
        vertexData[13] = new Vertex(v5,vn3);
        vertexData[14] = new Vertex(v6,vn3);
        vertexData[15] = new Vertex(v1,vn3);
        vertexData[16] = new Vertex(v6,vn3);
        vertexData[17] = new Vertex(v2,vn3);
              
        vertexData[18] = new Vertex(v2,vn4);
        vertexData[19] = new Vertex(v6,vn4);
        vertexData[20] = new Vertex(v7,vn4);
        vertexData[21] = new Vertex(v2,vn4);
        vertexData[22] = new Vertex(v7,vn4);
        vertexData[23] = new Vertex(v3,vn4);
              
        vertexData[24] = new Vertex(v3,vn5);
        vertexData[25] = new Vertex(v7,vn5);
        vertexData[26] = new Vertex(v8,vn5);
        vertexData[27] = new Vertex(v3,vn5);
        vertexData[28] = new Vertex(v8,vn5);
        vertexData[29] = new Vertex(v4,vn5);
              
        vertexData[30] = new Vertex(v5,vn6);
        vertexData[31] = new Vertex(v1,vn6);
        vertexData[32] = new Vertex(v4,vn6);
        vertexData[33] = new Vertex(v5,vn6);
        vertexData[34] = new Vertex(v4,vn6);
        vertexData[35] = new Vertex(v8,vn6);
        
        indexData = new short[36];
        for ( short idx = 0; idx < indexData.length; idx++ )
            indexData[idx] = idx;

    }
}
