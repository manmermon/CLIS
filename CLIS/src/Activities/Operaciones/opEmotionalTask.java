/*
 * Copyright 2011-2013 by Manuel Merino Monge <manmermon@dte.us.es>
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
package Activities.Operaciones;

import Auxiliar.AffectiveObject;
import Auxiliar.RandomSort;
import GUI.MyComponents.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.crypto.IllegalBlockSizeException;

public class opEmotionalTask
{
	public static final String[] RANDOM_SORT_TEXT_TYPE = { "None", "Random Samples", "Random Samples & Groups", "Random Samples & Groups by Blocks" };
	public static final String[] RANDOM_SORT_BEHAVOIR_DESCRIPTOR = { "None", 
			"Random sorting and swapping samples in their groups.", 
			"Random sorting and swapping samples in their groups, and random ordered sub-block's groups. It is avoided that twice same groups are consecutives.", 
	"Random sorting and swapping samples in their groups. Groups are randomly sorted but local block level. It is avoided that twice same groups are consecutives in a block and between blocks." };

	public static final int NONE_SORT = 0;  
	public static final int RANDOM_SWAP_SAMPLES = 1;  
	public static final int RANDOM_SWAP_SAMPLES_AND_GROUPS = 2;
	public static final int RANDOM_SWAP_SAMPLES_AND_GROUPS_IN_BLOCKS = 3;

	private List<AffectiveObject> imgs = null;
	private List<AffectiveObject> sounds = null;

	private Iterator<AffectiveObject> itImgs = null;
	private Iterator<AffectiveObject> itsounds = null;

	private int randomSamples;

	private boolean preserveCorrespondence = false;
	private boolean slideGrp = true;

	private static opEmotionalTask op = null;

	private opEmotionalTask()
	{
		super();

		this.imgs = new ArrayList< AffectiveObject >();
		this.sounds = new ArrayList< AffectiveObject >();
	}

	public static opEmotionalTask getInstance()
	{
		if (op == null)
		{
			op = new opEmotionalTask();
		}

		return op;
	}

	public int getNumberImages()
	{
		return this.imgs.size();
	}

	public int getNumberSounds()
	{
		return this.sounds.size();
	}

	public void addPathImage(String file, int gr, int block, boolean fixPosition, boolean isSam, boolean beepActive)
	{
		this.imgs.add(new AffectiveObject(file, gr, block, fixPosition, isSam, beepActive, AffectiveObject.IS_PICTURE));

		this.itImgs = null;
	}

	/*
	public void addPathImages( List< String > files )
	{
		for( String f : files )
		{
			this.addPathImage( f, 0, 0 );
		}
	}
	 */
	public void addPathImages(List<AffectiveObject> affObjs)
	{
		this.imgs.addAll(affObjs);

		this.itImgs = null;
	}

	public void addSound(String file, int gr, int block, boolean fixPosition, boolean isSam, boolean beepActive)
	{
		this.sounds.add(new AffectiveObject(file, gr, block, fixPosition, isSam, beepActive, AffectiveObject.IS_SOUND ) );

		this.itsounds = null;
	}

	public void addSounds(List<AffectiveObject> affObjs)
	{
		this.sounds.addAll(affObjs);

		this.itsounds = null;
	}

	public void clear()
	{
		this.imgs.clear();
		this.sounds.clear();
		this.itImgs = null;
		this.itsounds = null;
	}

	public void resetIterators()
	{
		resetIteratorImgs();
		resetIteratorSounds();
	}

	public void resetIteratorImgs()
	{
		this.itImgs = null;
		this.itImgs = this.imgs.iterator();
	}

	public void resetIteratorSounds()
	{
		this.itsounds = null;
		this.itsounds = this.sounds.iterator();
	}

	public Iterator<AffectiveObject> getImages()
	{
		if (this.itImgs == null)
		{
			this.itImgs = this.imgs.iterator();
		}

		return this.itImgs;
	}

	public Iterator<AffectiveObject> getSounds()
	{
		if (this.itsounds == null)
		{
			this.itsounds = this.sounds.iterator();
		}

		return this.itsounds;
	}

	public void setOrderPresentation(int randomType, boolean preserveCorrespondence, boolean slideGroup)
	{
		this.randomSamples = randomType;

		this.preserveCorrespondence = preserveCorrespondence;
		this.slideGrp = slideGroup;
	}

	public void applyOrder() throws IllegalBlockSizeException
	{
		if (!this.preserveCorrespondence)
		{
			this.imgs = this.applyOrderAux(this.imgs);
			this.sounds = this.applyOrderAux(this.sounds);
		}
		else
		{			
			if (this.imgs.isEmpty())
			{											   
				this.sounds = applyOrderAux(this.sounds);
			}
			else if (this.sounds.isEmpty())
			{
				this.imgs = applyOrderAux(this.imgs);
			} 
			else if (this.imgs.size() != this.sounds.size())
			{
				throw new IllegalBlockSizeException("Number of slides and sounds are not equal.");
			}
			else
			{


				List<AffectiveObject> samples = new ArrayList< AffectiveObject >();
				int gr;
				for (int i = 0; i < this.imgs.size(); i++)
				{
					AffectiveObject pic = (AffectiveObject)this.imgs.get(i);
					AffectiveObject snd = (AffectiveObject)this.sounds.get(i);

					gr = pic.getGroup();
					int bl = pic.getBlock();
					boolean fix = pic.isFixPosition();
					boolean sam = pic.isSAM();
					boolean beep = pic.isBeepActive();

					if (!this.slideGrp)
					{
						gr = snd.getGroup();
						bl = snd.getBlock();
						fix = snd.isFixPosition();
						sam = snd.isSAM();
						beep = snd.isBeepActive();
					}

					samples.add( new AffectiveObject( i + "", gr, bl, fix, sam, beep, AffectiveObject.IS_OTHER ) );
				}

				List<AffectiveObject> auxImg = new ArrayList< AffectiveObject >();
				List<AffectiveObject> auxSound = new ArrayList< AffectiveObject >();
				for (AffectiveObject orderedAffObj : samples)
				{
					int index = new Integer( orderedAffObj.getPathFile() );

					auxImg.add((AffectiveObject)this.imgs.get(index));
					auxSound.add((AffectiveObject)this.sounds.get(index));
				}

				this.imgs = auxImg;
				this.sounds = auxSound;
			}
		}
		
		this.resetIterators();
	}

	private List<AffectiveObject> applyOrderAux(List<AffectiveObject> SAMPLES) throws IllegalArgumentException
	{
		List<AffectiveObject> orderedObjs = new ArrayList< AffectiveObject >();

		if ((SAMPLES != null) && (!SAMPLES.isEmpty()))
		{
			if (this.randomSamples <= NONE_SORT)
			{
				orderedObjs.addAll(SAMPLES);
			}
			else if ((this.randomSamples > NONE_SORT) && (this.randomSamples <= RANDOM_SWAP_SAMPLES_AND_GROUPS_IN_BLOCKS))
			{
				List<Tuple<AffectiveObject, Integer>> affFixPosition = new ArrayList< Tuple< AffectiveObject, Integer > >();

				int index = 0;
				Iterator<AffectiveObject> it = SAMPLES.iterator();

				while (it.hasNext())
				{
					AffectiveObject ob = it.next();
					if (ob.isFixPosition())
					{
						affFixPosition.add(new Tuple<AffectiveObject, Integer>(ob, Integer.valueOf(index)));
						it.remove();
					}

					index++;
				}

				// Random sorting and swapping samples
				//

				// Block list
				List<Integer> blocks = new ArrayList< Integer>();

				// Objects to order
				List<Tuple<Object, Integer>> S = new ArrayList< Tuple< Object, Integer > >();
				for (AffectiveObject sample : SAMPLES)
				{
					S.add(new Tuple<Object, Integer>( sample,  sample.getGroup() ) ) ;
					blocks.add( sample.getBlock() ); // To preserve block order
				}

				// Random sorting and swapping samples
				S = RandomSort.getSampleOrder(S, RandomSort.RANDOM_SWAP_SAMPLES_FIX_BLOCKS, true);
				for (int i = 0; i < S.size(); i++)
				{
					Tuple< Object, Integer > t = S.get( i );
					AffectiveObject affObj = ( AffectiveObject )t.x;

					Tuple<Object, Integer> newT = new Tuple< Object, Integer >(new AffectiveObject(affObj.getPathFile(), affObj.getGroup(), blocks.get( i ), 
							affObj.isFixPosition(), affObj.isSAM(), affObj.isBeepActive(), 
							affObj.getAffectiveObjectType()), t.y );
					S.remove(i);
					S.add(i, newT);
				}

				//
				// Groups & Blocks 
				//

				if (this.randomSamples == RANDOM_SWAP_SAMPLES_AND_GROUPS )
				{
					// Random groups
					//

					S = RandomSort.getSampleOrder(S, RandomSort.RANDOM_FIX_SAMPLES_RANDOM_BLOCKS, true);
				}
				else if (this.randomSamples == RANDOM_SWAP_SAMPLES_AND_GROUPS_IN_BLOCKS)
				{
					// Random groups by blocks
					//

					// Auxiliary variables

					// Block list					 
					blocks.clear();

					// In each block, affective object list 			 
					Map<Integer, List<Tuple<Object, Integer>>> blockMap = new HashMap< Integer, List< Tuple< Object, Integer > > >();

					// Set of group in the block
					Map< Integer, Set< Integer > > blockGroup = new HashMap< Integer, Set< Integer > >();

					// Check block enumeration and filling in auxiliary variables.
					int prevBlock = Integer.MIN_VALUE;
					int currentBlock;
					for (int i = 0; i < S.size(); i++)
					{
						Tuple<Object, Integer> ob = S.get(i);
						currentBlock = ((AffectiveObject)ob.x).getBlock();

						if (currentBlock < prevBlock) // Check enumeration
						{
							throw new IllegalArgumentException("Block sequence must be asdencing.");
						}

						prevBlock = currentBlock;

						// List of blocks

						if (!blocks.contains( currentBlock ))
						{
							blocks.add( currentBlock );
						}


						// Affective objects

						List<Tuple<Object, Integer>> lst = blockMap.get( currentBlock );
						if (lst == null)
						{
							lst = new ArrayList< Tuple< Object, Integer > >();
						}
						lst.add(ob);
						blockMap.put( currentBlock, lst);


						// Set of group in the block

						Set<Integer> blGr = blockGroup.get( currentBlock );
						if (blGr == null)
						{
							blGr = new HashSet< Integer >();
						}

						blGr.add( ob.y);
						blockGroup.put( currentBlock, blGr );
					}

					// Sorting by blocks and checking last and first group

					S.clear();
					Integer lastBlockGr = Integer.MIN_VALUE;

					for (Integer blck : blocks)
					{
						// To avoid that last group of previous block and the first group of the current block are equal. 
						Set< Integer > groupInBlock = blockGroup.get( blck );  
						groupInBlock.remove( lastBlockGr ); // If size == 0, last group of previous block and the first group of the current block must be equal  

						boolean cont = true; // control variable
						List<Tuple<Object, Integer>> orderedBlockSamples = new ArrayList< Tuple< Object, Integer > >();

						if (groupInBlock.size() > 1)
						{
							while (cont)
							{
								orderedBlockSamples = RandomSort.getSampleOrder( blockMap.get( blck ), RandomSort.RANDOM_FIX_SAMPLES_RANDOM_BLOCKS, true ); // sorting block's samples

								Tuple< Object, Integer > sam =  orderedBlockSamples.get( 0 );
								cont = ( sam.y == lastBlockGr ); // Check previous last group and first current group
							}

						}
						else
						{
							// If size == 0, last group of previous block and the first group of the current block must be equal
							orderedBlockSamples = RandomSort.getSampleOrder( blockMap.get( blck ), RandomSort.RANDOM_FIX_SAMPLES_RANDOM_BLOCKS, true ); // sorting block's samples

							if (groupInBlock.size() == 1)
							{
								int gr = groupInBlock.iterator().next();
								Iterator<Tuple<Object, Integer>> itOrderedBlock = orderedBlockSamples.iterator();

								List<Tuple<Object, Integer>> auxOrderedBlock = new ArrayList< Tuple< Object, Integer > >();

								boolean stop = false;
								while ((itOrderedBlock.hasNext()) && (!stop))
								{
									Tuple<Object, Integer> t = itOrderedBlock.next();
									if ( t.y == gr)
									{
										auxOrderedBlock.add(t);
										itOrderedBlock.remove();
									}
									else
									{
										stop = !auxOrderedBlock.isEmpty();
									}
								}

								for (Tuple<Object, Integer> t : auxOrderedBlock)
								{
									orderedBlockSamples.add(0, t);
								}
							}
						}

						if (orderedBlockSamples.size() > 0)
						{
							lastBlockGr = orderedBlockSamples.get(orderedBlockSamples.size() - 1).y;
						}

						S.addAll(orderedBlockSamples);
					}
				}

				for( Tuple< Object, Integer > t : S )
				{
					orderedObjs.add( (AffectiveObject)t.x );
				}

				for( Tuple< AffectiveObject, Integer > aff : affFixPosition )
				{
					orderedObjs.add( aff.y, aff.x );
				}
			}
		}

		return orderedObjs;
	}
}