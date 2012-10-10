import Sav3D.GL2.Engines.SimpleEngine;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/5/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelViewer
{
    
    public static void main( String[] args )
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new SimpleEngine();
            }
        });
    }
    
}
