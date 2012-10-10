package Sav3D.GL2.Models.GW2Model;

import Sav3D.Utility.LittleEndianDataInputStream;
import de.matthiasmann.twl.utils.PNGDecoder;

import javax.media.opengl.GL2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
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
public class GW2Model
{
    // Gr2 File Information
    private final String fileName;
    private GW2Header header;
    private GW2ModelInfo modelInfo;
    private GW2IndexInfo indexInfo;
    private GW2VertexDeclarationInfo[] vertexDeclarationInfo;

    // Vertex And Index Data
    private GW2Vertex[] vertexData;
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

    private int tangentLocation;
    private int diffuseLocation;
    private int normalLocation;
    private int specularLocation;

    // Default material properties
    private final float[] mAmbient  = { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mDiffuse =  { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mSpecular = { 0.2f, 0.2f, 0.2f, 1.0f };
    private final float[] mNone     = { 0.0f, 0.0f, 0.0f, 1.0f };

    // ===========================================================
    //  TODO: IMPORTANT THIS IS MANUALLY DISCOVERED FOR NOW,
    //  TODO: DIFFERENT FOR EVERY FILE!
    // ===========================================================
    //private final int offsToModelInfo = 0x150; // Moa
    //private final int offsToModelInfo = 0x148;
    private final int offsToModelInfo = 0x3F8; // 10d952
    //private final int offsToModelInfo = 0x4e424; // 227bdd

    public GW2Model( GL2 gl, int shaderProgramID, String modelFile )
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

        for( GW2Vertex v : vertexData)
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

//        // TEXTURES
//        // =================
//        TextureID = new int[3];
//
//        // Generate 3 Texture IDs and put them in an array
//        gl.glGenTextures(3, TextureID, 0);
//
//        LoadAndBindTexture( gl, TEXTURE_DIFFUSE, "diffuse.png" );
//        LoadAndBindTexture( gl, TEXTURE_NORMAL, "normal.png" );
//        LoadAndBindTexture( gl, TEXTURE_SPECULAR, "specular.png" );
    }

    private void LoadAndBindTexture( GL2 gl, int TEXTURE_ID, String fileName )
    {
        try
        {
            InputStream input = new FileInputStream( fileName );
            PNGDecoder decoder = new PNGDecoder( input );
            
            // Set up a bytebuffer and decode the png file into it
            ByteBuffer textureData = ByteBuffer.allocateDirect( 4 * decoder.getWidth() * decoder.getHeight() );
            decoder.decode( textureData, decoder.getWidth()*4, PNGDecoder.Format.RGBA );
            textureData.rewind();
            
            // Bind the DIFFUSE texture id and set the parameters
            gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_ID ] );
            gl.glTexParameteri( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR );
            gl.glTexParameteri( gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR );
            gl.glTexImage2D( gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, decoder.getWidth(), decoder.getHeight(),
                    0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, textureData );
            gl.glPixelStorei( gl.GL_UNPACK_ALIGNMENT, 4 );
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
        gl.glMaterialf(  gl.GL_FRONT, gl.GL_SHININESS, 12.0f);
        
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
        
//        // Diffuse Texture
//        gl.glActiveTexture( gl.GL_TEXTURE0 );
//        gl.glEnable( gl.GL_TEXTURE_2D );
//        gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_DIFFUSE ] );
//
//
//        // Normal Texture
//        gl.glActiveTexture( gl.GL_TEXTURE1 );
//        gl.glEnable( gl.GL_TEXTURE_2D );
//        gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_NORMAL ] );
//
//        // Specular Texture
//        gl.glActiveTexture( gl.GL_TEXTURE2 );
//        gl.glEnable( gl.GL_TEXTURE_2D );
//        gl.glBindTexture( gl.GL_TEXTURE_2D, TextureID[ TEXTURE_SPECULAR ] );

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
            //LoadHeaderInfo();

            // TODO: NUMBER OF MODELS * READ MODEL INFO
            // Read Model Info
            LoadModelInfo();
            
            // Read Index Info
            LoadIndexInfo();

            // TODO: NUMBER OF MESHES * READ MESH INFO
            // Read Mesh Info
            LoadVertexDeclarationInfo();

            // Read Vertex Data
            LoadVertexData();
            
            // Read Index Data
            LoadIndexData();

        }
        catch   ( FileNotFoundException e ) { e.printStackTrace(); }
        catch ( IOException e ) { e.printStackTrace(); }
    }

    private void LoadModelInfo() throws IOException
    {
        // TODO: FIGURE OUT HOW TO READ THE OFFSET TO ModelInfo
        // FOR NOW IT IS MANUALLY LOOKED UP FROM THE FILE

        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        modelInfo = new GW2ModelInfo();

        input.skipBytes( this.offsToModelInfo );
        modelInfo.offsStringTable = input.readInt();
        input.skipBytes( 3*4 );
        modelInfo.offsIndexInfo = input.readInt();
        input.skipBytes( 6*4 );
        modelInfo.offsVertexDeclaration = input.readInt();
        modelInfo.vertexCount = input.readInt();
        modelInfo.offsVertexData = input.readInt();
        modelInfo.numVertexElements = input.readInt();
        modelInfo.offsElementNames = input.readInt();
        input.close();
    }

    private void LoadIndexInfo() throws IOException
    {
        // TODO: FIGURE OUT HOW TO READ THE OFFSET TO ModelInfo
        // FOR NOW IT IS MANUALLY LOOKED UP FROM THE FILE

        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );
        indexInfo = new GW2IndexInfo();

        input.skipBytes( this.offsToModelInfo );
        input.skipBytes( modelInfo.offsIndexInfo );

        input.skipBytes( 4*4 );
        indexInfo.indexCount = input.readInt();
        indexInfo.offsIndexData = input.readInt();
        input.skipBytes( 4*14 );
        indexInfo.numTriangles = input.readInt();

        input.close();
    }

    private void LoadVertexDeclarationInfo() throws IOException
    {
        // TODO: FIGURE OUT HOW TO READ THE OFFSET TO ModelInfo
        // FOR NOW IT IS MANUALLY LOOKED UP FROM THE FILE
        
        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );

        vertexDeclarationInfo = new GW2VertexDeclarationInfo[ modelInfo.numVertexElements ];

        input.skipBytes( this.offsToModelInfo );
        input.skipBytes( modelInfo.offsVertexDeclaration );
        
        for ( int idx = 0; idx < modelInfo.numVertexElements; idx++ )
        {
            vertexDeclarationInfo[idx] = new GW2VertexDeclarationInfo();
            vertexDeclarationInfo[idx].vertexElementFormat = input.readInt();
            vertexDeclarationInfo[idx].offsPositionString = input.readInt();
            input.skipBytes( 4 );
            vertexDeclarationInfo[idx].numValues = input.readInt();
            input.skipBytes( 4*4 );
        }

        input.close();
    }

    private void LoadVertexData() throws IOException
    {
        // TODO: FIGURE OUT HOW TO READ THE OFFSET TO ModelInfo
        // FOR NOW IT IS MANUALLY LOOKED UP FROM THE FILE

        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );

        vertexData = new GW2Vertex[ modelInfo.vertexCount ];

        input.skipBytes( this.offsToModelInfo );
        input.skipBytes( modelInfo.offsVertexData );



        for ( int count = 0; count < modelInfo.vertexCount; count++ )
        {
            vertexData[count] = new GW2Vertex();
            vertexData[count].setX( input.readFloat() );
            vertexData[count].setY( input.readFloat() );
            vertexData[count].setZ( input.readFloat() );


            // If we have 4xFloat BoneWeights and 4xFloat BoneIndices
            // Skip them
            if ( vertexDeclarationInfo.length == 5)
                input.skipBytes( 2*4 );

            vertexData[count].setNx( input.readUnsignedByte() );
            vertexData[count].setNy( input.readUnsignedByte() );
            vertexData[count].setNz( input.readUnsignedByte() );
            input.skipBytes(1);

            vertexData[count].setTx( input.readUnsignedByte() );
            vertexData[count].setTy( input.readUnsignedByte() );
            vertexData[count].setTz( input.readUnsignedByte() );
            input.skipBytes(1);

            vertexData[count].setBx( input.readUnsignedByte() );
            vertexData[count].setBy( input.readUnsignedByte() );
            vertexData[count].setBz( input.readUnsignedByte() );
            input.skipBytes(1);

            // In this case they are stored as half floats
            if ( vertexDeclarationInfo.length == 5)
            {
                vertexData[count].setU( input.readShort() );
                vertexData[count].setV( input.readShort() );
            }
            else
            {
                vertexData[count].setU( input.readFloat() );
                vertexData[count].setV( input.readFloat() );
            }
        }
        
        input.close();
    }

    private void LoadIndexData() throws IOException
    {

        // TODO: FIGURE OUT HOW TO READ THE OFFSET TO ModelInfo
        // FOR NOW IT IS MANUALLY LOOKED UP FROM THE FILE

        LittleEndianDataInputStream input;
        input = new LittleEndianDataInputStream( new FileInputStream( this.fileName ) );

        indexData = new short[ indexInfo.indexCount ];

        input.skipBytes( this.offsToModelInfo );
        input.skipBytes( indexInfo.offsIndexData );


        for ( int count = 0; count < indexInfo.indexCount; count++ )
            indexData[ count ] = input.readShort();
        input.close();
    }
}
