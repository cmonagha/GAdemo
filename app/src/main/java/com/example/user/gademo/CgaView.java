package com.example.user.gademo;

/**
 * Created by user on 8/16/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.user.gademo.CBobsMap.map;
import static com.example.user.gademo.defines.MAP_HEIGHT;
import static com.example.user.gademo.defines.MAP_WIDTH;

/**
 * Created by user on 8/15/15.
 */
public class CgaView extends SurfaceView implements SurfaceHolder.Callback {

    private CgaThread cgaThread;

    private Paint textPaint; // Paint used to draw text
    private Paint cannonballPaint; // Paint used to draw the cannonball
    private Paint cannonPaint; // Paint used to draw the cannon
    private Paint blockerPaint; // Paint used to draw the blocker
    private Paint endPaint; // Paint used to draw the target
    private Paint backgroundPaint;
    private Paint blockPaint;
    private Paint linePaint;
    private boolean dialogIsDisplayed = false;
    private Activity activity;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        cgaThread = new CgaThread(holder);
        cgaThread.setRunning(true);
        cgaThread.start(); // start the game loop thread

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        cgaThread.setRunning(false);

        while (retry)
        {
            try
            {
                cgaThread.join();
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
        getHolder().addCallback(this);
        activity = (Activity) context;

        textPaint = new Paint(); // Paint for drawing text
        cannonPaint = new Paint(); // Paint for drawing the cannon
        cannonballPaint = new Paint(); // Paint for drawing a cannonball
        blockerPaint = new Paint(); // Paint for drawing the blocker
        endPaint = new Paint(); // Paint for drawing the target
        backgroundPaint = new Paint();
        blockPaint = new Paint();
        linePaint = new Paint();
    }

    public void drawGameElements(Canvas canvas) {
        backgroundPaint.setColor(Color.GRAY);
        blockPaint.setColor(Color.BLUE);
        linePaint.setColor(Color.BLACK);
        endPaint.setColor(Color.RED);

        // clear the background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
                backgroundPaint);

        Point currentPoint = new Point(); // start of current target section

        final int BlockSizeY = canvas.getHeight()/MAP_HEIGHT;
        final int BlockSizeX = canvas.getWidth()/MAP_WIDTH;

        int x = 0;
        int y = 0;

        // draw verticles.  (0,y+blocky,canvaswidth,y+blocky)
        for (y = 0;y<canvas.getHeight(); y += BlockSizeY)
        {
            canvas.drawLine(x, y, canvas.getWidth(), y, linePaint);
        }
        y=0;
        //draw the horizontals (
        for(x = 0;x< canvas.getWidth(); x += BlockSizeX)
        {
            canvas.drawLine(x, y, x, canvas.getHeight(), linePaint);
        }

        int left, top;
        for (int i = 0; i< MAP_WIDTH; i++){
            for(int j = 0; j<MAP_HEIGHT; j++){

                if (map[j][i] == 1){

                    left = i*BlockSizeX;
                    top = j*BlockSizeY;
                    canvas.drawRect(left, top, left + BlockSizeX, top + BlockSizeY, blockPaint);
                }
                if((map[j][i] == 5) || (map[j][i] == 8)) {
                    left = i * BlockSizeX;
                    top = j * BlockSizeY;
                    canvas.drawRect(left, top, left+ BlockSizeX, top + BlockSizeY, endPaint);
                }
            }
        }
    }

    // stops the game
    public void stopGame()
    {
        if (cgaThread != null)
            cgaThread.setRunning(false);
    }

    public void releaseResources()
    {

    }

    // Thread subclass to control the game loop
    private class CgaThread extends Thread
    {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        // initializes the surface holder
        public CgaThread(SurfaceHolder holder)
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