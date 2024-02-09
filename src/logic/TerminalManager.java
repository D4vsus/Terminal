package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UI.Window;

/**
 * <h1> CommandManager.loadKeys() </h1>
 * <p> Load all the keys from the command manager </p>
 * @author D4vsus
 */

public abstract class TerminalManager {
	
	//Variables and objects
	protected static Window window;
	private static Map<String, Runnable> commands;
	private List<String> stringBuffer;
	private byte posStringBuffer;
	protected static List<String> commandLine;
	protected static String virtualPath;
	
	//Methods
	
	public TerminalManager() {
		this.stringBuffer = new ArrayList<String>();
		this.posStringBuffer = -1;
		TerminalManager.virtualPath = new String(System.getProperty("user.dir"));
		TerminalManager.window = new Window("Golden Drag") {

			private static final long serialVersionUID = 1L;
			
			public void enterAcction() {
				setEnterAcction();
			}

			@Override
			public void setControlZ() {
				ProcessManager.exitProgram();
			}

			@Override
			public void setkeyUp() {
				if (posStringBuffer+1 < stringBuffer.size()) {
					posStringBuffer++;
					this.setInputText(stringBuffer.get(stringBuffer.size()-1-posStringBuffer));
				}
			}
			
			@Override
			public void setkeyDown() {
				if (posStringBuffer-1 >= 0) {
					posStringBuffer--;
					this.setInputText(stringBuffer.get(stringBuffer.size()-1-posStringBuffer));
				}
			}
		};
		TerminalManager.commands = new HashMap<>();
		try {
			this.loadKeys();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadKeys()  {
		this.addNewCommands();
		TerminalManager.commands.put("clear", () -> TerminalManager.clear());
		TerminalManager.commands.put("print", () -> TerminalManager.print(commandLine.get(1)));
		TerminalManager.commands.put("println", () -> TerminalManager.println(commandLine.get(1)));
		TerminalManager.commands.put("exit", () -> this.finish());
		TerminalManager.commands.put("#", () -> this.dummy());
	}
	
	/**
	 * <h1> Terminal.print() </h1>
	 * <p> Print a String</p>
	 * @paramr String : what you want to print
	 * @author D4vsus
	 */
	
	protected static void print(String argument) {
		try {
			TerminalManager.window.print(argument);
		} catch (Exception e) {
			TerminalManager.println("Error:1:no arguments given");
		}
	}
	
	/**
	 * <h1> Terminal.println() </h1>
	 * <p> Print a String and add a jump of line</p>
	 * @paramr String : what you want to print
	 * @author D4vsus
	 */
	
	protected static void println(String argument) {
		TerminalManager.print(argument + "\n");
	}
	
	/**
	 * <h1> Terminal.println() </h1>
	 * <p> Print a String and add a jump of line</p>
	 * @paramr String : what you want to print
	 * @author D4vsus
	 */
	
	protected static void clear() {
		TerminalManager.window.clear();
	}
	
	/**
	 * <h1> Terminal.dummy() </h1>
	 * <p> An empty method </p>
	 * @author D4vsus
	 */
	
	private void dummy() {
	}
	
	public abstract void addNewCommands();
	
	/**
	 * <h1> Terminal.addNewCommand() </h1>
	 * <p> Add a new command to the terminal with a key string <br/></p>
	 * <p> and a method to add</p>
	 * @param key : String
	 * @param Runnable : runnable
	 * @author D4vsus
	 */
	
	public static void addNewCommand(String key,Runnable runnable) {
		TerminalManager.commands.put(key, runnable);
	}
	
	public void finish() {
		TerminalManager.window.dispose();
	}
	
	private void setEnterAcction() {
		if(ProcessManager.isAliveProgram()) {
			ProcessManager.setOutputStream(TerminalManager.window.read());
		}
		else {
			this.commandAcction();
		}	
		
		if (this.stringBuffer.size() > 127) {
			this.stringBuffer.removeFirst();
		}
		this.stringBuffer.add(TerminalManager.window.read());
		this.posStringBuffer = -1;
	}

	private void commandAcction() {
			TerminalManager.commandLine = Arrays.asList(TerminalManager.window.read().split(" "));
			TerminalManager.runCommand(commandLine.get(0).toLowerCase()); 
			this.posStringBuffer = -1;
	}
	
	/**
	 * <h1> Terminal.runCommand() </h1>
	 * <p> Execute a command if it exist </p>
	 * @author D4vsus
	 */
	
    public static void runCommand(String argument) {
    	if(TerminalManager.commands.get(argument.replace("\\", "\\\\")) != null) {TerminalManager.commands.get(argument.replace("\\", "\\\\")).run();}
		else {TerminalManager.println("Unknown command");}
    }
}
