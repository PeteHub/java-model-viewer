package Sav3D.GL2.Models.GW2Model;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/19/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GW2ModelInfo
{
    // String table offset is from the beginning of the ModelInfo block
    // Could mean the overall size of the modelinfo block, including this int
    int     offsStringTable;

    public int     unkValue0;      // 0x2C
    public int     unkValue1;      // 0
    public int     unkValue2;      // 0
    public int     offsIndexInfo;  // Offset to block describing index information
    public int     unkValue3;      // 1
    public int     unkValue4;      // Offset to unknown structure, maybe material info
    public int     unkValue5;      // 1
    public int     unkValue6;      // Offset to unknown structure
    public int     unkValue7;      // 0
    public int     unkValue8;      // 0
    
    // Offset to a block that contains structures that describers how vertices are declared and stored
    // This block contains numVertexElements instances of the VertexDeclaration structure
    int     offsVertexDeclaration; 
    
    int     vertexCount;
    int     offsVertexData;
    int     numVertexElements;
    int     offsElementNames;

    public GW2ModelInfo( )
    {
        this.offsStringTable        = 0;
        this.unkValue0              = 0;
        this.unkValue1              = 0;
        this.unkValue2              = 0;
        this.offsIndexInfo          = 0;
        this.unkValue3              = 0;
        this.unkValue4              = 0;
        this.unkValue5              = 0;
        this.unkValue6              = 0;
        this.unkValue7              = 0;
        this.unkValue8              = 0;
        this.offsVertexDeclaration  = 0;
        this.vertexCount            = 0;
        this.offsVertexData         = 0;
        this.numVertexElements      = 0;
        this.offsElementNames       = 0;
    }
}
