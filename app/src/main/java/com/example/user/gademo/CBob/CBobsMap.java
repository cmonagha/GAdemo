package com.example.user.gademo.CBob;

/**
 * Created by user on 8/16/15.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Vector;

import static com.example.user.gademo.CBob.defines.MAP_HEIGHT;
import static com.example.user.gademo.CBob.defines.MAP_WIDTH;
import static java.lang.Math.abs;



// this is some txt
public class CBobsMap {

    // Storage for the map
    static final int[][] map =
            {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1},
                    {8, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1},
                    {1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1},
                    {1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                    {1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1},
                    {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1},
                    {1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 5},
                    {1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    private static final int m_iMapWidth = MAP_WIDTH;
    private static final int m_iMapHeight = MAP_HEIGHT;

    // Index into the array which is the start point
    private static final int m_iStartX=14;
    private static final int m_iStartY=7;

    // and the finish point
    private static final int m_iEndX=0;
    private static final int m_iEndY=2;

    private Paint memPaint; // Paint used to draw the blocker
    private Paint endPaint; // Paint used to draw the target
    private Paint backgroundPaint;
    private Paint blockPaint;
    private Paint linePaint;

    public int[][] memory = new int[MAP_HEIGHT][MAP_WIDTH];

    CBobsMap()
    {
        Log.i("CBobsMap", "Constructor RAN");
        ResetMemory();
<<<<<<< HEAD:app/src/main/java/com/example/user/gademo/CBob/CBobsMap.java

        memPaint = new Paint(); // Paint for drawing the blocker
        endPaint = new Paint(); // Paint for drawing the target
        backgroundPaint = new Paint();
        blockPaint = new Paint();
        linePaint = new Paint();
    }
=======
    } // this is a addition
>>>>>>> 4240405fb9c1640dc0020c5b3ab6346f5832eb60:app/src/main/java/com/example/user/gademo/CBobsMap.java



    public double TestRoute(final Vector<Integer> vecPath, CBobsMap Bobs)
    {
        Log.i("CBobsMap", "TestRoute RAN");
        int posX = m_iStartX;
        int posY = m_iStartY;

        for(int dir=0; dir<vecPath.size(); ++dir)
        {
            int NextDir = vecPath.get(dir);
            //System.out.println("element at " + dir + ": " + vecPath.get(dir));
            switch(vecPath.get(dir))
            {
                case 0: //North
                    if ( ((posY-1) < 0)  || (map[posY-1][posX] == 1))
                    {
                        //System.out.println("passed, Y-1: " + (posY-1) + " X: " + posX + " map: " + map[posY-1][posX]);
                        break;
                    }

                    else
                    {
                        posY -= 1;
                    }
                    break;

                case 1: //South
                    //System.out.print("South was hit: ");
                    if(((posY+1) >= m_iMapHeight) || (map[posY+1][posX] == 1))
                    {
                        break;
                    }

                    else
                    {
                        posY += 1;
                    }

                    break;

                case 2: // East
                    if(((posX+1) >= m_iMapWidth) || (map[posY][posX+1] == 1))
                    {
                        break;
                    }
                    else
                    {
                        posX += 1;
                    }
                    break;

                case 3: // West
                    if (((posX-1) < 0) || (map[posY][posX-1]==1))
                    {
                        break;
                    }

                    else
                    {
                        posX -= 1;
                    }
                    break;
            } // end switch
            // mark the route in the memory array
            Bobs.memory[posY][posX] = 1;
            //System.out.println("" +Bobs.memory);
        }

        int DiffX = abs(posX-m_iEndX);
        int DiffY = abs(posY-m_iEndY);

        return 1/(double)(DiffX+DiffY+1);
    }

    public void Render(Canvas canvas)
    {
        backgroundPaint.setColor(Color.GRAY);
        blockPaint.setColor(Color.BLUE);
        linePaint.setColor(Color.BLACK);
        endPaint.setColor(Color.RED);
        memPaint.setColor(Color.GREEN);

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

    public void MemoryRender(Canvas canvas)
    {
<<<<<<< HEAD:app/src/main/java/com/example/user/gademo/CBob/CBobsMap.java
        memPaint.setColor(Color.GREEN);
        final int BlockSizeY = canvas.getHeight()/MAP_HEIGHT;
        final int BlockSizeX = canvas.getWidth()/MAP_WIDTH;
        int left, top;
        for(int k = 0; k < MAP_WIDTH; k++)
        {
            for(int l = 0; l< MAP_HEIGHT; l++)
            {
                if(memory[l][k] == 1)
                {
                    left = k *BlockSizeX;
                    top = l *BlockSizeY;
                    canvas.drawRect(left, top, left + BlockSizeX, top + BlockSizeY, memPaint);
                }
            }
        }
=======
        // this is a change
>>>>>>> 4240405fb9c1640dc0020c5b3ab6346f5832eb60:app/src/main/java/com/example/user/gademo/CBobsMap.java
    }

    public void ResetMemory()
    {
        Log.i("CBobsMap", "Reset Memory RAN");
        for(int y=0; y<m_iMapHeight; ++y)
        {
            for(int x = 0; x<m_iMapWidth; x++)
            {
                memory[y][x] = 0;
            }
        }
    }
}
