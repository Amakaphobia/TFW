package taskFrameWork.backbone;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.function.Function;

import boxes.GenericContainer;
import factories.HandlerFactory;
import logic.A_InputHandler;

/**
 * This is the task SUperclass all Tasks need to implement these
 * @author Dave
 *
 */
public abstract class Task 
{
	/**?*/
	protected short inputLocation;
	/**The Name of the Task*/
	protected String Name;
	/**info about the task*/
	protected String Info;
	/**true if it should print*/
	protected boolean toPrint;
	/**bufferedreader for input*/
	protected BufferedReader br;
	/**Collection of Constructors for a inputhandler*/
	protected LinkedList<Function<HandlerFactory<?>, A_InputHandler<?>>> InputRecepies;
	/**Collection of inputhandlers*/
	protected LinkedList<A_InputHandler<?>> InputCollection;
	/**string collection for output*/
	protected LinkedList<String> Ausgabe;
	/**the return object*/
	protected GenericContainer<?> returnObj;
	
	/**
	 * Constructor
	 * @param br for user input
	 */
	public Task(BufferedReader br)
	{
		this.br 				= br;
		this.toPrint			= false;
		this.inputLocation		= 0;
		this.Ausgabe 			= new LinkedList<>();
		this.InputRecepies 		= new LinkedList<>();
		this.InputCollection	= new LinkedList<>();
	}
	
	/**
	 * sets the info string
	 * @param Info the string you want to save
	 */
	protected void setInfo(String Info)
	{
		this.Info = Info;
	}
	
	/**
	 * sets the name string
	 * @param Name the string you want to save
	 */
	protected void setName(String Name)
	{
		this.Name = Name;
	}
	
	/**
	 * used to set the value of print to true
	 */
	protected void setPrint()
	{
		this.toPrint = true;
	}
	
	/**
	 * Used to add a InputHandler Recipe to the queue
	 * @param f the recipe of the InputHandler
	 */
	protected void addInput(Function<HandlerFactory<?>, A_InputHandler<?>> f)
	{
		this.InputRecepies.add(f);
	}
	
	/**
	 * used to add a new answer line to the output string
	 * @param s the new line
	 */
	protected void addAnswer(String s)
	{
		this.Ausgabe.add(s);
	}
	
	/**
	 * returns the infofield
	 * @return a string containing information about the program
	 */
	public String getInfo()
	{
		return this.Info;
	}
	
	/**
	 * returns the namefield
	 * @return a string containing the name of the program
	 */
	public String getName()
	{
		return this.Name;
	}
	
	/**
	 * gets the value of the print status back
	 * @return true if its gonna print
	 */
	public boolean getToPrint()
	{
		return this.toPrint;
	}
	
	/**
	 * Method used to get the output text container
	 * @return a List of Strings
	 */
	public LinkedList<String> getAusgabe()
	{
		return this.Ausgabe;
	}
	
	/**
	 * Method used to get a Inputvalue back out
	 * @param i the index of the value you want to extract. starts with 0.
	 * @param <T> the output value type
	 * @return the input value
	 */
	public <T> T getValue(int i)
	{
		return this.InputCollection.get(i).getValue();
	}
	
	/**
	 * Method used to execute the program
	 * @param <T> the output value type 
	 * @return the output value
	 */
	@SuppressWarnings("unchecked")
	public <T> T runThis()
	{	
		System.out.println(this.Name);
		System.out.println(this.Info);
		
		this.handleInput();
		this.returnObj = this.compute();
		this.buildOutput();
		
		return (T)this.returnObj.getValue();
	}
	
	/**
	 * method used get the return value of the Program body
	 * @return the return value cast to the specified type
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getReturnValue()
	{
		return ((T) this.returnObj.getValue());
	}
	
	/**
	 * method used internally to handle the inputs
	 */
	protected void handleInput()
	{
		InputRecepies.stream()
					 .forEach(this::buildAndHandleMyInputs);
	}
	
	/**
	 * method used internally to call the stored Inputhandler recipes
	 * @param f the recipe you want to execute
	 */
	protected void buildAndHandleMyInputs(Function<HandlerFactory<?>, A_InputHandler<?>> f)
	{
		
		A_InputHandler<?> in = f.apply(new HandlerFactory<>());
		in.setLocation(this.inputLocation);
		this.InputCollection.add(in);		
		in.handleInput();		
	}
	
	/**
	 * method used to describe the program body
	 * @return the return object computed by the body
	 */
	protected abstract GenericContainer<?> compute();
	
	/**
	 * Method used to build outputtext list
	 */
	protected abstract void buildOutput();	
}
