package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * <h1> Terminal </h1>
 * <p>Run a terminal in your computer</p>
 * @author D4vsus
 */

public abstract class CommandManager extends TerminalManager{
	
	//Methods
	
	/**
	 * <h1> CommandManager.loadKeys() </h1>
	 * <p> Load all the keys from the command manager </p>
	 * @author D4vsus
	 */
	
	public static void loadKeys()  {
		TerminalManager.addNewCommand("setFont", () -> CommandManager.setFont(commandLine.get(1)));
		TerminalManager.addNewCommand("ls", () -> CommandManager.ls(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("cd", () -> CommandManager.cd(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("mkdir", () -> CommandManager.mkdir(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("find", () -> CommandManager.findDisplay(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("touch", () -> CommandManager.touch(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("rm", () -> CommandManager.rm(commandLine.toArray(new String[commandLine.size()])));
		TerminalManager.addNewCommand("script", () -> CommandManager.runScript(commandLine.get(1)));
		TerminalManager.addNewCommand("printshiba", () -> CommandManager.printshiba());
	}

	/**
	 * <h1> CommandManager.ls() </h1>
	 * <p> Print a list with all the content of the directory</p>
	 * @paramr String : direction of the directory
	 * @author D4vsus
	 */
    
	private static void ls(String... dir) {
		File[] fileName = new File(((dir.length > 1)) ? dir[1] : virtualPath).listFiles();
		
		DecimalFormat df = new DecimalFormat("#,###");
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

		TerminalManager.window.println("\nDirectory: "+fileName[0].getParent());
		TerminalManager.window.println("\nThe storage they are asigned is: "+  df.format(fileName[0].getTotalSpace()) + " Bytes");
		TerminalManager.window.println("The storage they are free is: "+ df.format(fileName[0].getFreeSpace()) + " Bytes\n");

		int longestName=0;
		
		for (int i = 0; i < fileName.length; i++) {
			if(fileName[i].getName().length() > longestName) {
				longestName = fileName[i].getName().length();
			}
		}

		TerminalManager.window.println(String.format("NAME%" + (longestName - "NAME".length() + 1) + "c PROPERTIES %5c SIZE %6cLAST MODIFICATION",' ',' ',' '));
		TerminalManager.window.println(String.format("----%" + (longestName - "----".length() + 1) + "c ----------- %4c ---- %6c-----------------",' ',' ',' '));
		
		for (int i = 0; i < fileName.length; i++) {
					TerminalManager.window.println(String.format
							("%s%" + (longestName - fileName[i].getName().length() + 3) + "c %c %c %c %c %-6c %-10d %s",
							   fileName[i].getName(),
							  (fileName[i].isDirectory())?'D':'-',
							  (fileName[i].isFile())?'F':'-',
							  (fileName[i].canRead())?'R':'-',
							  (fileName[i].canWrite())?'W':'-',
							  (fileName[i].canExecute()?'X':'-'),
							  (fileName[i].isHidden()?'H':'-'),
							   fileName[i].length(),
							   sdf.format(fileName[i].lastModified())));
		}
	}
	
	/**
	 * <h1>CommandManager.cd()</h1>
	 * <p>Move the program through the directories</p>
	 * @paramr String : direction of the directory
	 * @author D4vsus 
	 */
	
	private static void cd(String... dir) {
		boolean found = false;
		if (new File(dir[1]).isDirectory()) {
			TerminalManager.virtualPath = dir[1];
			found = true;
		} else {
			File sonFile = new File(CommandManager.getFileActualFromDirectory(dir[1]));
			if (sonFile.isDirectory()) {
				TerminalManager.virtualPath = sonFile.getPath();
				found = true;		
			}
		}
		
		if (!found) {
			TerminalManager.window.println("path not found");
		} else {
		TerminalManager.window.println(TerminalManager.virtualPath);
		
		}
	}
	
	/**
	 * <h1>CommandManager.find()</h1>
	 * <p>Find a specify content of the name of a file in a directory</p>
	 * <p>and put it in to a big string</p>
	 * @paramr String : Name of the command 
	 * @paramr String : Name of the content of the name of the file
	 * @paramr String : Name of the content of path 
	 * @author D4vsus 
	 */
	
	private static String find(String... keyWord) {
		File mainDir = new File(keyWord[2]);
		StringBuffer strBuffer = new StringBuffer();

		try {
			for(File file : mainDir.listFiles()) {
				if (file.getName().contains(keyWord[1])) {
					strBuffer.append(String.format("%s  %s\n",file.getName(),file.getPath()));
				}
				if (file.isDirectory() && !Thread.currentThread().isInterrupted()) {
					strBuffer.append(CommandManager.find(keyWord[0],keyWord[1],file.getPath()));
				}	
			}
		} catch (NullPointerException e) {
		// skip Null pointers
		}

		return strBuffer.toString();
	}
	
	/**
	 * <h1>CommandManager.findDisplay()</h1>
	 * <p>Display what find returns</p>
	 * @paramr String : Name of the command
	 * @paramr String : Name of the content of the name of the file
	 * @paramr String : Name of the content of path 
	 * @author D4vsus 
	 */
	
	private static void findDisplay(String... keyWord) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(CommandManager.find(keyWord));
		TerminalManager.window.println(strBuffer.toString());
		TerminalManager.window.println("Found :" + strBuffer.toString().split("\n").length + " files.");
	}
	
	/**
	 * <h1>CommandManager.getFileActualDirectory()</h1>
	 * <p>Return the path of a file in the actual directory</p>
	 * @paramr String : name of the file
	 * @return String : the path of the file, or null if wasn't found
	 * @author D4vsus 
	 */
	
	private static String getFileActualFromDirectory(String dir) {
		File ParentDirectory = new File(TerminalManager.virtualPath);
		
		for (int i = 0; i < ParentDirectory.list().length; i++) {
			
			if (dir.equals(ParentDirectory.list()[i]) && ParentDirectory.listFiles()[i].isDirectory()) {
				
				return ParentDirectory.listFiles()[i].getPath();
			}
		}
		return null;
	}
	
	/**
	 * <h1>CommandManager.mkdir()</h1>
	 * <p>Make a directory</p>
	 * @paramr String : direction of the directory or directory
	 * @author D4vsus 
	 */
	
	private static void mkdir(String... arguments) {
		File newDir = new File((arguments.length > 2) ? arguments[2]+arguments[1] : TerminalManager.virtualPath+arguments[1]);
		
		if(!newDir.exists()) {
			newDir.mkdir();
		} else {
			TerminalManager.window.println("The directory exist already");
		}
	}
	
	/**
	 * <h1>CommandManager.touch()</h1>
	 * <p>Make a file</p>
	 * @paramr String : direction of the directory or directory
	 * @author D4vsus 
	 */
	
	private static void touch(String... arguments) {
		File newFile = new File((arguments.length > 2) ? arguments[2]+arguments[1] : TerminalManager.virtualPath+arguments[1]);
		
		if(!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			TerminalManager.window.println("The directory exist already");
		}
	}
	
	/**
	 * <h1>CommandManager.rm()</h1>
	 * <p>Removes a file</p>
	 * @paramr String : direction of the directory or directory
	 * @author D4vsus 
	 */
	
	private static void rm(String... arguments) {
		File newFile = new File((arguments.length > 2) ? arguments[2]+arguments[1] : TerminalManager.virtualPath+arguments[1]);
		
		if(newFile.exists()) {
			newFile.delete();
		} else {
			TerminalManager.window.println("The file dosen't exist");
		}
	}
	
	/**
	 * <h1> CommandManager.setFont() </h1>
	 * <p> Set the font of the terminal</p>
	 * @paramr String : Font you want to add
	 * @author D4vsus
	 */
	
	private static void setFont(String argument) {
		TerminalManager.window.setFontOfTheWindow(argument);
	}

	/**
	 * <h1> CommandManager.printshiba() </h1>
	 * <p> Print a shiba inu</p>
	 * @author D4vsus
	 */
	
	private static void printshiba() {
		TerminalManager.window.println("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⠙⠛⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⢸⣿⠖⠈⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠻⠁⠛⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⢸⠟⠀⠘⡇⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⢠⠿⣿⣿⡆⠈⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⢸⣾⠀⠀⠹⣀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⢀⡀⠘⠀⠙⢻⡇⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⢸⣧⡀⠀⠀⢣⢇⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢃⣤⣻⡗⠂⠀⣴⣄⡇⠀⠀⢸⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠘⠉⣑⠄⠀⠀⠫⠧⡈⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢋⡴⠋⢿⠉⡍⠃⠀⠿⣿⣧⣄⠀⠸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⡂⠀⠈⠂⠀⢀⠀⠀⣤⣦⣌⠙⠛⠻⠿⠿⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠋⢀⡈⠀⢐⠾⠘⠃⢠⡐⠀⠈⣿⣷⡀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡏⢰⠁⠀⢠⣦⣶⣿⣷⠈⠉⢿⠿⣿⣿⣿⣶⣿⣶⣦⣤⣀⡀⣀⠀⣉⠉⢉⠛⠛⠛⠉⠀⠔⠈⠀⣰⣾⡖⠀⠀⣀⠉⠀⠀⣾⣯⡁⠀⢸⣿⣟⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⣀⣤⣾⣿⣿⣿⣿⣿⣷⠀⠀⠐⢆⠸⣗⢻⠋⠟⠻⡟⢿⠿⡿⠿⠿⠿⣿⡟⠛⠿⣾⠃⠀⠀⣆⡈⠑⠃⠀⠀⠀⠀⠀⢠⣴⣿⡇⠀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⢁⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⠇⣴⠀⢸⣦⡶⣼⣳⣿⢾⡷⣿⢶⣷⣤⠺⣑⢫⡹⣿⣶⡄⠀⠀⠘⠄⢙⢻⡆⠴⠀⠀⠀⠀⠀⠀⣻⣿⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⢛⣉⣤⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⣴⠏⠀⣘⣻⣟⣿⣿⣿⣻⡟⣿⣿⣿⣿⣿⣿⣿⣿⣿⣹⣿⣶⡄⠀⣳⣾⡟⠀⠀⠀⠀⠀⠀⢀⠀⣿⡇⠀⢰⡟⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⢠⣾⣿⣿⣿⣿⣯⣿⣿⡏⢻⣿⣿⣿⡿⠏⠀⠋⠀⣸⣿⢻⠇⢙⣉⣏⣙⡇⢻⣿⣿⣿⣿⣿⣿⣿⣿⣅⠉⢿⣷⢠⣸⡿⠋⠀⠀⠀⠀⠀⡀⢺⣇⡉⢁⠀⢻⣏⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⠋⣠⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀⠀⢸⣿⣿⡇⢘⠀⠀⣰⣿⡟⢈⣷⣿⣿⣿⣿⣿⣦⠈⣿⣿⣿⣿⣾⣿⣿⣿⣷⣼⣻⣿⣋⡀⣠⡄⠀⠀⢤⣀⣿⡿⠻⠿⣯⡀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⠃⢴⣼⠟⠉⡟⢿⣿⠇⠈⠀⠉⠀⠀⠀⢾⣿⠹⡄⠨⠃⠀⣿⡟⠀⣼⣿⣿⣿⣿⡿⣿⣿⠆⣹⣿⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⣿⣧⣹⣧⣼⣾⣾⣿⣿⠓⠀⠶⣿⣿⣆⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⡇⠀⠀⣀⣤⣤⣶⣘⠀⠀⠀⠀⠀⡀⠀⠠⢿⡷⢰⡗⡆⠀⠜⠻⠁⣼⣿⣿⣿⣿⣾⣿⣿⣿⡄⢿⣿⣿⢻⠿⣿⣿⣿⣿⡿⣿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⡄⢈⣿⣿⣆⠙⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⠀⠀⣸⣿⣿⡿⢟⠋⢳⣤⠀⠀⠀⠃⠀⣶⢸⣧⣹⠃⠀⠀⣶⡆⠀⢻⣿⣿⣿⡛⠏⠛⠋⠙⠃⠾⣟⣿⣿⣆⡟⣹⣿⢻⣟⣯⢚⡝⣿⣾⣏⣿⡉⣯⢻⡿⣏⡀⠀⠈⠙⢻⣿⡆⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣟⢀⢠⣿⣿⣿⠇⠘⣀⠈⠛⠀⠀⠀⢀⣧⡏⠸⠁⣀⡀⣠⣾⠿⠏⠀⠸⢿⠟⠃⠈⡀⢠⣤⡀⠀⠀⠙⠛⣼⣿⣿⣿⣿⣿⣿⡿⠿⠿⠟⢿⣿⡿⣿⡷⣭⠀⠹⣿⣆⠀⢴⣾⣿⣧⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⢾⢸⢽⣿⣿⣿⠀⢠⡿⢦⢀⣀⣀⣾⣿⠃⣀⣰⣾⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀⢀⣿⠀⠀⠁⠀⠀⠀⠀⢩⢀⠉⢀⣠⣈⣀⣁⣠⣴⠆⠈⡁⠁⢤⣤⣤⣄⠀⠸⣷⠀⠈⠻⣿⣯⡀⢸⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⢀⡪⢽⣿⣿⣿⠀⡈⠁⢦⣿⣿⣿⣿⣥⣴⣿⣿⣿⣿⣿⣿⠟⠀⢈⣇⢰⢠⠀⠀⠈⣿⣆⠀⠀⠀⠀⠀⣴⡾⠎⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣌⣿⣿⣿⣆⠀⠻⢧⠀⡘⠛⣿⡿⠀⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⢰⠸⡨⣿⣿⣿⣿⡀⢿⣷⣷⣿⣿⣿⣿⣿⣿⣿⣻⢿⡿⣿⣿⡇⠀⢸⣿⢀⠉⠳⢤⣤⣄⣀⠀⠀⢀⣰⣷⠿⠁⣰⣿⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠗⠀⠸⣷⣌⠑⣿⡇⠀⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣇⠃⠐⣻⣿⣿⣿⡿⠃⠈⠉⠉⠉⠉⠛⠻⣿⣿⣟⣿⣿⣿⣿⣿⣷⣴⣿⣿⡌⠁⠘⠟⠿⠟⠿⠿⠞⠀⣈⣧⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⡆⠀⠸⠧⠸⣿⣧⠀⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⡆⠈⡀⠛⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⢮⣉⡷⣭⣍⡛⠿⣿⣿⣇⢰⣶⣦⣶⣶⣶⣤⣴⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣏⠁⠰⣦⠐⣸⣿⣿⡄⠹⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⡏⢀⡾⠁⣼⣿⣿⣁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣽⣷⣆⢿⣿⣶⣌⢿⣷⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⢀⠂⠑⢈⡻⣿⣷⡄⢹⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⡇⢸⣇⠸⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡄⢸⣿⣿⣿⣦⡛⢿⣿⣶⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣏⡀⠀⢻⣄⢢⣕⣹⣿⣿⠀⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⠀⢿⣷⠀⢿⣿⡿⠀⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⣦⣾⣿⣿⣿⣿⣿⡆⣹⣏⢀⣿⣿⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⣿⣿⣿⣿⣿⣿⡿⠀⣸⣧⠴⣯⣿⣿⣿⡀⢻⣿⣿⣿⣿⣿\r\n"
				+ "⣿⡇⢸⣿⣧⡘⣿⣧⠀⠁⠀⢀⣀⢀⣀⠀⠀⢳⣤⣉⢻⣿⣿⣿⣿⣿⡿⢇⣹⣿⠉⢿⣿⣿⣿⣤⣹⣟⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⢋⣱⣤⣶⣶⣶⣶⣶⣿⣾⣿⡷⠉⠀⣾⣇⡷⣿⣯⢽⣿⣿⡇⠸⣿⣿⣿⣿⣿\r\n"
				+ "⣿⡇⢸⣿⣿⣷⣌⣻⣦⡀⠐⠂⠱⢤⣤⡄⠦⠀⢸⣷⠸⠿⣷⣶⣿⢧⡄⣾⣷⣿⣶⣦⣭⡙⠻⣿⣿⣿⡟⢿⣿⣿⣿⣿⣿⠟⢩⣥⣶⣾⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣿⣧⠀⣷⣿⣻⣿⢻⣧⣻⠾⣿⣿⡄⢻⣿⣿⣿⣿\r\n"
				+ "⣿⡇⢸⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠈⠂⠛⠀⠀⢀⣀⣶⣶⣆⣾⣿⡿⢿⣿⣿⠻⢿⣿⣷⣾⣿⣷⣶⣀⠙⢻⣿⣁⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢿⣶⣿⣿⣿⣿⣿⠇⢀⡈⣿⢻⣿⢉⠷⡉⠃⢻⣿⣇⠈⢿⣿⣿⣿\r\n"
				+ "⣿⣷⡀⢻⣿⣿⣿⣿⣿⣇⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠻⠿⠟⠛⠛⠃⠊⠁⠀⠀⠀⢹⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣼⣿⣿⣿⣿⠉⠉⣄⣸⣇⣿⣹⣿⣾⠀⠀⠀⣏⢸⡟⠃⠸⣿⣿⣿\r\n"
				+ "⣿⣿⣷⢸⣿⣿⣿⣿⣿⣿⣿⣄⠐⢦⡀⠀⢀⣀⣀⠀⠀⠀⠀⠀⠀⣠⣀⣤⣤⣴⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠘⣿⣿⣏⣿⢻⣟⡿⠀⠀⡠⣿⢺⣿⣴⡀⢻⣿⣿\r\n"
				+ "⣿⣿⣿⡆⢹⣿⣿⢿⣿⣿⣿⣿⣷⣶⣤⣆⣾⣿⣿⣿⣦⣴⣄⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⢀⣿⣿⣿⣿⠈⠁⠀⣠⣮⠴⣯⡀⠙⣿⠃⠸⣿⣿\r\n"
				+ "⣿⣿⣿⡇⢾⣿⣿⡿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠁⣠⣜⣿⣿⡿⡏⠀⠀⢀⣬⣿⣏⡧⣽⡎⢱⣆⠐⣿⣿\r\n"
				+ "⣿⣿⣿⣷⡀⢻⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⡐⢹⢿⡿⢿⠂⠀⠀⠀⢾⣿⣿⢺⡇⠅⢠⣿⣿⡄⣿⣿\r\n"
				+ "⣿⣿⣿⣿⡇⠀⢟⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⣠⣾⣿⠈⠉⠃⠠⠆⠐⠀⢀⢸⣿⣿⠻⠃⢀⣾⣿⣿⡇⢸⣿\r\n"
				+ "⣿⣿⣿⡏⢠⣥⣤⣄⠈⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣾⡿⢉⡤⠁⠀⢀⠘⠀⠂⢀⣬⣹⡟⠏⠀⣀⣾⣿⣿⣿⡇⢸⣿\r\n"
				+ "⣿⣿⣿⢀⣿⣯⠀⢻⣦⡀⠈⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣤⣭⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢀⣀⡍⠀⣄⡋⠀⠀⠀⣼⡿⠋⣩⣴⣾⣿⣿⣿⣿⣟⠀⣿⣿\r\n"
				+ "⣿⣿⣿⢨⣿⣿⡆⠈⢿⣿⣶⣀⠀⠙⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠛⢋⣰⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⢐⠻⠁⠀⠀⣤⡏⢁⣸⣿⣿⣿⣿⣿⣿⣿⣿⠀⢿⣿\r\n"
				+ "⣿⣿⡿⠘⣿⣿⣿⣧⡀⠘⣿⣿⣷⣤⡀⠀⠀⠉⠋⠙⠻⠿⠟⠻⠟⠉⠉⠛⠛⠋⠙⠋⠉⠉⠀⠀⢀⣀⣴⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠀⠊⠀⠀⢠⣴⣿⣶⣿⣿⣿⣿⣿⣿⣿⣿⠏⣴⣦⠸⣿\r\n"
				+ "⣿⣿⠁⣼⣿⣿⣿⣿⣿⣦⣉⣽⣿⣿⣿⣾⣶⣦⠀⠦⡀⠴⣤⡀⣖⢦⣐⢢⣰⢆⡐⠖⡂⠀⣀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡏⢀⣀⢀⣠⣶⢿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠂⣿⡷⠈⣿\r\n"
				+ "⣿⣿⡀⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀⠀⠀⠄⠀⠀⠤⠠⢈⠀⠀⢈⣀⣀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⣯⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⢰⣿⣿⠀⢻\r\n"
				+ "⣿⣿⠃⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⢠⣶⡄⠈⢶⡰⢀⠁⠀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⢸⣿⣿⠀⢸\r\n"
				+ "⣿⠟⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⠀⠀⠅⠠⣷⣀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢃⣠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣸⣿⣿⡄⢸\r\n"
				+ "⡟⠁⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⢀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢋⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⣹⣽⡿⠿⠇⠠\r\n"
				+ "⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀⣠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⣣⣼⣿⢿⣟⣿⠁⢸\r\n"
				+ "⣤⡙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⣹⣾⣿⡿⣿⣿⡿⠟⢠⣿\r\n"
				+ "⣿⣿⣤⡙⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⡠⣿⣿⣿⣿⣿⠟⣁⣴⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣦⣌⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢟⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠋⢉⠠⣷⣿⣿⡿⠛⣁⣴⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣷⣦⣙⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢋⣰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠇⠰⣿⣿⣷⡛⠟⢉⣠⣾⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣉⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣯⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠁⣦⠀⠟⠙⣉⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣌⣙⠛⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢂⣩⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣤⣉⡙⠛⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠟⣋⣡⣴⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣤⣌⣉⣉⣉⡙⠛⠛⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠟⠛⣉⣉⣉⣉⣥⣤⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
	}
	
	/**
	 * <h1> CommandManager.runScript() </h1>
	 * <p> Run a script </p>
	 * @param String : Name of the file
	 * @author D4vsus
	 */
	
	private static void runScript(String fileName) {
		try {
			File file = new File((new File(fileName).isFile())? fileName:TerminalManager.virtualPath + fileName );
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
				
				String argument;
				while ((argument=reader.readLine()) != null) {
					TerminalManager.commandLine = Arrays.asList(argument.split(" "));
					TerminalManager.runCommand(TerminalManager.commandLine.get(0));
				}
				reader.close();
			} else {
				TerminalManager.window.println("script not found");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
