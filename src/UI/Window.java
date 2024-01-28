package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * <h1> Window </h1>
 * <p> Run the window of the terminal </p>
 * @throws Exception
 * @author David
 */
public abstract class Window extends JFrame implements ActionListener{
	
	//Variables and objects
	private static final long serialVersionUID = 1L;
	private JMenuBar MenuBar;
	private JMenu[] MenuBottons;
	private JPanel InputPanel;
	private JTextArea Output;
	private JTextField InputTextFile;
	private JButton BottonEnter;
	private JScrollPane ScrollPane;
	
	//Methods
	
	/**
	 * <h1> Window.Window() </h1>
	 * <p> Initialize the window, here variables and the objects</p>
	 * @param title : String
	 * @param wide : integer
	 * @param height : integer
	 * @param terminal : Terminal
	 * @author David
	 */
	
	public Window(String title,int wide,int height){
		//prepare main window
		this.setTitle(title);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(wide,height);
	    this.setLocationRelativeTo(null);
	      
	    add_menu_bar(2);
	    add_input_panel();
	    add_output_panel();
	    setFontOfTheWindow("Serif",22);
	     //start window
	    
	    this.setColorOfInput(0x00665E, 0x00665E, 0x00665E);
	    this.setColorOfFont(0xFFFFFF);
	    this.setColorOfOutput(0x00665E, 0x00665E);
	    this.setColorOfMenu(0xCEDC00,0x000000);
	    this.setVisible(true);
	}
	
	/**
	 * <h1> Window.add_menu_bar() </h1>
	 * <p> Add the menu bar to the window</p>
	 * @param number_of_bottons : integer
	 * @author David
	 */
	
	private void add_menu_bar(int number_of_bottons) {
		this.MenuBar = new JMenuBar();
	   	this.MenuBottons = new JMenu[number_of_bottons];
	    this.MenuBottons[0] = new JMenu("Options");
	    this.MenuBottons[1] = new JMenu("Help");
	    this.MenuBar.add(this.MenuBottons[0]);
	    this.MenuBar.add(this.MenuBottons[1] );
	    this.getContentPane().add(BorderLayout.NORTH,MenuBar);
	}
	
	/**
	 * <h1> Window.add_input_panel() </h1>
	 * <p> Add the input panel to the window</p>
	 * @author David
	 */
	
	private void add_input_panel() {
        this.InputPanel = new JPanel();
        this.InputTextFile = new JTextField(100); 
        this.InputTextFile.addActionListener(this);
        this.BottonEnter = new JButton("Enter");
        this.BottonEnter.addActionListener(this);
        this.InputPanel.add(InputTextFile);
        this.InputPanel.add(BottonEnter);
        this.getContentPane().add(BorderLayout.SOUTH,InputPanel);
	}
	
	/**
	 * <h1> Window.add_output_panel() </h1>
	 * <p> Add the output panel to the window</p>
	 * @author David
	 */
	
