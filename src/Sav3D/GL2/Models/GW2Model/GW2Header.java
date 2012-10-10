package Sav3D.GL2.Models.GW2Model;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/19/12
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GW2Header
{
    public int     magicNumber;
    public int     unkValue1;
    public int     unkValue2;
    public int     offsBinary;
    public int     unkValue3;
    public int     unkValue4;
    public short   numModels;
    public short   numMaterials;
    public short   numBones;
    public short   numAttachments;
    public int     unkValue5;
    public int     unkValue6;
    public int     unkValue7;
    public int     unkValue8;
    public float   minBoundingBox_x;
    public float   minBoundingBox_y;
    public float   minBoundingBox_z;
    public float   minBoundingBox_w;
    public float   maxBoundingBox_x;
    public float   maxBoundingBox_y;
    public float   maxBoundingBox_z;
    public float   maxBoundingBox_w;
    public int     offsOffsetsTable;
    public int     offsModelInfo;
    public int     offsMaterialInfo;
    public int     offsBonesInfo;
    public int     offsAttachmentInfo;

    public GW2Header()
    {
        this.magicNumber         = 0;
        this.unkValue1           = 0;
        this.unkValue2           = 0;
        this.offsBinary          = 0;
        this.unkValue3           = 0;
        this.unkValue4           = 0;
        this.numModels           = 0;
        this.numMaterials        = 0;
        this.numBones            = 0;
        this.numAttachments      = 0;
        this.unkValue5           = 0;
        this.unkValue6           = 0;
        this.unkValue7           = 0;
        this.unkValue8           = 0;
        this.minBoundingBox_x    = 0.f;
        this.minBoundingBox_y    = 0.f;
        this.minBoundingBox_z    = 0.f;
        this.minBoundingBox_w    = 0.f;
        this.maxBoundingBox_x    = 0.f;
        this.maxBoundingBox_y    = 0.f;
        this.maxBoundingBox_z    = 0.f;
        this.maxBoundingBox_w    = 0.f;
        this.offsOffsetsTable    = 0;
        this.offsModelInfo       = 0;
        this.offsMaterialInfo    = 0;
        this.offsBonesInfo       = 0;
        this.offsAttachmentInfo  = 0;
    }
}
