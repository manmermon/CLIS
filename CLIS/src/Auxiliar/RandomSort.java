/*
 * Copyright 2011-2018 by Manuel Merino Monge <manmermon@dte.us.es>
 *  
 *   This file is part of CLIS.
 *
 *   CLIS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CLIS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CLIS.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package Auxiliar;

import GUI.MyComponents.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSort
{
	public static final int NUMBER_RANDOM_SAMPLE_TYPE = 6;
	public static final int RANDOM_NONE = 0;
	public static final int RANDOM_ALL_SAMPLES = 1;
	public static final int RANDOM_FIX_SAMPLES_RANDOM_BLOCKS = 2;
	public static final int RANDOM_RANDOM_SAMPLES_FIX_BLOCKS = 3;
	public static final int RANDOM_SWAP_SAMPLES_FIX_BLOCKS = 4;
	public static final int RANDOM_SWAP_SAMPLES_RANDOM_BLOCKS = 5;

	private static void checkInputs(List<Tuple<Object, Integer>> samples) throws IllegalArgumentException
	{
		if (samples == null)
		{
			throw new IllegalArgumentException("Input lists null.");
		}
	}

	public static List<Tuple<Object, Integer>> getSampleOrder(List<Tuple<Object, Integer>> samples, int type, boolean nonConsecutiveSameGroup) throws IllegalArgumentException
	{
		checkInputs(samples);

		List<Tuple<Object, Integer>> sam = new ArrayList(samples);

		switch (type)
		{
		case RANDOM_ALL_SAMPLES: 
		{
			Collections.shuffle(sam);

			break;	    
		}
		case RANDOM_FIX_SAMPLES_RANDOM_BLOCKS:
		{
			sam = randomFixSamplesRandomBlocks(samples, nonConsecutiveSameGroup);

			break;
		}
		case RANDOM_RANDOM_SAMPLES_FIX_BLOCKS:
		{
			sam = randomRandomSampleFixBlocks(samples);

			break;
		}
		case RANDOM_SWAP_SAMPLES_FIX_BLOCKS:
		{
			sam = randomSwapSampleFixBlocks(samples);

			break;
		}
		case RANDOM_SWAP_SAMPLES_RANDOM_BLOCKS:
		{
			sam = randomSwapSampleFixBlocks(samples);
			sam = randomFixSamplesRandomBlocks(sam, nonConsecutiveSameGroup);
			break;
		}
		}

		return sam;
	}

	/**
	 * Sample's indexes by groups. Group's order is preserved.
	 * @param samplesAndGroups
	 * @return List< Tuple< Integer, List< Integer > > >: group and its sample's indexes. 
	 */
	private static List<Tuple<Integer, List<Integer>>> getIndexByOrderedGroups(List<Tuple<Object, Integer>> samplesAndGroups)
	{
		// Group and indexes
		List<Tuple<Integer, List<Integer>>> GROUPS = new ArrayList<Tuple<Integer, List<Integer>>>();

		List<Integer> indexes = new ArrayList<Integer>();

		int prevGr = Integer.MIN_VALUE;

		for (int indexGr = 0; indexGr < samplesAndGroups.size(); indexGr++)
		{
			Tuple<Object, Integer> gr = samplesAndGroups.get(indexGr);

			if( gr.y != prevGr)
			{
				if (indexes.size() > 0)
				{
					Tuple<Integer, List<Integer>> group = new Tuple<Integer, List<Integer>>( prevGr, indexes);

					GROUPS.add(group);
				}

				prevGr = gr.y;
				indexes = new ArrayList< Integer >();
			}

			indexes.add( indexGr );
		}

		if (indexes.size() > 0)
		{
			GROUPS.add(new Tuple< Integer, List< Integer > >( prevGr, indexes));
		}

		return GROUPS;
	}

	private static List<Tuple<Object, Integer>> randomFixSamplesRandomBlocks(List<Tuple<Object, Integer>> samplesAndGroups, boolean nonConsecutiveSameGroup)
	{
		checkInputs(samplesAndGroups);

		List<Tuple<Object, Integer>> orderSamples = new ArrayList<Tuple<Object, Integer>>();

		List<Integer> groups = new ArrayList< Integer >();
		for (Tuple<Object, Integer> t : samplesAndGroups)
		{
			if (!groups.contains(t.y))
			{
				groups.add((Integer)t.y);
			}
		}

		List<Tuple<Integer, List<Integer>>> GROUPS = getIndexByOrderedGroups(samplesAndGroups);

		Map< Integer, List< List< Integer > > > grIndexes = new HashMap< Integer, List< List< Integer > > >();
		for (Tuple<Integer, List<Integer>> g : GROUPS)
		{
			List<List<Integer>> lst = grIndexes.get(g.x);
			if (lst == null)
			{
				lst = new ArrayList< List< Integer > >();
			}

			if (g.y != null)
			{
				lst.add( g.y);
			}

			grIndexes.put( g.x, lst);
		}

		Integer prevGr = Integer.MIN_VALUE;
		while ( grIndexes.size() > 0)
		{
			List< Integer > copyGroups = new ArrayList< Integer>(groups);
			boolean delPrevGr = false;

			if (nonConsecutiveSameGroup)
			{
				delPrevGr = copyGroups.remove(prevGr);
			}

			Collections.shuffle( copyGroups);

			if (delPrevGr)
			{
				int posPrevGr = copyGroups.size();
				if (posPrevGr > 1)
				{
					posPrevGr = ThreadLocalRandom.current().nextInt(0, posPrevGr) + 1;
				}
				copyGroups.add(posPrevGr, prevGr);
			}

			for (Integer gr : copyGroups)
			{
				List<List<Integer>> lst = grIndexes.get(gr);
				if ((lst != null) && (!lst.isEmpty()))
				{
					List<Integer> lstInds = lst.get(0);

					for (Integer index : lstInds)
					{
						orderSamples.add( samplesAndGroups.get( index ) );
					}

					lst.remove(0);
					if (lst.isEmpty())
					{
						grIndexes.remove(gr);
					}
				}
				else
				{
					grIndexes.remove(gr);
				}
			}

			prevGr = (Integer)((List)copyGroups).get(((List)copyGroups).size() - 1);
		}

		return orderSamples;
	}

	private static List<Tuple<Object, Integer>> randomRandomSampleFixBlocks(List<Tuple<Object, Integer>> samplesAndGroups)
	{
		checkInputs(samplesAndGroups);

		List<Tuple<Object, Integer>> sampleOrder = new ArrayList<Tuple<Object, Integer>>();

		List< Tuple< Integer, List< Integer > > > groups = getIndexByOrderedGroups( samplesAndGroups );

		for( Tuple< Integer, List< Integer > > g : groups )
		{
			Collections.shuffle( g.y );

			for( Integer index : g.y )
			{
				sampleOrder.add( samplesAndGroups.get( index ) );
			}
		}		

		return sampleOrder;
	}

	private static List<Tuple<Object, Integer>> randomSwapSampleFixBlocks(List<Tuple<Object, Integer>> samplesAndGroups)
	{
		List<Tuple<Object, Integer>> orderSample = new ArrayList<Tuple<Object, Integer>>();

		List<Tuple<Integer, List<Integer>>> groups = getIndexByOrderedGroups(samplesAndGroups);

		// Union of group's indexes by group.
		HashMap<Integer, List<Integer>> SG = new HashMap<Integer, List<Integer>>();

		for (Tuple<Integer, List<Integer>> grIndexes : groups)
		{
			int gr = grIndexes.x;

			List<Integer> indexes = SG.get( gr);

			if (indexes == null)
			{
				indexes = new ArrayList< Integer >();
			}

			indexes.addAll( grIndexes.y );

			SG.put(Integer.valueOf(gr), indexes);
		}


		for (Integer g : SG.keySet())
		{
			Collections.shuffle((List)SG.get(g));
		}

		// Random order
		for( Integer g : SG.keySet() )
		{
			Collections.shuffle( (List< Integer >)SG.get( g ) );
		}

		// Set samples
		for( Tuple< Integer, List< Integer > > grIndexes : groups )
		{      
			int gr =  grIndexes.x;
			int nSamples = grIndexes.y.size();

			List<Integer> indexes = SG.get( gr );
			for( int i  = 0; i < nSamples; i++ )
			{
				orderSample.add( samplesAndGroups.get( indexes.get( 0 ) ) );
				indexes.remove( 0 );
			}
		}

		return orderSample;
	}
}