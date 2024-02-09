package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * <h1> Window </h1>
 * <p> Run the window of the terminal </p>
 * @throws Exception
 * @author D4vsus
 */

public abstract class Window extends JFrame implements ActionListener,KeyListener{
	
	//Variables and objects
	private static final long serialVersionUID = 1L;
	private JPanel inputPanel;
	private JTextArea outputArea;
	private JTextField inputTextFile;
	private JButton bottonEnter;
	private JScrollPane scrollPane;
	
	//Methods
	
	/**
	 * <h1> Window.Window() </h1>
	 * <p> Initialize the window, here variables and the objects</p>
	 * @param title : String
	 * @param wide : integer
	 * @param height : integer
	 * @param terminal : Terminal
	 * @author D4vsus
	 */
	
	public Window(String title){
		//prepare main window
		this.setTitle(title);
		ImageIcon icon = new ImageIcon("resources\\GDrag.png");
		this.setIconImage(icon.getImage());
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      
	    //add_menu_bar(2);
	    add_input_panel();
	    add_output_panel();
	    setFontOfTheWindow(Font.MONOSPACED,18);
	    //the fast green
	    //this.setColorOfInput(0x00665E, 0x00665E, 0x00665E);
	    //this.setColorOfFont(0xFFFFFF);
	    //this.setColorOfOutput(0x00665E, 0x00665E);
	    //this.setColorOfMenu(0xCEDC00,0x000000);
	    //the Golden Dragon
	    this.setColorOfInput(0xffdf00, 0xffff55, 0xffff55);
	    this.setColorOfFont(0x000000);
	    this.setColorOfOutput(0xffc600, 0xffc600);
	     //start window
	    
	    this.pack();
	    this.setLocationRelativeTo(null);
	    this.setVisible(true);
	}
	
	/**
	 * <h1> Window.add_input_panel() </h1>
	 * <p> Add the input panel to the window</p>
	 * @author D4vsus
	 */
	
	private void add_input_panel() {
        this.inputPanel = new JPanel();
        this.inputTextFile = new JTextField(75); 
        this.inputTextFile.addActionListener(this);
        this.inputTextFile.addKeyListener(this);
        this.bottonEnter = new JButton("Enter");
        this.bottonEnter.addActionListener(this);
        this.inputPanel.add(inputTextFile,BorderLayout.EAST);
        this.inputPanel.add(bottonEnter,BorderLayout.EAST);
        this.getContentPane().add(BorderLayout.SOUTH,inputPanel);
	}
	
	/**
	 * <h1> Window.add_output_panel() </h1>
	 * <p> Add the output panel to the window</p>
	 * @author D4vsus
	 */
	
