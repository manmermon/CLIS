package Auxiliar;

public class WarningMessage
{
  public static final int ERROR_MESSAGE = -1;
  public static final int OK_MESSAGE = 0;
  public static final int WARNING_MESSAGE = 1;
  private String msg;
  private int warningType;
  
  public WarningMessage()
  {
    this.msg = "";
    this.warningType = 0;
  }
  
  public String getMessage()
  {
    return this.msg;
  }
  
  public void setMessage(String message, int type)
  {
    this.msg = message;
    if (this.warningType == OK_MESSAGE )
    {
      this.warningType = type;


    }
    else if ((this.warningType > OK_MESSAGE) && (type != OK_MESSAGE ) )
    {
      this.warningType = type;
    }
  }
  
  public int getWarningType()
  {
    return this.warningType;
  }
}