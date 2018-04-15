package taskFrameWork.backbone;

import java.io.BufferedReader;
import java.util.LinkedList;

import boxes.Pair;
import exceptions.ListNotFound;
import exceptions.NoTaskChosen;
import factories.HandlerFactory;
import factories.ParserFactory;
import logic.A_InputHandler;
import utils.TextBuilder;
import utils.XStringBuilder;

/**
 * Framework class to run tasks
 * @author Dave
 *
 */
public class TaskFrameWork 
{
	/**Aufforderung*/
	private String Aufforderung = "To Choose the List: ";
	/**Aufforderung*/
	private String Aufforderung2 = " please enter ";
	/**Aufforderung*/		
	private String Aufforderung3 = "To Choose the task: ";
	/**Aufforderung*/
	private String Line = "\n------------------------------------------------------\n";
	/**Aufforderung*/
	private String ListQuery = " Please Enter List Number";
	/**Aufforderung*/
	private String TaskQuery = " Please Enter Task Number";
	
	/**Liste contianing Listen von Tasklists mit deren namen*/
	private LinkedList<Pair<String, LinkedList<Task>>> AufgabenListeListe;
	/**buffered reader for user input*/
	private BufferedReader br;
	/**collection von aufgaben*/
	private LinkedList<Task> AufgabenListe;
	/**task to execute*/
	private Task Task;
	
	/**
	 * Method used to choose a list out of the listlist
	 * @param br for user input
	 * @param AufgabenListeListe is a list containing all tasklists
	 * @return a chosen linkedlist with tasks
	 */
	private LinkedList<Task> chooseList(BufferedReader br, LinkedList<Pair<String, LinkedList<Task>>> AufgabenListeListe )
	{
		XStringBuilder strb = new XStringBuilder("");
		HandlerFactory<Integer> CF = new HandlerFactory<>();
		A_InputHandler<?> input;
		
		int count = 0;
		int chosen;
		
		//Print List		
		for(Pair<String,LinkedList<Task>> Listenpaar : AufgabenListeListe)
		{
			strb.append(Aufforderung)
				.append(Listenpaar.getKey())
				.append(Aufforderung2)
				.append(count++)
				.append(Line);
			
			System.out.print(strb.pop());
		}
		
		//get Input
		input = CF.chooseHandlerType(HandlerFactory.CONSOLEHANDLER)
				  .setUserprompt(ListQuery)
				  .setReader(br)
				  .setParser(ParserFactory.PARSE_TO_INTEGER)
				  .UnsafeSetNumValidator(e -> e >= 0 && e < AufgabenListeListe.size())
				  .build();
		
		input.handleInput();
		chosen = input.<Integer>getValue();
		
		return AufgabenListeListe.get(chosen).getValue();
	}
		
	/**
	 * Method used to choose a Task out of a List
	 * @param br BufferedReader used to read the user Input
	 * @param Liste LinkedList Holding Tasks
	 * @return the chosen Task
	 */
	private  Task chooseTask(BufferedReader br, LinkedList<Task> Liste)
	{
		XStringBuilder strb = new XStringBuilder("");
		HandlerFactory<Integer> CF = new HandlerFactory<>();
		
		A_InputHandler<?> input;
		
		int count = 0;
		int chosen;
		
		//Print List
		for(Task Task : Liste)
		{
			strb.append(Aufforderung3)
			.append(Task.getName())
			.append(Aufforderung2)
			.append(count++)
			.append(Line);
		
			System.out.print(strb.pop());
		}
		
		//get Input
		input = CF.chooseHandlerType(HandlerFactory.CONSOLEHANDLER)
				  .setUserprompt(TaskQuery)
				  .setReader(br)
				  .setParser(ParserFactory.PARSE_TO_INTEGER)
				  .UnsafeSetNumValidator(e -> e >= 0 && e < Liste.size())
				  .build();
		
		input.handleInput();
		chosen = input.<Integer>getValue();
		
		return Liste.get(chosen);
	}
	
	/**
	 * Adds new TaskLists to the ListList
	 * @param Name Sets the name of the new Tasklist
	 */
	public void addList(String Name)
	{
		this.AufgabenListeListe.add(new Pair<String, LinkedList<Task>>(Name, new LinkedList<>()));
	}
	
	/**
	 * Adds a Task to specified tasklist;
	 * @param Liste The Name Of the Chosen List
	 * @param Task The Task you wish to add
	 * @throws Exception if there is no such list
	 */
	public void addTask(String Liste,Task Task) throws Exception
	{
		boolean failure = true;
		
		for(Pair<String,LinkedList<Task>> P : this.AufgabenListeListe)
		{
			if(Liste.equals(P.getKey()))
			{
				P.getValue().add(Task);
				failure = false;
				break;
			}
		}
		
		if(failure)
			throw new ListNotFound("Die gewählte Liste konnte nicht gefunden werden");
	}
	
	/**
	 * This Method Lets you run the Task out of the list
	 * @throws Exception if something isnt configuered correctly
	 */
	public void run() throws Exception
	{

		this.AufgabenListe = chooseList(br, AufgabenListeListe);

		Task = chooseTask(br, AufgabenListe);

		this.Task.runThis();

		if(this.Task.getToPrint())
			TextBuilder.printList(this.Task.getAusgabe());
		
	}
	
	/**
	 * Constructor
	 * @param br Buffered Reader used for user input
	 */
	public TaskFrameWork(BufferedReader br) 
	{
		this.br = br;
		this.AufgabenListeListe = new LinkedList<>();
	}
	
	/**
	 * Method used to run a task specified by simpleAddTask()
	 * @param Task the Task you wish to test
	 * @throws NoTaskChosen if something isnt configuered correctly
	 */
	public void simpleRun(Task Task) throws NoTaskChosen
	{
		this.Task = Task;
		
		if(this.Task == null)
			throw new NoTaskChosen("You need to specify a Task with .simpleAddTask first");
		this.Task.runThis();
		if(this.Task.getToPrint())
			TextBuilder.printList(this.Task.getAusgabe());
	}
	
	/**
	 * method used to get the chosen task
	 * @return a task if one is chosen out of a list
	 * @throws NoTaskChosen if there is no such task chosen yet
	 */
	public Task getTask() throws NoTaskChosen
	{
		if(this.Task == null)
			throw new NoTaskChosen("You need to choose a task out of a list before attempting that");
		return this.Task;
	}
}