	private void add_output_panel() {
		this.outputArea = new JTextArea(25, 30);
		this.outputArea.setEditable(false);
		this.outputArea.addKeyListener(this);
		this.scrollPane=new JScrollPane(this.outputArea);
		this.getContentPane().add(this.scrollPane);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//IOfunctions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <h1> Window.print() </h1>
	 * <p> Print a string into the terminal</p>
	 * @param output: String
	 * @author D4vsus
	 */
	
	public void print(String output)  {
		try {
			if (this.outputArea.getLineCount() > 512) {
				this.outputArea.setText(this.outputArea.getText(1, this.outputArea.getLineCount()));
			}
			this.outputArea.append(output);
			System.out.append(output);
		} catch(Exception e){
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1> Window.print() </h1>
	 * <p> Print a string into the terminal and add an end of line</p>
	 * @param output : String
	 * @author D4vsus
	 */
	
	public void println(String output)  {
		this.print(output +'\n');
	}
	
	/**
	 * <h1> Window.printAll() </h1>
	 * <p> Print an array of strings into the terminal <br/></p>
	 * <p> and add an end of line in each one </p>
	 * @param output : String[]
	 * @author D4vsus
	 */
	
	public void printAll(String[] output)  {
		StringBuffer str = new StringBuffer();
		for(String line:output) {
			str.append(line);
		}
		println(str.toString());
	}
	
	/**
	 * <h1> Window.clear() </h1>
	 * <p> Clear the terminal output</p>
	 * @author D4vsus
	 */
	
	public void clear() {
		this.outputArea.setText(null);
		System.out.flush();
		System.gc();
	}
	
	/**
	 * <h1> Window.read() </h1>
	 * <p> Change the font, type and size of the window </p>
	 * @return The input text box : String
	 * @author D4vsus
	 */
	
	public String read() {
		return this.inputTextFile.getText();
	}
	
	/**
	 * <h1> Window.actionPerformed() </h1>
	 * <p> Loads all the actions events </p>
	 * @param e : ActionEvent
	 * @author D4vsus
	 */
	
	public void actionPerformed(ActionEvent e) {
	        if (e.getSource()==bottonEnter || e.getSource()==inputTextFile) {
	           try {
	        	   
				println(read());
				enterAcction();
				
	           } catch (Exception e1) {
				 e1.printStackTrace();
	           }
	           
	           this.inputTextFile.setText(null);
	        }
	        if(e.getSource() == this) {
	        	
	        }
	}
	
	/**
	 * <h1> Window.setInputText() </h1>
	 * <p> Sets a text on the input text bar </p>
	 * @author D4vsus 
	 */
	
	public void setInputText(String text) {
		this.inputTextFile.setText(text);
	}
	
	/**
	 * <h1> Window.enterAcction() </h1>
	 * <p> Run all commands when pressing enter </p>
	 * @author D4vsus 
	 */
	
	public abstract void enterAcction();
	
	/**
	 * <h1> Window.keyTyped() </h1>
	 * <p> Nothing for know </p>
	 * @author D4vsus
	 * @Override
	 */
	
	public void keyTyped(KeyEvent e) {

	}
	
	/**
	 * <h1> Window.keyReleased() </h1>
	 * <p> Nothing for know </p>
	 * @author D4vsus
	 * @Override
	 */
	
	public void keyReleased(KeyEvent e) {
	}
	
	/**
	 * <h1> Window.keyPressed() </h1>
	 * <p> Manage key pressing </p>
	 * @author D4vsus
	 * @param KeyEvent : pressed
	 * @Override
	 */
	
	public void keyPressed(KeyEvent e) {
	    switch( e.getKeyCode() ) { 
        case KeyEvent.VK_UP:
        	this.setkeyUp();
            break;
        case KeyEvent.VK_DOWN:
        	this.setkeyDown();
            break;
        case KeyEvent.VK_LEFT:
            // handle left
            break;
        case KeyEvent.VK_RIGHT :
            // handle right
            break;
        case KeyEvent.VK_Z + ActionEvent.CTRL_MASK :
        	break;
	    }
		if((e.getKeyCode() == KeyEvent.VK_Z && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0))) {
			this.setControlZ();
		}
	}
	
	/**
	 * <h1> Window.keyDownMethod() </h1>
	 * <p> Sets an action when pressing the arrow down </p>
	 * @author D4vsus
	 * @Override
	 */
	
	public abstract void setkeyDown();
	
	/**
	 * <h1> Window.setControlZ() </h1>
	 * <p> Sets an action when pressing the ctrl + z </p>
	 * @author D4vsus
	 * @Override
	 */
	
	public abstract void setControlZ();
	
	/**
	 * <h1> Window.setkeyUp() </h1>
	 * <p> Sets an action when pressing the arrow up </p>
	 * @author D4vsus
	 * @Override
	 */
	
	public abstract void setkeyUp();
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Style///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font of the window </p>
	 * @param font : String
	 * @author D4vsus
	 */
	
	public void setFontOfTheWindow(String font) {
		this.outputArea.setFont(new Font(font, Font.PLAIN,this.outputArea.getFont().getSize()));
		this.inputTextFile.setFont(new Font(font, Font.PLAIN,this.outputArea.getFont().getSize()));
	}
	
	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font and size of the window </p>
	 * @param font : String
	 * @param size : integer
	 * @author D4vsus
	 */
	
	public void setFontOfTheWindow(String font,int size) {
		this.outputArea.setFont(new Font(font, Font.PLAIN,size));
		this.inputTextFile.setFont(new Font(font, Font.PLAIN,size));
	}
	
	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font, type and size of the window </p>
	 * @param font : String
	 * @param type : integer
	 * @param size : integer
	 * @author D4vsus
	 */
	
	public void setFontOfTheWindow(String font,int type,int size) {
		this.outputArea.setFont(new Font(font, type,size));
		this.inputTextFile.setFont(new Font(font, type,size));
	}
	
	/**
	 * <h1> Window.getFontOfTheWindow() </h1>
	 * <p> Get the font of the window </p>
	 * @return String : the font of the window
	 * @author D4vsus 
	 */
	
	public String getFontOfTheWindow() {
		return this.outputArea.getFont().toString();
	}
	
	/**
	 * <h1> Window.setColorOfFont() </h1>
	 * <p> Change the color of the window </p>
	 * @param RGB : integer(hexadecimal)
	 * @author D4vsus
	 */
	
	public void setColorOfFont(int RGB) {
		this.outputArea.setForeground(new Color(RGB));
		this.inputTextFile.setForeground(new Color(RGB));
		this.inputTextFile.setCaretColor(new Color(RGB));
		this.bottonEnter.setForeground(new Color(RGB));
		//this.MenuBar.setForeground(new Color(RGB));
	}
	
	/**
	 * <h1> Window.setColorOfInput() </h1>
	 * <p> Change the color of the Input Panel </p>
	 * @param RGBPanel : integer(hexadecimal)
	 * @param RGBInputTextFile : integer(hexadecimal)
	 * @param RGBBottonEnter : integer(hexadecimal)
	 * @author D4vsus
	 */
	
	public void setColorOfInput(int RGBPanel,int RGBInputTextFile,int RGBBottonEnter) {
		this.inputPanel.setBackground(new Color(RGBPanel));
		this.inputTextFile.setBackground(new Color(RGBInputTextFile));
		this.bottonEnter.setBackground(new Color(RGBInputTextFile));
	}
	
	/**
	 * <h1> Window.setColorOfOutput() </h1>
	 * <p> Change the color of the Output Panel </p>
	 * @param RGBOutput : integer(hexadecimal)
	 * @param RGBScrollPane : integer(hexadecimal)
	 * @author D4vsus
	 */
	
	public void setColorOfOutput(int RGBOutput,int RGBScrollPane) {
		this.outputArea.setBackground(new Color(RGBOutput));
		this.scrollPane.setBackground(new Color(RGBScrollPane));
	}
}