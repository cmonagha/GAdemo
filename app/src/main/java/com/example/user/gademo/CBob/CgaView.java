package com.example.user.gademo.CBob;

/**
 * Created by user on 8/16/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.user.gademo.CBob.defines.CHROMO_LENGTH;
import static com.example.user.gademo.CBob.defines.CROSSOVER_RATE;
import static com.example.user.gademo.CBob.defines.GENE_LENGTH;
import static com.example.user.gademo.CBob.defines.MUTATION_RATE;
import static com.example.user.gademo.CBob.defines.POP_SIZE;
import static com.example.user.gademo.CBob.defines.mPixelFactor;

/**
 * Created by user on 8/15/15.
 */
public class CgaView extends SurfaceView implements SurfaceHolder.Callback {

    private CgaThread cgaThread;
    CgaBob g_pGABob;

    private final float mTextWidth;
    private final float mTextHeight;

    private Paint mPaint;
    private long mTotalMillis;
    private int mDraws;
    private float mFps;

    private String mFpsText = "";


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        cgaThread = new CgaThread(holder);
        cgaThread.setRunning(true);
        cgaThread.start(); // start the game loop thread

        //holder2 = holder;
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

    public CgaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);


        g_pGABob = new CgaBob(CROSSOVER_RATE, MUTATION_RATE, POP_SIZE, CHROMO_LENGTH, GENE_LENGTH);

        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = (float) (25*mPixelFactor);
        mTextWidth = (float) (50*mPixelFactor);
        mPaint.setTextSize(mTextHeight/2);
    }

    public void start() {
        //Log.i("Thread", "START ran");
        //if (cgaThread != null){
        if(!cgaThread.threadIsRunning){
            cgaThread.setRunning(true);
            cgaThread.run();
        }

        //}
        g_pGABob.Start();
    }

    public void drawGameElements(long elapsedTime, Canvas canvas) {


        g_pGABob.render( canvas);

        mTotalMillis += elapsedTime;
        if (mTotalMillis > 1000) {
            mFps = mDraws*1000 / mTotalMillis;
            mFpsText = mFps+" fps";
            mTotalMillis = 0;
            mDraws = 0;
        }
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0,(int)(canvas.getHeight()-mTextHeight), mTextWidth, canvas.getHeight(), mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(mFpsText, mTextWidth / 2, (int) (canvas.getHeight() - mTextHeight / 2), mPaint);
        mDraws++;

        if (g_pGABob.Started()) {
            g_pGABob.Epoch();
        }
        //else{
        // /   cgaThread.setRunning(false);
        //}

    }

    // stops the game
    public void stopGame()
    {
        //if (cgaThread != null){
        //    Log.i("pause", "Pause Ran");
        //    cgaThread.setRunning(false);
        //}

        g_pGABob.Stop();
    }

    public void reset(){
        Log.i("B", "Reset Ran");
        g_pGABob = new CgaBob(CROSSOVER_RATE, MUTATION_RATE, POP_SIZE, CHROMO_LENGTH, GENE_LENGTH);
    }

    public void releaseResources()
    {

    }

    public void killThread(){
        g_pGABob.Stop();
        cgaThread.setRunning(false);
    }

    // Thread subclass to control the game loop
    private class CgaThread extends Thread
    {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default
        private boolean threadIsPaused = false;

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
            long previousTimeMillis = 0;
            long currentTimeMillis = 0;
            long elapsedMillis = 0;
            previousTimeMillis = System.currentTimeMillis();

            while (threadIsRunning)
            {
                currentTimeMillis = System.currentTimeMillis();
                elapsedMillis = currentTimeMillis - previousTimeMillis;
                try
                {
                    canvas = surfaceHolder.lockCanvas(null);

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        drawGameElements(elapsedMillis, canvas); // draw
                    } // end synchronized block
                } // end try
                finally
                {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                } // end finally
                if (threadIsRunning == false){
                    Log.i("Thread", "Thread is stopped.");
                }
                previousTimeMillis = currentTimeMillis;
            } // end while
        } // end method run
    }
}