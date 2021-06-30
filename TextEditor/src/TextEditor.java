import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

//import com.sun.java_cup.internal.runtime.Scanner;

public class TextEditor extends JFrame implements ActionListener {
	
	JTextArea editSpace;                        //to include text area in frame
	JScrollPane scrollPanel;                    //to maintain scrollable view of a large text area
	JSpinner fontSelector;                      //to select font size
	JLabel label;
	JButton fontColor;
	JComboBox fontSpace;
	JMenuBar menuBar;                            //for including menubar
	JMenu menu;                                  
	JMenuItem open,save,exit,nw,pr,cut,copy,paste,undo;  //item of menubar
	UndoManager um=new UndoManager();
	
    TextEditor(){                                               //constructor 
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //for exist
    	this.setTitle("Text Editor");                          // for giving title/heading
    	this.setSize(400,400);                                 //size of text editor
    	this.setLayout(new FlowLayout());                      // for centered layout
    	this.setLocationRelativeTo(null);                      //to align it centered
    	
    	editSpace=new JTextArea();                               //adding text area to frame
    	//editSpace.setPreferredSize(new Dimension(350,350));    //set size of text area
    	editSpace.setLineWrap(true);                          // for line wrapping of text
    	editSpace.setWrapStyleWord(true);                   //for wrapping at boundary of a line
    	Font font = new Font("Calibri",Font.PLAIN,14);         //default font
    	editSpace.setFont(font);
    	
    	scrollPanel=new JScrollPane(editSpace);
    	scrollPanel.setPreferredSize(new Dimension(350,350));
    	scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
    	label=new JLabel("Font Size: ");                   //to give name to spinner
    	
    	fontSelector=new JSpinner();
    	fontSelector.setPreferredSize(new Dimension(55,30));  // to set size of spinner
    	fontSelector.setValue(14);                          // set default size
    	fontSelector.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Font font=new Font(editSpace.getFont().getFamily(),Font.PLAIN,(int) fontSelector.getValue());
				editSpace.setFont(font);
				
			}
    		
    	});
        
    	fontColor=new JButton("Color");
    	fontColor.addActionListener(this);
    	
    	String[] avaiFonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    	
    	fontSpace=new JComboBox(avaiFonts);
    	fontSpace.addActionListener(this);
    	fontSpace.setSelectedItem("Calibri");
    	
    	//------------menubar---------------
    	
    	menuBar=new JMenuBar();
    	menu=new JMenu("File");
    	open=new JMenuItem("Open");
    	save=new JMenuItem("Save");                     //instantiating different items of menubar
    	//exit=new JMenuItem("Exit");
    	nw=new JMenuItem("New");
    	pr=new JMenuItem("Print");
    	
    	open.addActionListener(this);                  //some work has to be done when we click on open
    	save.addActionListener(this);
    	//exit.addActionListener(this);
    	nw.addActionListener(this);
    	pr.addActionListener(this);
    	 menu.add(open);
    	 menu.add(save);
    	 //menu.add(exit);         //adding menuitems to menu and then adding menu to menubar
    	 menu.add(nw);
    	 menu.add(pr);
    	 menuBar.add(menu);
    	 //-----Edit--------//
    	JMenu emenu=new JMenu("Edit");
    	
    	 cut=new JMenuItem("Cut");
    	 copy=new JMenuItem("Copy");
    	 paste=new JMenuItem("Paste");
    	 //undo=new JMenuItem("Undo");
    	 
    	cut.addActionListener(this);
    	copy.addActionListener(this);
    	paste.addActionListener(this);
    	//undo.addActionListener(this);
    	
    	emenu.add(cut);
    	emenu.add(copy);
    	emenu.add(paste);
    	//emenu.add(undo);
    	menuBar.add(emenu);
    	//--------Edit-------//
    	//------------menubar---------------
    	this.setJMenuBar(menuBar);
    	this.add(label);
    	this.add(fontSelector);                                 //add fontSelector to frame;
    	this.add(fontColor);                                     //add color button to frame
    	this.add(fontSpace);
    	this.add(scrollPanel);
    	//this.add(editSpace);                                  //add text area to frame
    	this.setVisible(true);                               // to make it visible
    	
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==fontColor) {
			JColorChooser colorChoose=new JColorChooser();
			
			Color colour=colorChoose.showDialog(null,"Select Color",Color.black);
			
			editSpace.setForeground(colour);
		}
		if(e.getSource()==fontSpace) {            //what has to be done in actionlistener is instructed here
			editSpace.setFont(new Font((String) fontSpace.getSelectedItem(),Font.PLAIN,editSpace.getFont().getSize()));
		}
		if(e.getSource()==open) {
			JFileChooser filechoose=new JFileChooser();
			filechoose.setCurrentDirectory(new File("."));     //default location to open
			FileNameExtensionFilter extension=new FileNameExtensionFilter("Text files","txt");  //to set which type of files you want to open
			filechoose.setFileFilter(extension);
			
			int flag=filechoose.showOpenDialog(null);
			
			if(flag==JFileChooser.APPROVE_OPTION) {
				File file=new File(filechoose.getSelectedFile().getAbsolutePath());
				java.util.Scanner scan=null;
				try {
					scan=new java.util.Scanner(file);
					if(file.isFile()) {
						while(scan.hasNextLine()) {
							String s=scan.nextLine()+"\n";
							editSpace.append(s);
						}
					}
				}
				catch(FileNotFoundException e1) {
					e1.printStackTrace();
				}
				finally {
					scan.close();
				}
			}
		}
        if(e.getSource()==save) {
			JFileChooser filechoose=new JFileChooser();
			filechoose.setCurrentDirectory(new File("."));     //default location where we want to save
			
			int flag=filechoose.showSaveDialog(null);
			
			if(flag==JFileChooser.APPROVE_OPTION) {
				File file;
				PrintWriter fileOut=null;
				
				file=new File(filechoose.getSelectedFile().getAbsolutePath());
				try {
					fileOut=new PrintWriter(file);
					fileOut.println(editSpace.getText());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				finally {
					fileOut.close();
				}
			}
		}
        /*if(e.getSource()==exit) {
	       System.exit(0);
        }*/
        if (e.getSource()==pr) {
            try {
				editSpace.print();
			} catch (PrinterException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(editSpace,e1.getMessage());
			}
        }
        if(e.getSource()==nw) {
        	editSpace.setText("");
        }
        if(e.getSource()==cut) {
        	editSpace.cut();
        }
        if(e.getSource()==copy) {
        	editSpace.copy();
        }
        if(e.getSource()==paste) {
        	editSpace.paste();
        }
	}

}
