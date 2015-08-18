package com.example.user.gademo;

/**
 * Created by user on 8/16/15.
 */

import java.util.Vector;
import java.util.Random;
import android.util.Log;

import static com.example.user.gademo.defines.MAP_WIDTH;
import static com.example.user.gademo.defines.MAP_HEIGHT;
/**
 * Created by user on 8/15/15.
 */
public class CgaBob {
    public class SGenome{
        public Vector<Integer> vecBits = new Vector<>();
        double dFitness;
        Random rand = new Random();

        SGenome(){
            dFitness = 0.0;
        }

        SGenome(int num_bits)
        {
            dFitness = 0.0;
            for (int i=0; i < num_bits; i++){
                vecBits.add(rand.nextInt(2));
            }
        }
    };

    private Vector<SGenome> m_vecGenome = new Vector<>();
    private int m_iPopSize;
    private double m_dCrossoverRate;
    private double m_dMutationRate;

    private int m_iChromoLength;

    private int m_iGeneLength;
    private int m_iFittestGenome;
    private double m_dBestFitnessScore;
    private double m_dTotalFitnessScore;
    private int m_iGeneration;

    private CBobsMap m_BobsMap = new CBobsMap();
    public CBobsMap m_BobsBrain = new CBobsMap();

    private boolean m_bBusy;

    private void Mutate(Vector<Integer> vecBits)
    {
        Log.i("CgaBob", "Mutate RAN");
        Random rand = new Random();
        for(int curBit = 0; curBit<vecBits.size(); curBit++)
        {
            // do we flip this bit
            if (rand.nextFloat() < m_dMutationRate)
            {
                // flip the bit
                if (vecBits.get(curBit) == 0)
                    vecBits.set(curBit, 1);
                else
                    vecBits.set(curBit, 0);
            }
        }
    }

    private void Crossover(final Vector<Integer> mum, final Vector<Integer> dad, Vector<Integer> baby1, Vector<Integer> baby2)
    {
        Log.i("CgaBob", "Crossover RAN");
        Random rand = new Random();
        if ((rand.nextFloat() > m_dCrossoverRate) || (mum == dad))
        {
            for(int i =0; i<m_iChromoLength;i++){
                baby1.add(mum.get(i));
            }
            for(int i =0; i<m_iChromoLength;i++){
                baby2.add(dad.get(i));
            }
            return;
        }
        int cp = rand.nextInt(m_iChromoLength);
        for (int i = 0; i < cp; i++)
        {
            baby1.add(mum.get(i));
            baby2.add(dad.get(i));
        }
        for(int i = cp; i<mum.size(); i++)
        {
            baby1.add(dad.get(i));
            baby2.add(mum.get(i));
        }
    }

    private SGenome RouletteWheelSelection()
    {
        Log.i("CgaBob", "RouletteWheelSelection RAN");
        Random rand = new Random();
        double fSlice = rand.nextFloat() * m_dTotalFitnessScore;
        double cfTotal = 0.0;
        int SelectedGenome = 0;
        for(int i = 0; i<m_iPopSize; i++)
        {
            cfTotal += m_vecGenome.get(i).dFitness;
            if(cfTotal > fSlice)
            {
                SelectedGenome = i;
                break;
            }
        }

        return m_vecGenome.get(SelectedGenome);
    }

    private void UpdateFitnessScore()
    {
        Log.i("CgaBob", "UpdateFitnessScore RAN");
        m_iFittestGenome = 0;
        m_dBestFitnessScore = 0;
        m_dTotalFitnessScore = 0;

        CBobsMap TempMemory = new CBobsMap();

        // update the fitness scores and keep a check on fittest so far
        for (int i = 0; i<m_iPopSize; i++){
            // decode each genomes chromosomes into a vector of directions
            Vector<Integer> vecDirections = new Vector<>();
            vecDirections = Decode(m_vecGenome.get(i).vecBits);

            // get it's fitness score
            m_vecGenome.get(i).dFitness = m_BobsMap.TestRoute(vecDirections, TempMemory);

            //update total
            m_dTotalFitnessScore += m_vecGenome.get(i).dFitness;

            // if this is the fittest genome found so far, store results
            if(m_vecGenome.get(i).dFitness > m_dBestFitnessScore)
            {
                m_dBestFitnessScore = m_vecGenome.get(i).dFitness;

                m_iFittestGenome = i;

                for (int j = 0; j < MAP_HEIGHT; j++){
                    for(int k = 0; k < MAP_WIDTH; k++){
                        m_BobsBrain.memory[j][k] = TempMemory.memory[j][k];
                    }
                }

                // Has Bob found the exit?
                if(m_vecGenome.get(i).dFitness == 1)
                {
                    //if so, stom the run
                    m_bBusy = false;
                    Log.i("CgaBob", "Found the exit");
                }
            }

            TempMemory.ResetMemory();
        }
    }

