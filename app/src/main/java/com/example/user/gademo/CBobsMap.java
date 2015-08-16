package com.example.user.gademo;

/**
 * Created by user on 8/16/15.
 */
import java.util.Vector;
import java.awt.*;

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

    public int[][] memory = new int[MAP_HEIGHT][MAP_WIDTH];

    CBobsMap()
    {
        ResetMemory();
    }



    public double TestRoute(final Vector<Integer> vecPath, CBobsMap Bobs)
    {
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

    public void Render(final int xClient, final int yClient, Graphics g)
    {
        final int BlockSizeY = yClient/MAP_HEIGHT;
        final int BlockSizeX = xClient/MAP_WIDTH;

        int x = 0;
        int y = 0;

        for (y = 0;y<yClient; y += BlockSizeY)
        {
            g.setColor(Color.black);
            g.drawLine(x, y, xClient, y);
        }
        y=0;
        for(x = 0;x<xClient; x += BlockSizeX)
        {
            g.setColor(Color.black);
            g.drawLine(x,y,x,yClient);
        }
        int left, top;
        for (int i = 0; i< MAP_WIDTH; i++){
            for(int j = 0; j<MAP_HEIGHT; j++){

                if (map[j][i] == 1){

                    left = i*BlockSizeX;
                    top = j*BlockSizeY;
                    g.setColor(Color.blue);
                    g.fillRect(left, top, BlockSizeX, BlockSizeY);
                }
                if((map[j][i] == 5) || (map[j][i] == 8)) {
                    left = i * BlockSizeX;
                    top = j * BlockSizeY;
                    g.setColor(Color.RED);
                    g.fillRect(left, top, BlockSizeX, BlockSizeY);
                }
            }
        }
    }

    public void MemoryRender(final int xClient, final int yClient, Graphics g)
    {
        final int BlockSizeY = yClient/MAP_HEIGHT;
        final int BlockSizeX = xClient/MAP_WIDTH;
        int left, top;
        System.out.println("-------Memory --------");

        for (int j = 0; j < MAP_HEIGHT; j++){

            for(int k = 0; k < MAP_WIDTH; k++){
                System.out.print(" " + memory[j][k]);
            }
            System.out.println();
        }
        for (int l = 0; l< MAP_HEIGHT; l++){
            for(int j = 0; j<MAP_WIDTH; j++){

                if (memory[l][j] == 1){

                    left = j*BlockSizeX;
                    top = l*BlockSizeY;
                    g.setColor(Color.GRAY);
                    System.out.println("did fillrect work?");
                    g.fillRect(left, top, BlockSizeX, BlockSizeY);
                }
            }
        }
    }

    public void ResetMemory()
    {
        for(int y=0; y<m_iMapHeight; ++y)
        {
            for(int x = 0; x<m_iMapWidth; x++)
            {
                memory[y][x] = 0;
            }
        }
    }
}
