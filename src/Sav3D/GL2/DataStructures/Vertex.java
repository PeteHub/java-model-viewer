package Sav3D.GL2.DataStructures;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/18/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vertex
{
    float x, y, z;      // Vertices
    float nx, ny, nz;   // Normals
    float s0, t0;       // TexCoord0
    float s1, t1;       // TexCoord1
    float s2, t2;       // TexCoord2

    // 16 bytes of padding to align the structure for multiples of 32 bytes.
    // This is suggested for ATI cards
    float[] padding = { 0,0,0,0 };
    
    public Vertex( float x, float y, float z, float nx, float ny, float nz ) 
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.s0 = this.t0 = this.s1 = this.t1 = this.s2 = this.t2 = 0.0f;
    }

    public Vertex( float[] vertices, float[] normals )
    {
        this.x = vertices[0];
        this.y = vertices[1];
        this.z = vertices[2];
        this.nx = normals[0];
        this.ny = normals[1];
        this.nz = normals[2];
        this.s0 = this.t0 = this.s1 = this.t1 = this.s2 = this.t2 = 0.0f;
    }
    
    public float[] getVertices()
    {
        return new float[] { x, y, z };
    }
    
    public float[] getNormals()
    {
        return new float[] { nx, ny, nz };
    }
}
