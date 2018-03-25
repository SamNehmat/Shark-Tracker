package ocearch.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ocearch.model.User;
import ocearch.view.TitleScreen;
/*
 * This class controls the code behind the user frame
 */
public class UserController {
	//Declaring variables
	private ArrayList<User> userList;
	private ArrayList<String> savedUserNames;
	private TitleScreen ts;
	private String userNameInput;
	private User uTemp;
	private BufferedReader br;
	private File userlistCSV = new File("source/data/userlist.csv");
	private User guest = new User("Guest");
	
	public UserController(TitleScreen ts){//This method reads the CSV file, and if none exists, creates a default profile
		this.ts = ts;//Refferring to the current instance of the title screen
		userList = new ArrayList<User>();//Creating a new userlist arraylist
		setCNUListener();//Calling the method setCNUListener
		try {
			readCSV();//Reading the CSV file
		} catch (IOException e) {
			System.out.println("Cannot read CSV file");//If no file exists, then print out that it cant read it
			e.printStackTrace();
		}
		if(savedUserNames.contains("Guest")){}else{userList.add(guest);};//If the combo box option "Guest" is selected, create a guest user
		RefreshUserList();//Refreshing the user list
	}
	
	public ArrayList<User> getUserList(){//get method for the user list
		return userList;//returning the user list
	}
	
	public User getUser(String userName){//get method for the user names
		for(User u : userList){//for loop to loop around as many times as users exist
			if(u.getName().equals(userName)){uTemp = u;};
		}
		return uTemp;//Returning the user
	}
	
	public void RefreshUserList(){//This method refreshes the user list
		if(userList.isEmpty() == false){ts.getComboBox().removeAllItems();//Removing all items from the user list if it is not empty already.
											for(User u : userList){//Adding all of the names again to the combo box
												ts.getComboBox().addItem(u.getName());
										}
		/*Refreshing the panel*/
		ts.repaint();
		ts.revalidate();}
		
	}
	public void setCNUListener(){
			ts.getCreateButton().addActionListener(new ActionListener(){//Adding an action listener to the create new user button
				@Override
				public void actionPerformed(ActionEvent e) {
					userNameInput = JOptionPane.showInputDialog("Enter Your New Username:");//Creating a dialog box where the user enters their details
				
							if(userNameInput != null){userList.add(new User(userNameInput));}//if there is a user input, add it to the userNameInput string
							try {//Writing the user name input to the CSV
								writeToCSV();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						    RefreshUserList();//Calling the method RefreshUserList
						}
					});
			}
	
	public void readCSV() throws IOException{//This method reads the CSV file
		savedUserNames = new ArrayList<String>();//Creating a new arraylist
		String csvFile = "userlist.csv";//Defining the file location
		String cvsSplitBy = (",");//Defining the delimiter
		br = new BufferedReader(new FileReader(csvFile));//Creating a new file reader
		String line = "";
		while ((line = br.readLine()) != null){//Keep looping as long as the read line is not empty
			String[] cols = line.split(cvsSplitBy);//Defining what each value is split by.
			savedUserNames.add(cols[0]);//adding them to the arraylist
			}		
		for(String s : savedUserNames){//Using a for loop to go around all of the saved user names
			userList.add(new User(s));//Adding a user to the user object
			}

		}
	
	//Writing a user to the CSV file
	public void writeToCSV() throws FileNotFoundException{
		userlistCSV = new File("source/data/userlist.csv");//Creating a new CSV file
		PrintWriter  writer = new PrintWriter ("source/data/userlist.csv");//Creatiner a new Printer Writer
		StringBuilder sb = new StringBuilder();//Creating a string builder
			for(User u : userList){//Using a string builder to append to the CSV file
				sb.append(u.getName());//Adding the name to the file
				sb.append(",");//Adding the delimiter to the file
				sb.append('\n');
			}
			writer.write(sb.toString());//Write the values to the file
			writer.close();//Close the writer
		}
	
	

}