	private void add_output_panel() {
		this.Output = new JTextArea(100, 100);
		this.Output.setEditable(false);
		this.ScrollPane=new JScrollPane(this.Output);
		this.getContentPane().add(this.ScrollPane);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//IOfunctions//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <h1> Window.print() </h1>
	 * <p> Print a string into the terminal</p>
	 * @param output: String
	 * @author David
	 */
	
	public void print(String output)  {
		try {
			this.Output.append(output);
			System.out.append(output);
		} catch(Exception e){
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1> Window.print() </h1>
	 * <p> Print a string into the terminal and add an end of line</p>
	 * @param output : String
	 * @author David
	 */
	
	public void println(String output)  {
		try {
			this.Output.append(output +'\n');
			System.out.append(output+ '\n');
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * <h1> Window.printAll() </h1>
	 * <p> Print an array of strings into the terminal <br/></p>
	 * <p> and add an end of line in each one </p>
	 * @param output : String[]
	 * @author David
	 */
	
	public void printAll(String[] output)  {
		for(String line:output) {
			println(line);
		}
	}
	
	/**
	 * <h1> Window.clear() </h1>
	 * <p> Clear the terminal output</p>
	 * @author David
	 */
	
	public void clear() {
		this.Output.setText("");
		System.out.flush();
	}
	
	
	
	/**
	 * <h1> Window.read() </h1>
	 * <p> Change the font, type and size of the window </p>
	 * @return The input text box : String
	 * @author David
	 */
	
	public String read() {
		return this.InputTextFile.getText();
	}
	
	/**
	 * <h1> Window.actionPerformed() </h1>
	 * <p> Loads all the actions events </p>
	 * @param e : ActionEvent
	 * @author David
	 */
	public void actionPerformed(ActionEvent e) {
	        if (e.getSource()==BottonEnter || e.getSource()==InputTextFile) {
	           try {
	        	   
				println(read());
				enterAcction();
				
	           } catch (Exception e1) {
				 e1.printStackTrace();
	           }
	           
	           this.InputTextFile.setText("");
	        }
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Style///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * <h1> Window.enterAcction() </h1>
	 * <p> Run all commands when pressing enter </p>
	 * @author David 
	 */
	public abstract void enterAcction();
	
	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font of the window </p>
	 * @param font : String
	 * @author David
	 */
	
	public void setFontOfTheWindow(String font) {
		this.Output.setFont(new Font(font, Font.PLAIN,this.Output.getFont().getSize()));
		this.InputTextFile.setFont(new Font(font, Font.PLAIN,this.Output.getFont().getSize()));
	}
	
	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font and size of the window </p>
	 * @param font : String
	 * @param size : integer
	 * @author David
	 */
	
	public void setFontOfTheWindow(String font,int size) {
		this.Output.setFont(new Font(font, Font.PLAIN,size));
		this.InputTextFile.setFont(new Font(font, Font.PLAIN,size));

	}
	
	
	
	/**
	 * <h1> Window.setFontOfTheWindow() </h1>
	 * <p> Change the font, type and size of the window </p>
	 * @param font : String
	 * @param type : integer
	 * @param size : integer
	 * @author David
	 */
	
	public void setFontOfTheWindow(String font,int type,int size) {
		this.Output.setFont(new Font(font, type,size));
		this.InputTextFile.setFont(new Font(font, type,size));
	}
	
	/**
	 * <h1> Window.setColorOfFont() </h1>
	 * <p> Change the color of the window </p>
	 * @param RGB : integer(hexadecimal)
	 * @author David
	 */
	
	public void setColorOfFont(int RGB) {
		this.Output.setForeground(new Color(RGB));
		this.InputTextFile.setForeground(new Color(RGB));
		this.InputTextFile.setCaretColor(new Color(RGB));
		this.BottonEnter.setForeground(new Color(RGB));
		this.MenuBar.setForeground(new Color(RGB));
	}
	
	/**
	 * <h1> Window.setColorOfInput() </h1>
	 * <p> Change the color of the Input Panel </p>
	 * @param RGBPanel : integer(hexadecimal)
	 * @param RGBInputTextFile : integer(hexadecimal)
	 * @param RGBBottonEnter : integer(hexadecimal)
	 * @author David
	 */
	
	public void setColorOfInput(int RGBPanel,int RGBInputTextFile,int RGBBottonEnter) {
		this.InputPanel.setBackground(new Color(RGBPanel));
		this.InputTextFile.setBackground(new Color(RGBInputTextFile+0x002710));
		this.BottonEnter.setBackground(new Color(RGBInputTextFile+0x002710));
	}
	
	/**
	 * <h1> Window.setColorOfOutput() </h1>
	 * <p> Change the color of the Output Panel </p>
	 * @param RGBOutput : integer(hexadecimal)
	 * @param RGBScrollPane : integer(hexadecimal)
	 * @author David
	 */
	
	public void setColorOfOutput(int RGBOutput,int RGBScrollPane) {
		this.Output.setBackground(new Color(RGBOutput));
		this.ScrollPane.setBackground(new Color(RGBScrollPane));
	}
	
	/**
	 * <h1> Window.setColorOfMenu() </h1>
	 * <p> Change the color of the Menu Panel </p>
	 * @param RGBMenu : integer(hexadecimal)
	 * @author David
	 */
	
	public void setColorOfMenu(int RGBMenu,int RGBBorder) {
		this.MenuBar.setBackground(new Color(RGBMenu));
	}

}