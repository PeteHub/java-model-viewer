package Sav3D.GL2.Models.GW2Model;

import Sav3D.Utility.HalfFloat;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/19/12
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class GW2Vertex
{
    private float x;
    private float y;
    private float z;
    private float blendWeight;
    private float blendIndices;
    private float nx;
    private float ny;
    private float nz;
    private float tx;
    private float ty;
    private float tz;
    private float bx;
    private float by;
    private float bz;
    private short halfFloatU;    // half float
    private short halfFloatV;    // half float
    private float u;
    private float v;

    public GW2Vertex()
    {

        this.x            = 0.f;
        this.y            = 0.f;
        this.z            = 0.f;
        this.blendWeight  = 0;
        this.blendIndices = 0;
        this.nx           = 0.f;
        this.ny           = 0.f;
        this.nz           = 0.f;
        this.tx           = 0.f;
        this.ty           = 0.f;
        this.tz           = 0.f;
        this.bx           = 0.f;
        this.by           = 0.f;
        this.bz           = 0.f;
        this.halfFloatU   = 0;
        this.halfFloatV   = 0;
        this.u            = 0.f;
        this.v            = 0.f;
    }

    public GW2Vertex( float x, float y, float z, float blendWeight, float blendIndices,
                      byte nx, byte ny, byte nz, byte tx, byte ty, byte tz, byte bx, byte by, byte bz,
                      short halfFloatU, short halfFloatV )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.blendWeight = blendWeight;
        this.blendIndices = blendIndices;
        
        this.nx = convertNormalFromByteToFloat( nx );
        this.ny = convertNormalFromByteToFloat( ny );
        this.nz = convertNormalFromByteToFloat( nz );

        // Tangent (converts the same way)
        this.tx = convertNormalFromByteToFloat( tx );
        this.ty = convertNormalFromByteToFloat( ty );
        this.tz = convertNormalFromByteToFloat( tz );

        // Binormal (converts the same way)
        this.bx = convertNormalFromByteToFloat( bx );
        this.by = convertNormalFromByteToFloat( by );
        this.bz = convertNormalFromByteToFloat( bz );

        // TODO: convert them from half float to float
        this.halfFloatU = halfFloatU;
        this.halfFloatV = halfFloatV;
        this.u = 0.f;
        this.v = 0.f;
    }

    public void setX( float x )
    {
        this.x = x;
    }

    public void setY( float y )
    {
        this.y = y;
    }

    public void setZ( float z )
    {
        this.z = z;
    }

    public void setBlendWeight( float blendWeight )
    {
        this.blendWeight = blendWeight/255.0f;
    }

    public void setBlendIndices( float blendIndices )
    {
        this.blendIndices = blendIndices;
    }

    public void setNx( int nx )
    {
        this.nx = convertNormalFromByteToFloat( nx );
    }

    public void setNy( int ny )
    {
        this.ny = convertNormalFromByteToFloat( ny );
    }

    public void setNz( int nz )
    {
        this.nz = convertNormalFromByteToFloat( nz );
    }

    public void setTx( int tx )
    {
        this.tx = convertNormalFromByteToFloat( tx );
    }

    public void setTy( int ty )
    {
        this.ty = convertNormalFromByteToFloat( ty );
    }

    public void setTz( int tz )
    {
        this.tz = convertNormalFromByteToFloat( tz );
    }

    public void setBx( int bx )
    {
        this.bx = convertNormalFromByteToFloat( bx );
    }

    public void setBy( int by )
    {
        this.by = convertNormalFromByteToFloat( by );
    }

    public void setBz( int bz )
    {
        this.bz = convertNormalFromByteToFloat( bz );
    }

    public void setU( float floatU )
    {
        this.u = floatU;
    }

    public void setV( float floatV )
    {
        this.v = floatV;
    }

    public void setU( short halfFloatU )
    {
        this.halfFloatU = halfFloatU;
        this.u = HalfFloat.toFloat( halfFloatU );
    }

    public void setV( short halfFloatV )
    {
        this.halfFloatV = halfFloatV;
        this.v = HalfFloat.toFloat( halfFloatV );
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }
    
    public float[] getVertex3f()
    {
        return new float[] { x, y, z };
    }

    public float getNx()
    {
        return nx;
    }

    public float getNy()
    {
        return ny;
    }

    public float getNz()
    {
        return nz;
    }

    public float[] getNormal3f()
    {
        return new float[] { nx, ny, nz };
    }  
    
    public float[] getTangent3f()
    {
        return new float[] { tx, ty, tz };
    }

    public float[] getBinormal3f()
    {
        return new float[] { bx, by, bz };
    }

    public float getU()
    {
        return u;
    }

    public float getV()
    {
        return v;
    }
    
    public float[] getUV2f()
    {
        return new float[] { u, v };
    }

    private float convertNormalFromByteToFloat( int normalCoordinate )
    {
        // stored as (normal+128) / 127 (normal + 0x80)/127 => 0xFF = 1, 0x80 = 0, 0x00 ~= -1
        // so we have to reverse it
//        if ( normalCoordinate == 0)
//            return -1.0f;

        return (float)( normalCoordinate - 128 ) / 127.0f;
        //return (float)( normalCoordinate / 127.0f) +128.0f;
        //return (float)( normalCoordinate /255.0f ) ;
    }
}
