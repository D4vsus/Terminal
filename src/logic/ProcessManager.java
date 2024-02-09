package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * <h1>ProccesManager</h1>
 * <p>Run programs and help to manage it</p>
 * @author D4vsus
 */

public abstract class ProcessManager extends TerminalManager {
	
	//Variables and objects
	private static ProcessBuilder processBuilder;
	private static Process process;
	private static BufferedReader inputStream;
	private static BufferedReader errorStream;
	private static BufferedWriter outputStream;

	//Methods
	
	/**
	 * <h1>ProccesManager.loadKeys()</h1>
	 * <p> Load all the keys from the process manager</p>
	 * @author D4vsus
	 */
	
	public static void loadKeys() {
		TerminalManager.addNewCommand("./", () -> ProcessManager.runProgram());
		TerminalManager.addNewCommand("SENDTOPROGRAM", () -> ProcessManager.sendToProgram());
		TerminalManager.addNewCommand("EXITFROMPROGRAM", () -> ProcessManager.exitProgram());
	}
	
	/**
	 * <h1>ProccesManager.setControlZ()</h1>
	 * <p> Macro to shut Down programs</p>
	 * @author D4vsus
	 */
	
	protected static void setControlZ() {
		if(ProcessManager.process.isAlive()) {
			ProcessManager.process.destroy();
			TerminalManager.println("Program ended by ctrl+Z");
			TerminalManager.println("Exit value: " + ProcessManager.process.exitValue());
		}
	}
	
	/**
	 * <h1>ProccesManager.sendToProgram()</h1>
	 * <p> Takes the input the input line and send them to as program</p>
	 * @author D4vsus
	 */
	
	public static void sendToProgram() {
		if(ProcessManager.isAliveProgram()) {
			ProcessManager.setOutputStream("SENDTOPROGRAM "+TerminalManager.window.read());
		} else {
			window.println("No program running");
		}
	}
	
	/**
	 * <h1>ProccesManager.exitProgram()</h1>
	 * <p> End the program program if it was on</p>
	 * @author D4vsus
	 */
	
	public static void exitProgram() {
		Integer exitCode;
		if((exitCode = shutDown()) != null) {
			TerminalManager.window.println("\nExit progam by ctrl+Z");
			TerminalManager.window.println("Exit Code: "+exitCode.intValue());
		}
	}
	
	/**
	 * <h1>ProccesManager.runProgram()</h1>
	 * <p> Initialize the program</p>
	 * @author D4vsus
	 */
	
	private static void runProgram() {	
		ProcessManager.startProgram(TerminalManager.commandLine.subList(1, TerminalManager.commandLine.size()));
		new Thread(() -> ProcessManager.whileRunning()).start();
		if (!ProcessManager.isAliveProgram()){TerminalManager.println("Program not found");}
	}

	/**
	 * <h1>ProccesManager.startProgram()</h1>
	 * <p>Initialize the program</p>
	 * @param arguments : List of String
	 * @author D4vsus
	 */
	
	protected static void startProgram(List<String> arguments){
		try {
			ProcessManager.processBuilder = new ProcessBuilder(arguments);
			ProcessManager.process = processBuilder.start();
			ProcessManager.inputStream = ProcessManager.process.inputReader();
			ProcessManager.errorStream = ProcessManager.process.errorReader();
			ProcessManager.outputStream = ProcessManager.process.outputWriter();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1>ProccesManager.endProgram()</h1>
	 * <p>End the program if it wasn't</p>
	 * @throws IOException
	 * @author D4vsus
	 */
	
	protected static void endProgram() {
		try {
			ProcessManager.process.destroy();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1>ProccesManager.getInputChar()</h1>
	 * <p>Gets a last read letter letter of the input stream of the program </br></p>
	 * <p>translated to a strings</p>
	 * @return String
	 * @author D4vsus
	 */
	
	protected static String getInputChar() {
		try {
			int character = 0;
			character = ProcessManager.inputStream.read();
			if (character != -1) {
				return ""+(char)character;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	/**
	 * <h1>ProccesManager.getErrorStream()</h1>
	 * <p>Gets the error stream of the program </br> translated to an array of strings</p>
	 * @return String[]
	 * @author D4vsus
	 */
	
	protected static String getErrorChar() {
		try {
			int character = 0;
			character = ProcessManager.errorStream.read();
			if (character != -1) {
				return ""+(char)character;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	/**
	 * <h1>ProccesManager.setOutputStream()</h1>
	 * <p>Sets the output stream of the program </p>
	 * @param input : String
	 * @author D4vsus
	 */
	
	protected static void setOutputStream(String input) {
		try {
			ProcessManager.outputStream.write(input + '\n');
			ProcessManager.outputStream.flush();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1>ProccesManager.whileRunning</h1>
	 * <p> Run while the program is running </p>
	 * @author D4vsus
	 */
	
	protected static void whileRunning() {
		String letter;
	 	
		while ((letter = ProcessManager.getInputChar()) != null)
		{TerminalManager.print(letter);}

		while ((letter = ProcessManager.getErrorChar()) != null)
		{TerminalManager.print(letter);}
	}
	
	/**
	 * <h1>ProccesManager.isAliveProgram()</h1>
	 * <p> See if the program is alive </p>
	 * @return boolean
	 * @author D4vsus
	 */
	
	
	protected static boolean isAliveProgram() {
		if (ProcessManager.process != null) {
			return ProcessManager.process.isAlive();
		}
		else {
			return false;
		}
	}

	/**
	 * <h1>ProccesManager.shutDown()</h1>
	 * <p> shut down the program if it's alive </p>
	 * @return Integer : the exit value or null if there is no program running
	 * @author D4vsus
	 */
	
	protected static Integer shutDown() {
		if (ProcessManager.process != null) {
			ProcessManager.process.destroy();
			return ProcessManager.process.exitValue();
		}
		return null;
	}
}
