package Sav3D.GL2.Engines;

import Sav3D.GL2.Renderers.JOGLSimpleRenderer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Peter
 * Date: 2/5/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleEngine
{
    // public Model
    // public Material or Texture

    private Frame frame;
    private JOGLSimpleRenderer renderer;

    public SimpleEngine()
    {
        this.frame = new Frame("SWTOR Model Viewer v1.0");

        // Set render target to frame, render at 30 frames/second
        this.renderer = new JOGLSimpleRenderer(frame, 60);
        
        // Handle the frame closing event
        this.frame.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                /*
                    We have to run this in another thread than the AWT event queue,
                    otherwise on some systems (mostly unix) we can cause a deadlock
                    when trying to stop the game loop.
                 */
                new Thread( new Runnable()
                {
                    public void run()
                    {
                        renderer.StopRenderingLoop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        
        this.frame.setSize(1280+8,720+27);
        //this.frame.setSize(640+8,480+27);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        // Start the game loop
        this.renderer.StartRenderingLoop();
    }

}
