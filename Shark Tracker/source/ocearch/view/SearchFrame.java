package ocearch.view;

import api.jaws.Jaws;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
/*
 * This class holds the search frame and all of its components.
 */
public class SearchFrame extends JFrame{//This class extends JFrame
	/*Declaring global variables*/
	private Jaws j = new Jaws("s5HxyvUdVjzOi67y", "jU62ba5dgyl9tXI2", false);//Creates a new instance of the API and accesses it
	private JComboBox<String> trackingRange, genderBox, stageofLifeBox, tagLocationBox;//Creating a new combo box holding the search attributes
	private JPanel optionsPane, searchResults, jpCmbBoxes, pic, disclaimer;//Creating panels for this frame
	private BufferedImage sharkImage = null;//Creating a new buffered image file
	private JLabel subTitle, disclaim, trackingRangeLabel, genderLabel, stageOfLifeLabel, tagLocationLabel;//Creating labels for this frame
	private String[] ranges = {"Last 24 hours", "Last Week", "Last Month"};//Creating an array of type string for the tracking range combo box
	private String [] gender = {"All", "Male", "Female"};//Creating an array of type string for the gender combo box
	private String [] stages = {"All", "Mature", "Immature", "Undetermined"};//Creating an array of type string for the stage of life combo box
	private ArrayList<String> locations = j.getTagLocations();//Creating an arraylist of type string to get the locations of the shark
	private JButton searchButton, searchByName;//Creating a search button and a search button for 'surprise me' feature which searches sharks by name
	private JScrollPane scrollbar;//Creating a scrollbar for this frame.

	public SearchFrame(){//The main search frame which calls other methods
		/*Calling the methods intitialiseGUI and buildGUI*/
		intitialiseGUI();
		buildGUI();
	}
	public void intitialiseGUI(){//This method holds information about the GUI. It creates components and sets the layout for them.
		setTitle("Search");//Setting the title for this frame
		setSize(1100, 650);//Setting the size for this frame
	    setLocationRelativeTo(null);//Putting the frame in the centre of the screen

	    optionsPane = new JPanel(new BorderLayout());//Creating a panel for the left hand size of the frame which will hold the options
		jpCmbBoxes = new JPanel();//Creating a panels to hold the combo boxes
		pic = new JPanel();//Creating a panel to hold the picture
		disclaimer = new JPanel(new BorderLayout());//Creating a panel to contain the disclaimer at the bottom of the frame
		searchResults = new JPanel();//Creating a panel to hold the search results on the right hand side of the screen
		scrollbar = new JScrollPane(searchResults, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//Creating a new scroll pane and disabling the horizontal scroll bar
		
		disclaim = new JLabel(j.getAcknowledgement());//Creating a label for the disclaimer
		subTitle = new JLabel("Shark Tracker");//Creating a label for the subtitle
		trackingRangeLabel = new JLabel("Tracking Range");//Creating a label for the tracking range
		genderLabel = new JLabel("Gender");//Creating a label for the gender
		stageOfLifeLabel = new JLabel("Stage of Life");//Creating a label for the stage of life
		tagLocationLabel = new JLabel("Tag Location");//Creating a label for the tag location
		searchButton = new JButton("Search");//Creating a label for the search button
		searchByName = new JButton("Search by Name");//Creating a label for the searchByName button
		
		/*Setting the alignment of each of the components*/
		subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		trackingRangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		genderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);;
		stageOfLifeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		tagLocationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		searchByName.setAlignmentX(Component.CENTER_ALIGNMENT);
		subTitle.setFont(new Font("Serif", Font.PLAIN, 40));//Setting the font for the subtitle
		
		trackingRange = new JComboBox<String>(ranges);//Creating a combo box for the tracking range options
		genderBox = new JComboBox<String>(gender);//Creating a combo box for the gender options
		stageofLifeBox = new JComboBox<String>(stages);//Creating a combo box for the stage of life options
		tagLocationBox = new JComboBox<String>();//Creating a combo box for the tag location options
		tagLocationBox.addItem("All");//Adding an extra field for tag location "all"
		
		for(String s : locations){//This for loop gets the tag locations and then adds them to the combo box
			tagLocationBox.addItem(s);//Adding the location to the combo box
		}
		scrollbar = new JScrollPane(searchResults);//Adding a scroll pane for the search results
	}
	
