package Sav3D.GL2.Models.GW2Model;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/22/12
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class GW2VertexDeclarationInfo
{
    // Position/TexCoord stored as normal floats
    public static final int VECTOR_FLOAT  = 0xA;

    // Normal, Tangent and Binormal vectors are
    // stored in UBYTE4 on one byte each, respectively
    // 4th byte is ignored
    // Conversion is (byte-0x80/127.0f)
    public static final int VECTOR_PACKED = 0x14;

    int vertexElementFormat;
    int offsPositionString;
    int unkValue0;
    int numValues;
            
    int unkValue1;
    int unkValue2;
    int unkValue3;
    int unkValue4;           
    
    

    public GW2VertexDeclarationInfo()
    {
        this.vertexElementFormat = 0;
        this.offsPositionString  = 0;
        this.unkValue0           = 0;
        this.numValues           = 0;

        this.unkValue1           = 0;
        this.unkValue2           = 0;
        this.unkValue3           = 0;
        this.unkValue4           = 0;
    }
}