    private Vector<Integer> Decode (final Vector<Integer> bits)
    {
        Log.i("CgaBob", "Decode RAN");
        Vector<Integer> directions = new Vector<Integer>();

        //step through the choromosome a gene at a time
        for(int gene = 0; gene < bits.size(); gene+= m_iGeneLength)
        {
            //get the gene at this position
            Vector<Integer> ThisGene = new Vector<Integer>();
            for(int bit = 0; bit<m_iGeneLength; ++bit)
            {
                ThisGene.add(bits.get(gene+bit));
            }

            //convert to decimal and add to list of directions
            //System.out.println("Decode: " + ThisGene);
            directions.add(BinToInt(ThisGene));
        }
        return directions;

    }

    private int BinToInt (final Vector<Integer> v)
    {
        //Log.i("CgaBob", "BinToInt RAN");
        int val = 0;
        int multiplier = 1;

        for(int cBit=v.size(); cBit > 0; cBit--)
        {
            val += v.get(cBit-1) * multiplier;
            multiplier *= 2;
        }
        return val;
    }

    private void CreateStartPopulation()
    {
        Log.i("CgaBob", "CreateStartPopulation RAN");
        for (int i = 0; i<m_iPopSize; i++)
        {
           m_vecGenome.add(new SGenome(m_iChromoLength));
        }
        m_iGeneration = 0;
        m_iFittestGenome = 0;
        m_dBestFitnessScore = 0;
        m_dTotalFitnessScore = 0;
    }

    public CgaBob(double cross_rat, double mut_rat, int pop_size, int num_bits, int gene_len)
    {
        m_dCrossoverRate = cross_rat;
        m_dMutationRate = mut_rat;
        m_iPopSize = pop_size;
        m_iChromoLength = num_bits;
        m_dTotalFitnessScore = 0.0;
        m_iGeneration = 0;
        m_iGeneLength = gene_len;
        m_bBusy = false;

        CreateStartPopulation();
    }

    public void Run(){
        Log.i("CgaBob", "Run RAN");
        CreateStartPopulation();
        m_bBusy = true;
       Epoch();
    }
    public void Render()
    {

    }

    public void Epoch()
    {
        Log.i("CgaBob", "Epoch RAN");
        UpdateFitnessScore();

        // Now Create a new population
        int NewBabies = 0;

        // Create some storage for the baby genomes
        Vector<SGenome> vecBabyGenomes = new Vector<>();

        while (NewBabies < m_iPopSize)
        {
            SGenome mum = RouletteWheelSelection();
            SGenome dad = RouletteWheelSelection();

            SGenome baby1 = new SGenome();
            SGenome baby2 = new SGenome();

            Crossover(mum.vecBits, dad.vecBits, baby1.vecBits, baby2.vecBits);

            //operator - mutate
            Mutate(baby1.vecBits);
            Mutate(baby2.vecBits);

            //add to new population
            vecBabyGenomes.add(baby1);
            vecBabyGenomes.add(baby2);

            NewBabies += 2;
        }

        m_vecGenome.clear();
        for(int i =0; i < m_iPopSize; i++){
            m_vecGenome.add(vecBabyGenomes.get(i));
        }

        ++m_iGeneration;
    }

    public int Generation(){ return m_iGeneration;}
    public int GetFittest(){return m_iFittestGenome;}
    boolean Started(){return m_bBusy;}
    public void Stop(){m_bBusy = false;}
}