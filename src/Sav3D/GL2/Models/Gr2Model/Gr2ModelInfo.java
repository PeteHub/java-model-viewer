package Sav3D.GL2.Models.Gr2Model;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/19/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gr2ModelInfo
{
    int     offsModelName;
    short   unkValue1;
    short   unkValue2;
    short   numMeshes;
    short   numBones;
    short   vertexFormatFlags;
    short   vertexSize;
    int     vertexCount;
    int     indexCount;
    int     offsVertexData;
    int     offsMeshData;
    int     offsIndexData;
    int     offsBoneData;

    public Gr2ModelInfo()
    {
        this.offsModelName     = 0;
        this.unkValue1         = 0;
        this.unkValue2         = 0;
        this.numMeshes         = 0;
        this.numBones          = 0;
        this.vertexFormatFlags = 0;
        this.vertexSize        = 0;
        this.vertexCount       = 0;
        this.indexCount        = 0;
        this.offsVertexData    = 0;
        this.offsMeshData      = 0;
        this.offsIndexData     = 0;
        this.offsBoneData      = 0;
    }
}
