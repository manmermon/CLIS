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

package Controls;

import Auxiliar.WarningMessage;
import java.util.Map;

public abstract interface IControlLevel2
{
  public static final int StopWithTaskDone = -1;
  public static final int StopInNextLoopInteraction = 0;
  public static final int ForcedStop = 1;
  
  public abstract void addSubordinates(Map<String, Object> paramMap) throws Exception;
  
  public abstract void deleteSubordinates(int paramInt);
  
  public abstract void setSubordinates(String paramString, Object paramObject) throws Exception;
  
  public abstract void toWorkSubordinates(Object paramObject) throws Exception;
  
  public abstract void setControlSupervisor(IControlLevel1 paramIControlLevel1);
  
  public abstract boolean isWorking();
  
  public abstract void setBlockingStartWorking(boolean paramBoolean);
  
  public abstract boolean getBlockingStartWorking();
  
  public abstract WarningMessage checkParameters();
}