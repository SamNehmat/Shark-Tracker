package ocearch.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import api.jaws.Shark;
/*
 * This class extends a JPanel in which is then used in order to hold information about
 * each of the sharks. It also holds the layout and design for the follow and unfollow panels within the search frame.
 */

public class SharkInfo extends JPanel{//Extending JPanel for the class
	//Declaring variables
	private String name, gender, stageOfLife, species, description, length, weight, lastPing;
	private JButton favourite, unfavourite;
	private JLabel infoLabel;
	private Shark s;
	private Dimension labelSize, buttonSize;


	public SharkInfo(Shark s){//This is the main method for this class, it simply initialises variables and calls other methods
		/*Initialising variables*/
		this.s = s;//Referring to the current instance of the shark
		name = s.getName();//Using the getName method in order to obtain the name of the shark
		gender = s.getGender();//Using the getGender method in order to obtain the gender of the shark
		stageOfLife = s.getStageOfLife();//Using the getStageOfLife method in order to obtain the stage of life for the shark
		species = s.getSpecies();//Using the getSpecies method in order to obtain the species of the shark
		length = s.getLength();//Using the getLength method in order to obtain the length of the shark
		weight = s.getWeight();//Using the getWeight method in order to obtain the weight of the shark
		description = s.getDescription();//Using the getDescriptiozn method in order to obtain the description of the shark
		initialiseGUI();//Calling method initialiseGUI
		buildUnfollowedGUI(); //fix this
	}

	public void initialiseGUI(){//This method creates buttons/labels and sets the positioning and size of each
		
		setLayout(new BorderLayout());//Setting the layout to BorderLayout for this panel
		labelSize = new Dimension(300,250);//Defining the size for the labels
		buttonSize = new Dimension(25,25);//Defining the size for the buttons
		favourite = new JButton("Follow");//Creating a Follow button
		unfavourite = new JButton("Following");//Creating a Following button
		
		/* Creating a labels to hold the information about the shark and have each new field on a new line*/
		infoLabel = new JLabel("<html><body>Name: "+name+""
				+ "<br>Gender: "+gender+""
						+ "<br>Stage of Life: "+stageOfLife+""
								+ "<br>Species: "+species+""
										+ "<br>Length: "+length+""
												+ "<br>Weight: " +weight+""
													+ "<br><br> Last Ping: "
															+ "<br><br>Description:"
																+ "<br>"+description);
			}
	
	public void buildUnfollowedGUI(){//This method sets the sizes and borders for the unfollowed panel components
		add(infoLabel, BorderLayout.CENTER);//Adding the label to the panel with a center border layout
		infoLabel.setMinimumSize(labelSize);//Setting the minimum size for the label
		infoLabel.setPreferredSize(labelSize);//Setting the preferred size for the label
		infoLabel.setMaximumSize(labelSize);//Setting the maximum size for the label
		
		add(favourite, BorderLayout.PAGE_END);//Adding the button to the panel with a page end layout
		favourite.setMinimumSize(buttonSize);//Setting the minimum size for the button
		favourite.setPreferredSize(buttonSize);//Setting the preferred size for the button
		favourite.setMaximumSize(buttonSize);//Setting the maximum size for the button
		
		setBorder(BorderFactory.createLineBorder(Color.black));//Setting a border for the panel
		setBorder(new EmptyBorder(20, 20, 20, 20));//Defining the size for this border
		}
	
	public void buildFollowedGUI(){//This method sets the sizes and borders for the followed panel components
		add(infoLabel, BorderLayout.CENTER);//Adding the label to the panel with a center border layout
		infoLabel.setMinimumSize(labelSize);//Setting the minimum size for the label
		infoLabel.setPreferredSize(labelSize);//Setting the preferred size for the label
		infoLabel.setMaximumSize(labelSize);//Setting the maximum size for the label
		
		add(unfavourite, BorderLayout.PAGE_END);//Adding the button to the panel with a page end layout
		unfavourite.setMinimumSize(buttonSize);//Setting the minimum size for the button
		unfavourite.setPreferredSize(buttonSize);//Setting the preferred size for the button
		unfavourite.setMaximumSize(buttonSize);//Setting the maximum size for the button
		
		setBorder(BorderFactory.createLineBorder(Color.black));//Setting a border for the panel
		setBorder(new EmptyBorder(20, 20, 20, 20));//Defining the size for this border
		
		}
	
	public JButton getFollowButton(){//creating a get method for the follow button
		return favourite;//Returning the follow button
	}
	public JButton getUnfollowButton(){//Creating a get method for the unfollow button
		return unfavourite;//returning the unfollow button
	}
	public Shark getShark(){//Creating a get method for the shark
		return s;//Returning the shark
	}
}