	public void buildGUI(){//This method builds the GUI. It adds the panels to the frame and sets the layout of each. It also adds the components to the panels

		setLayout(new BorderLayout());//Setting a borderlayout for the frame
		optionsPane.setBorder(BorderFactory.createLineBorder(Color.black));//This sets a border around the options panel
		add(disclaimer, BorderLayout.SOUTH);//Adds the disclaimer panel to the frame
		add(optionsPane, BorderLayout.LINE_START);//Adds the options panel to the frame
		add(scrollbar);//Adds the scrollbar to the frame (contains the search frame)
		optionsPane.add(jpCmbBoxes, BorderLayout.NORTH);//Adds the combo boxes to the options pane
		optionsPane.add(pic, BorderLayout.SOUTH);//Adds the picture to the options pane
		disclaimer.add(disclaim, BorderLayout.LINE_START);//Adds the disclaimer text to the disclaimer panel
		
		/*Sets borders around each of the combo boxes, buttons and the disclaimer text */
		trackingRange.setBorder(new EmptyBorder(10, 10, 10, 10));
		genderBox.setBorder(new EmptyBorder(10, 10, 10, 10));
		stageofLifeBox.setBorder(new EmptyBorder(10, 10, 10, 10));
		tagLocationBox.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		disclaimer.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchByName.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		searchResults.setLayout((new BoxLayout(searchResults, BoxLayout.PAGE_AXIS)));//Setting the layout for search results panel
		searchResults.setBorder(BorderFactory.createLineBorder(Color.black));//Sets a border around the panel
		/*Adding components to the combo boxes panel */
		jpCmbBoxes.setLayout(new BoxLayout(jpCmbBoxes, BoxLayout.Y_AXIS));//Sets the layout for this panel
		jpCmbBoxes.add(subTitle);
		jpCmbBoxes.add(trackingRangeLabel);
		jpCmbBoxes.add(trackingRange);
		jpCmbBoxes.add(genderLabel);
		jpCmbBoxes.add(genderBox);
		jpCmbBoxes.add(stageOfLifeLabel);
		jpCmbBoxes.add(stageofLifeBox);
		jpCmbBoxes.add(tagLocationLabel);
		jpCmbBoxes.add(tagLocationBox);
		jpCmbBoxes.add(searchButton);
		jpCmbBoxes.add(searchByName);
		
		try {//Importing the image for this panel
			sharkImage = ImageIO.read(new File("source/images/shark2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel image = new JLabel(new ImageIcon(sharkImage));//Creating a label and adding the image to the label
		pic.add(image);//adding the label to the panel
	}
	public String returnTrackingRange(){//Get method for the tracking range
		return (String) trackingRange.getSelectedItem();//Return the tracking range options
	}
	
	public String returnGenderBox(){//Get method for the gender
		return (String) genderBox.getSelectedItem();//Return the gender options
	}
	
	public String returnStageOfLife(){//Get method for the stage of life
		return (String) stageofLifeBox.getSelectedItem();//Return the stage of life options
	}
	
	public String returnTagLocation(){//Get method for the tag location
		return (String) tagLocationBox.getSelectedItem();//Return the tag location options
	}
	
	public JPanel getSearchJPanel(){//Get method for the search panel
		return searchResults;//Return the search results
	}
	
	public JButton getSearchButtonButton(){//Get method for the search button
		return searchButton;//Return the search button
	}
	
	public JScrollPane getScrollbar(){//Get method for the scroll bar
		return scrollbar;//Return the scroll bar
	}
	
	public JButton getSearchByNameButton(){
		return searchByName;
	}
	
	public JScrollPane getScrollPane(){
		return scrollbar;
	}

	


}
