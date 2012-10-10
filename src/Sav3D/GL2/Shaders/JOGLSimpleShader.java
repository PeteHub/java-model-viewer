package Sav3D.GL2.Shaders;

import com.jogamp.common.nio.Buffers;

import javax.media.opengl.GL2;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/12/12
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class JOGLSimpleShader
{
    private int shader_id;
    private int shader_type;
    
    private String sFileName;

    private GL2 gl;
    
    public JOGLSimpleShader( GL2 gl, String sFileName, int GL2_SHADER_TYPE ) throws IOException
    {
        this.gl = gl;

        this.sFileName = sFileName;
        this.shader_type = GL2_SHADER_TYPE;

        LoadShader();
    }

    private void LoadShader() throws IOException
    {

        this.shader_id = gl.glCreateShader( this.shader_type );

        InputStream ins = new FileInputStream(this.sFileName);

        StringBuffer buffer = new StringBuffer();
        Scanner scanner = new Scanner(ins);
        try {
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine() + "\n");
            }
        } finally {
            scanner.close();
        }

        gl.glShaderSource( this.shader_id, 1, new String[] { buffer.toString() }, (int[]) null, 0 );

        // Compile the shader
        gl.glCompileShader( this.shader_id );


        IntBuffer shaderLog = Buffers.newDirectIntBuffer( 1 );
        IntBuffer shaderLogLength = Buffers.newDirectIntBuffer(1);
        ByteBuffer shaderLogText;

        gl.glGetShaderiv( this.shader_id, gl.GL_INFO_LOG_LENGTH, shaderLog );

        if ( shaderLog.get(0) > 1)
        {
            shaderLogText = Buffers.newDirectByteBuffer( shaderLog.get(0) );
            gl.glGetShaderInfoLog(this.shader_id, shaderLog.get(0), shaderLogLength, shaderLogText );

            while ( shaderLogText.hasRemaining() )
                System.out.print( Character.toChars( shaderLogText.get() ));
        }
    }
    
    public int GetShaderId()
    {
        return this.shader_id;
    }
}
