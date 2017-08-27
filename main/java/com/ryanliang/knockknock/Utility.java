/**
 *
 * @author Ryan L.
 */

package com.ryanliang.knockknock;

public class Utility {

	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException ee)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
}
