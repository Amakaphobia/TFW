package exceptions;

/**
 * Exception thrown if you try to run a not specified task
 * @author Dave
 *
 */
public class NoTaskChosen extends Exception 
{
	/**svid*/
	private static final long serialVersionUID = 4566362198265466555L;
	
	/**
	 * Constructor
	 * @param s the error message
	 */
	public NoTaskChosen(String s) 
	{
		super(s);
	}
}
