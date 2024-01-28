package main;

import java.io.IOException;

import logic.Terminal;

public class Main{
	
	public static void main(String[] args) throws Exception {
		Terminal terminal = new Terminal() {

			@Override
			public void addNewCommands() throws IOException {}
		};
		
		System.out.println(terminal);
	}
}