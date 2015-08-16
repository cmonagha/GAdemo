package com.example.user.gademo;

/**
 * Created by user on 8/16/15.
 */
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.jar.Attributes;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by user on 8/15/15.
 */
public class CgaView extends SurfaceView implements SurfaceHolder.Callback {

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!dialogIsDisplayed)
        {
            cannonThread = new CannonThread(holder);
            cannonThread.setRunning(true);
            cannonThread.start(); // start the game loop thread
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        cannonThread.setRunning(false);

        while (retry)
        {
            try
            {
                cannonThread.join();
                retry = false;
            } // end try
            catch (InterruptedException e)
            {
            } // end catch
        }
    }

    public CgaView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    // Thread subclass to control the game loop
    private class CannonThread extends Thread
    {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        // initializes the surface holder
        public CannonThread(SurfaceHolder holder)
        {
            surfaceHolder = holder;
            setName("CannonThread");
        } // end constructor

        // changes running state
        public void setRunning(boolean running)
        {
            threadIsRunning = running;
        } // end method setRunning

        // controls the game loop
        @Override
        public void run()
        {
            Canvas canvas = null; // used for drawing
            long previousFrameTime = System.currentTimeMillis();

            while (threadIsRunning)
            {
                try
                {
                    canvas = surfaceHolder.lockCanvas(null);

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.00;
                        updatePositions(elapsedTimeMS); // update game state
                        drawGameElements(canvas); // draw
                        previousFrameTime = currentTime; // update previous time
                    } // end synchronized block
                } // end try
                finally
                {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                } // end finally
            } // end while
        } // end method run
    }
}