package exceptions;

/**
 * Exception thrown if you try to call a list thats not existing
 * @author Dave
 *
 */
public class ListNotFound extends Exception 
{
	/**svid*/
	private static final long serialVersionUID = 7262816381641976305L;
	
	/**
	 * Constructor
	 * @param s the error message
	 */
	public ListNotFound(String s) 
	{
		super(s);
	}
}
