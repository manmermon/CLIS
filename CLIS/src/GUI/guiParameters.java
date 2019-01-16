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

package GUI;

public class guiParameters
{
	private boolean showTestTimer = true;
	private boolean fullScreen = false;
	private int userAnswerTarget = 1;
	private int initialAnswerLevel = 0;
	private int timer = 60;
	private int idScreen = 1;

	public void setVisibleTestClock(boolean v)
	{
		this.showTestTimer = v;
	}

	public boolean isVisibleTestClock()
	{
		return this.showTestTimer;
	}

	public void setFullScreen(boolean full)
	{
		this.fullScreen = full;
	}

	public boolean isFullScreen()
	{
		return this.fullScreen;
	}

	public void setUserAnswerTarget(int target)
	{
		this.userAnswerTarget = target;
	}

	public int getUserAnswerTarget()
	{
		return this.userAnswerTarget;
	}

	public void setInitialAnswerLevel(int v)
	{
		this.initialAnswerLevel = v;
	}

	public int getInitialAnswerLevel()
	{
		return this.initialAnswerLevel;
	}

	public void setTimeValue(int time)
	{
		this.timer = time;
	}

	public int getTimeValue()
	{
		return this.timer;
	}

	public void setIdScreen(int screen)
	{
		if (screen < 1)
		{
			screen = 1;
		}

		this.idScreen = screen;
	}

	public int getIdScreen()
	{
		return this.idScreen;
	}
}