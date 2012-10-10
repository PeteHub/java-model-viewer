package Sav3D.GL2.Models.Gr2Model;

import Sav3D.Utility.LittleEndianDataInputStream;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/19/12
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gr2Model
{
    // Gr2 File Information
    private final String fileName;
    private Gr2Header header;
    private Gr2ModelInfo modelInfo;
    private Gr2MeshInfo meshInfo;

    // Vertex And Index Data
    private Gr2Vertex[] vertexData;
    private short[] indexData;
    
    private int stride;
    
    // OpenGL context
    private GL2 gl;

    // Vertex and Index buffer objects for OpenGL 2
    private int[] VertexVBOID;
    private int[] IndexVBOID;

    // Textures
    private int[] TextureID;
    private final int TEXTURE_DIFFUSE  = 0;
    private final int TEXTURE_NORMAL   = 1;
    private final int TEXTURE_SPECULAR = 2;
    
    private Texture diffuseTexture;
    private Texture normalTexture;
    private Texture specularTexture;

    private int tangentLocation;
    private int diffuseLocation;
    private int normalLocation;
    private int specularLocation;

    // Default material properties
    private final float[] mAmbient  = { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mDiffuse =  { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mNone     = { 0.0f, 0.0f, 0.0f, 1.0f };
    private final float   mSpecularShininess = 65;

    public Gr2Model( GL2 gl, int shaderProgramID, String modelFile )
    {
        this.gl = gl;
        this.fileName = modelFile;

        this.tangentLocation = gl.glGetAttribLocation( shaderProgramID, "tangent" );
        this.diffuseLocation = gl.glGetUniformLocation( shaderProgramID, "diffuseTexture" );
        this.normalLocation = gl.glGetUniformLocation( shaderProgramID, "normalTexture" );
        this.specularLocation = gl.glGetUniformLocation( shaderProgramID, "specularTexture" );

        // Load the data from the model file
        LoadFile();

        // VERTEX BUFFER OBJECT
        // ====================

        this.VertexVBOID = new int[1];

        // Generate a VBO and bind it for vertices
        gl.glGenBuffers( 1, VertexVBOID, 0);
        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, VertexVBOID[0] );

        // Generate Vertex Data Buffer
        // This includes the normals as well

        this.stride = (3+3+2+3)*4; // vertices+normals+uv * float
        long sizeInBytes = vertexData.length * stride;

        //FloatBuffer vertexDataBuffer = FloatBuffer.allocate( vertexData.length * 8); // vertices+normals+uv
        FloatBuffer vertexDataBuffer = FloatBuffer.allocate( vertexData.length * 11); // vertices+normals+uv+tangent

        for( Gr2Vertex v : vertexData)
        {
            vertexDataBuffer.put( v.getVertex3f() );
            vertexDataBuffer.put( v.getNormal3f() );
            vertexDataBuffer.put( v.getUV2f() );
            vertexDataBuffer.put( v.getTangent3f() );
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

        // TEXTURES
        // =================
        TextureID = new int[3];

        // Generate 3 Texture IDs and put them in an array
        gl.glGenTextures(3, TextureID, 0);

        LoadAndBindTexture( gl, TEXTURE_DIFFUSE, "diffuse.dds" );
        LoadAndBindTexture( gl, TEXTURE_NORMAL, "normal.dds" );
        LoadAndBindTexture( gl, TEXTURE_SPECULAR, "specular.dds" );
    }

    private void LoadAndBindTexture( GL2 gl, int TEXTURE_ID, String fileName )
    {
        try
        {
            InputStream input = new FileInputStream( fileName );
            

            switch ( TEXTURE_ID )
            {
                case TEXTURE_DIFFUSE:
                    this.diffuseTexture = TextureIO.newTexture( input, false, "DDS");
                    break;
                case TEXTURE_NORMAL:
                    this.normalTexture = TextureIO.newTexture( input, false, "DDS");
                    break;
                case TEXTURE_SPECULAR:
                    this.specularTexture = TextureIO.newTexture( input, false, "DDS");
                    break;
            }

//            PNGDecoder decoder = new PNGDecoder( input );
//            
//            // Set up a bytebuffer and decode the png file into it
//            ByteBuffer textureData = ByteBuffer.allocateDirect( 4 * decoder.getWidth() * decoder.getHeight() );
//            decoder.decode( textureData, decoder.getWidth()*4, PNGDecoder.Format.RGBA );
//            textureData.rewind();
//            
//            // Bind the DIFFUSE texture id and set the parameters
//            gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_ID ] );
//            gl.glTexParameteri( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR );
//            gl.glTexParameteri( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR );
//            gl.glTexImage2D( gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, texture.getTextureWidth(), texture.getTextureHeight(),
//                    0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, ByteBuffer.wrap(texture.getTextureData()) );
//            gl.glPixelStorei( gl.GL_UNPACK_ALIGNMENT, 4 );
            input.close();

        }
        catch ( FileNotFoundException e ) { e.printStackTrace(); }
        catch ( IOException e ) { e.printStackTrace(); }
    }


    public void drawModel( )
    {
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_AMBIENT,  FloatBuffer.wrap( mAmbient ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_DIFFUSE,  FloatBuffer.wrap( mDiffuse ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_SPECULAR, FloatBuffer.wrap( mSpecular ) );
        gl.glMaterialfv( gl.GL_FRONT, gl.GL_EMISSION, FloatBuffer.wrap( mNone ) );
        gl.glMaterialf(  gl.GL_FRONT, gl.GL_SHININESS,this.mSpecularShininess);

        // Enable Client States
        gl.glEnableClientState( gl.GL_VERTEX_ARRAY );
        gl.glEnableClientState( gl.GL_NORMAL_ARRAY );
        gl.glEnableClientState( gl.GL_TEXTURE_COORD_ARRAY );
        gl.glEnableVertexAttribArray( tangentLocation );

        // Bind the Vertex Buffer
        gl.glBindBuffer( gl.GL_ARRAY_BUFFER, VertexVBOID[0] );

        // Vertex data starts at 0 offset and repeats every 24 bytes
        // Normal data starts 12 bytes in and repeats every 24 bytes
        gl.glVertexPointer( 3, gl.GL_FLOAT, this.stride, 0 );
        gl.glNormalPointer( gl.GL_FLOAT, this.stride, 12 );
        gl.glTexCoordPointer( 2, gl.GL_FLOAT, this.stride, 24);

        // Tangent Vector atrribute
        // We can calculate the binormal with a dot product, no need to pass that
        gl.glVertexAttribPointer( tangentLocation, 3, gl.GL_FLOAT, false, this.stride, 32);
        
        gl.glUniform1i( this.diffuseLocation, 0);
        gl.glUniform1i( this.normalLocation, 1);
        gl.glUniform1i( this.specularLocation, 2);
        
        // Diffuse Texture
        gl.glActiveTexture( gl.GL_TEXTURE0 );
        gl.glEnable( gl.GL_TEXTURE_2D );
        //gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_DIFFUSE ] );
        diffuseTexture.bind( gl );


        // Normal Texture
        gl.glActiveTexture( gl.GL_TEXTURE1 );
        gl.glEnable( gl.GL_TEXTURE_2D );
        //gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_NORMAL ] );
        normalTexture.bind( gl );

        // Specular Texture
        gl.glActiveTexture( gl.GL_TEXTURE2 );
        gl.glEnable( gl.GL_TEXTURE_2D );
        //gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_SPECULAR ] );
        specularTexture.bind( gl );

        // Bind the Index Buffer
        gl.glBindBuffer( gl.GL_ELEMENT_ARRAY_BUFFER, IndexVBOID[0] );

        // Draw indexed vertices
        gl.glDrawElements( gl.GL_TRIANGLES, indexData.length, gl.GL_UNSIGNED_SHORT, 0 );

        // Disable client states
        gl.glDisableClientState( gl.GL_VERTEX_ARRAY );
        gl.glDisableClientState( gl.GL_NORMAL_ARRAY );
        gl.glDisableClientState( gl.GL_TEXTURE_COORD_ARRAY );
        gl.glDisableVertexAttribArray( tangentLocation );
    }

    private void LoadFile()
    {
        LittleEndianDataInputStream input;
        try
        {
            // Read Header
            LoadHeaderInfo();

            // TODO: NUMBER OF MODELS * READ MODEL INFO
            // Read Model Info
            LoadModelInfo();

            // TODO: NUMBER OF MESHES * READ MESH INFO
            // Read Mesh Info
            LoadMeshInfo();

            // Read Vertex Data
            LoadVertexData();
            
            // Read Index Data
            LoadIndexData();

        }
        catch   ( FileNotFoundException e ) { e.printStackTrace(); }
        catch ( IOException e ) { e.printStackTrace(); }
    }

    private void LoadIndexData() throws IOException
    {
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        indexData = new short[ modelInfo.indexCount ];

        input.skipBytes( modelInfo.offsIndexData );

        for ( int count = 0; count < modelInfo.indexCount; count++ )
            indexData[ count ] = input.readShort();
        input.close();
    }

    private void LoadVertexData() throws IOException
    {
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        input.skipBytes( modelInfo.offsVertexData );

        vertexData = new Gr2Vertex[ modelInfo.vertexCount ];

        for ( int count = 0; count < modelInfo.vertexCount; count++ )
        {
            vertexData[count] = new Gr2Vertex();
            vertexData[count].setX( input.readFloat() );
            vertexData[count].setY( input.readFloat() );
            vertexData[count].setZ( input.readFloat() );
            
            vertexData[count].setBlendWeight( input.readInt() );
            vertexData[count].setBlendIndices( input.readInt() );

            vertexData[count].setNx( input.readUnsignedByte() );
            vertexData[count].setNy( input.readUnsignedByte() );
            vertexData[count].setNz( input.readUnsignedByte() );
            input.skipBytes(1);

            vertexData[count].setTx( input.readUnsignedByte() );
            vertexData[count].setTy( input.readUnsignedByte() );
            vertexData[count].setTz( input.readUnsignedByte() );
            input.skipBytes(1);

            vertexData[count].setU( input.readShort() );
            vertexData[count].setV( input.readShort() );
        }
        input.close();
    }

    private void LoadMeshInfo() throws IOException
    {
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        meshInfo = new Gr2MeshInfo();

        input.skipBytes( modelInfo.offsMeshData );
        meshInfo.triangleOffset = input.readInt();
        meshInfo.triangleCount = input.readInt();
        meshInfo.materialIdx = input.readInt();
        input.close();
    }

    private void LoadModelInfo() throws IOException
    {
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        modelInfo = new Gr2ModelInfo();

        input.skipBytes( header.offsModelInfo );
        modelInfo.offsModelName = input.readInt();
        input.skipBytes(0x4);
        modelInfo.numMeshes = input.readShort();
        modelInfo.numBones = input.readShort();
        input.skipBytes(0x2);
        modelInfo.vertexSize = input.readShort();
        modelInfo.vertexCount = input.readInt();
        modelInfo.indexCount = input.readInt();
        modelInfo.offsVertexData = input.readInt();
        modelInfo.offsMeshData = input.readInt();
        modelInfo.offsIndexData = input.readInt();
        modelInfo.offsBoneData = input.readInt();
        input.close();
    }

    private void LoadHeaderInfo() throws IOException
    {
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        header = new Gr2Header();

        input.skipBytes(0x18);
        header.numModels = input.readShort();
        header.numMaterials = input.readShort();
        header.numBones = input.readShort();
        header.numAttachments = input.readShort();

        input.skipBytes( 0x34 );
        header.offsModelInfo = input.readInt();
        header.offsMaterialInfo = input.readInt();
        header.offsBonesInfo = input.readInt();
        header.offsAttachmentInfo = input.readInt();
        input.close();
    }
}
