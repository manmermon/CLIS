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

public class AffectiveObject
{
	public static final int IS_PICTURE = 0;  
	public static final int IS_SOUND = 1;  
	public static final int IS_VIDEO = 2;
	public static final int IS_OTHER = -1;

	
	private int group;
	private int block;
	private boolean fixPosition;
	private boolean isSam;
	private boolean beepActive;

	private String pathFile;
	private int typeAffectiveObj;

	public AffectiveObject(String path, int gr, int block, boolean fix, boolean sam, boolean beep, int typeAffectObj)
	{
		this.pathFile = path;
		this.group = gr;
		this.block = block;
		this.fixPosition = fix;
		this.typeAffectiveObj = typeAffectObj;
		this.isSam = sam;
		this.beepActive = beep;
	}

	public AffectiveObject(String path, int typeAffectObj)
	{
		this(path, 0, 0, false, true, false, typeAffectObj);
	}

	public int getAffectiveObjectType()
	{
		return this.typeAffectiveObj;
	}

	public boolean isPicture()
	{
		return this.typeAffectiveObj == IS_PICTURE;
	}

	public boolean isSound()
	{
		return this.typeAffectiveObj == IS_SOUND;
	}

	public boolean isVideo()
	{
		return this.typeAffectiveObj == IS_VIDEO;
	}

	public boolean isOther()
	{
		return this.typeAffectiveObj == IS_OTHER;
	}

	public int getGroup()
	{
		return this.group;
	}

	public int getBlock()
	{
		return this.block;
	}

	public String getPathFile()
	{
		return this.pathFile;
	}

	public boolean isFixPosition()
	{
		return this.fixPosition;
	}

	public boolean isSAM()
	{
		return this.isSam;
	}

	public boolean isBeepActive()
	{
		return this.beepActive;
	}

	private String createString()
	{
		return "<" + this.pathFile + ", " + this.group + ", " + this.block + ", " + this.fixPosition + ", " + this.isSam + ", " + this.beepActive;
	}

	public String toString() {
		return createString() + ">";
	}

	public String toStringComplete()
	{
		return createString() + ", " + this.typeAffectiveObj + ">";
	}

	public AffectiveObject clone()
	{
		return new AffectiveObject(this.pathFile, this.group, this.block, this.fixPosition, this.isSam, this.beepActive, this.typeAffectiveObj);
	}
}