package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <h1>Engine</h1>
 * <p>Run programs and help to manage it</p>
 * @author David
 */

abstract class Engine implements Runnable{
	//Variables and objects
	private ProcessBuilder pb;
	private Process p;
	private BufferedReader input_stream;
	private BufferedReader error_stream;
	private BufferedWriter output_stream;

	//Methods

	/**
	 * <h1>Engine.startProgram()</h1>
	 * <p>Initialize the program</p>
	 * @param arguments : List of String
	 * @throws IOException
	 * @author David
	 */
	
	public void startProgram(List<String> arguments){
		try {
			this.pb = new ProcessBuilder(arguments);
			this.p = pb.start();
			this.input_stream = this.p.inputReader(Charset.forName("utf-8"));
			this.error_stream = this.p.errorReader(Charset.forName("utf-8"));
			this.output_stream = this.p.outputWriter(Charset.forName("utf-8"));
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1>Enigne.endProgram()</h1>
	 * <p>End the program if it wasn't</p>
	 * @throws IOException
	 * @author David
	 */
	
	public void endProgram() {
		try {
			this.p.destroy();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1>Engine.getInputChar()</h1>
	 * <p>Gets a last read letter letter of the input stream of the program </br></p>
	 * <p>translated to a strings</p>
	 * @return String
	 * @throws IOException
	 * @author David
	 */
	
	public String getInputChar() {
		try {
			int character = 0;
			character = input_stream.read();
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
	 * <h1>Engine.getErrorStream()</h1>
	 * <p>Gets the error stream of the program </br> translated to an array of strings</p>
	 * @return String[]
	 * @throws IOException
	 * @author David
	 */
	
	public String[] getErrorStream() {
		try {
			int character = 0;
			String output = new String();

			while((character = this.error_stream.read()) != -1) {//for each
				output += ((char)character);
			}
			return output.split("\n");
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
	
	/**
	 * <h1>Engine.setOutputStream()</h1>
	 * <p>Sets the output stream of the program </p>
	 * @param input : String
	 * @throws IOException
	 * @author David
	 */
	
	public void setOutputStream(String input) {
		try {
			this.output_stream.write(input + '\n');
			this.output_stream.flush();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	@Override
	public void run() {
		whileRunning();
	}
	
	public abstract void whileRunning();
	
	/**
	 * <h1>Engine.isAliveProgram()</h1>
	 * <p> See if the program is alive </p>
	 * @return boolean
	 * @throws IOException
	 * @author David
	 */
	
	
	public boolean isAliveProgram() {
		if (this.p != null) {
			return this.p.isAlive();
		}
		else {
			return false;
		}
	}
}
