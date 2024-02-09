package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.Window;


/**
 * <h1> Terminal </h1>
 * <p>Run a terminal in your computer</p>
 * @author David
 */

public abstract class Terminal {
	//Variables and objects 
	private Window window;
	private Engine engine;
	private Map<String, Runnable> commands;
	
	//Methods
	/**
	 * <h1> Terminal.Terminal() </h1>
	 * <p> Initialize the class and it keys </p>
	 * @throws Exception
	 * @author David
	 */
	
	public Terminal() {
		this.engine = new Engine() {
			@Override
			public void whileRunning() {
				updateCommunication();
			}
		};
		this.window = new Window("Terminal",2000,720) {

			private static final long serialVersionUID = 1L;

			public void enterAcction() {
				setEnterAcction();
			}
		};
		this.commands = new HashMap<>();
		try {
			this.loadKeys();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <h1> Terminal.enterAcction() </h1>
	 * <p> Run all commands when pressing enter </p>
	 * @throws Exception
	 * @author David
	 * @throws IOException 
	 */
	
	public void setEnterAcction() {
		try {
			if(this.engine.isAliveProgram()) {
				this.engine.setOutputStream(this.window.read());
			}
			else {
				this.runCommand(this.window.read().split("[ \\(\\)]")[0]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <h1> Terminal.loadKeys() </h1>
	 * <p> Load all the commands, starting with the new once <br/></p>
	 * <p> and ending with the default once </p>
	 * @throws Exception
	 * @author David
	 */
	
	private void loadKeys() throws Exception {
		this.addNewCommands();
		this.commands.put("clear", () -> this.window.clear());
		this.commands.put("print", () -> this.print(this.window.read().split("[\\(\\)]")[1]));
		this.commands.put("run", () -> this.runScript(this.window.read().split("[\\(\\)]")));
		this.commands.put("println", () -> this.println(this.window.read().split("[\\(\\)]")[1]));
		this.commands.put("exit", () -> this.finish());
		this.commands.put("./", () -> this.runProgram());
		this.commands.put("#", () -> this.dummy());
	}
	
	/**
	 * <h1> Terminal.runCommand() </h1>
	 * <p> Execute a command if it exist </p>
	 * @author David
	 */
	
    public void runCommand(String argument) {
    	if(this.commands.get(argument.replace("\\", "\\\\")) != null) {this.commands.get(argument.replace("\\", "\\\\")).run();}
		else {this.window.println("Unknown command");}
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//commands////////////////////////////////////////////////////////////////////////////////////////////////////
	private void print(String argument) {
		try {
			this.window.print(argument);
		} catch (Exception e) {
			this.window.println("Error:1:no arguments given");
		}
	}
	
	private void println(String argument) {
		try {
			this.window.print(argument+'\n');
		} catch (Exception e) {
			this.window.println("Error:1:no arguments given");
		}
	}
	
	/**
	 * <h1> Terminal.dummy() </h1>
	 * <p> An empty method </p>
	 * @author David
	 */
	
	private void dummy() {
	}
	
	/**
	 * <h1> Terminal.addNewCommands() </h1>
	 * <p> An abstract method to load new keys or other stuff.<br/></p>
	 * <p> To add new keys use <b><i>{@linkplain Terminal#addNewCommand()}</i></b></p>
	 * @author David
	 */
	
	public abstract void addNewCommands() throws IOException;
	
	/**
	 * <h1> Terminal.addNewCommand() </h1>
	 * <p> Add a new command to the terminal with a key string <br/></p>
	 * <p> and a method to add</p>
	 * @param key : String
	 * @param runnable
	 * @author David
	 */
	
	public void addNewCommand(String key,Runnable runnable) {
		commands.put(key, runnable);
	}
	
	/**
	 * <h1> Terminal.runProgram() </h1>
	 * <p> Run a program and print all the content<br/></p>
	 * @author David
	 */
	
	private void runProgram() {
		//write commands in to a list
		List<String> commands = new ArrayList<>();
		String[] passCommands;
		passCommands = this.window.read().split(" ");
		
		for(int x = 1;x < passCommands.length;x++) {
			commands.add(passCommands[x]);
		}
		
		//start the program
		this.engine.startProgram(commands);
		new Thread(this.engine).start();
	}
	
	
	/**
	 * <h1> Terminal.updateCommunication() </h1>
	 * <p> Print in the screen the output of the program </p>
	 * @author David
	 */
	
	public void updateCommunication() {
		try {	
			String letter;
			 	
			while ((letter = this.engine.getInputChar()) != null)
			{this.window.print(letter);}

			this.window.printAll(this.engine.getErrorStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void runScript(String[] file_name) {
		try {
			File file = new File(file_name[1]);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String argument;
			while ((argument=reader.readLine()) != null) {
				this.runCommand(argument.split("[ \\(\\)]")[0]);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <h1> Terminal.finish() </h1>
	 * <p> Stop the main program </p>
	 */
	
	public void finish() {
		this.window.dispose();
	}
}
