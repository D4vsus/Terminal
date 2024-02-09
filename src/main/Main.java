package main;

import logic.CommandManager;
import logic.ProcessManager;
import logic.TerminalManager;

public class Main{
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		TerminalManager terminal = new TerminalManager() {
			
			@Override
			public void addNewCommands() {}
		};
		CommandManager.loadKeys();
		ProcessManager.loadKeys();
	}
}