package Sav3D.GL2.Models.GW2Model;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/22/12
 * Time: 1:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class GW2IndexInfo
{
    public int      unkValue0; // 1
    public int      unkValue1;
    public int      unkValue2;
    public int      unkValue3;
    public int      indexCount;
    public int      offsIndexData;

    public int[]    unkValues; // 14 unknown int32 numbers

    public int      numTriangles;

    public GW2IndexInfo(  )
    {
        this.unkValue0    = 0;
        this.unkValue1    = 0;
        this.unkValue2    = 0;
        this.unkValue3    = 0;
        this.indexCount   = 0;
        this.offsIndexData= 0;
        this.unkValues    = new int[14];
        this.numTriangles = 0;
    }
}
