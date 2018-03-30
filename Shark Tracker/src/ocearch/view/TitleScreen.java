package ocearch.view;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/*
 * This class holds the panels and components for the title screen. This is the
 * screen in which is displayed upon first opening the program.
 */
public class TitleScreen extends JFrame {
	//Declaring variables
	private JPanel main, buttons, pic, user, top;
	private JButton searchButton, favouritesButton, createNewUser;
	private JLabel title;
	private BufferedImage sharkImage = null;
	private JComboBox<String> users;

	public TitleScreen(){//This is the main method for this class, it simply calls other methods
		/*Calling methods intitialiseGUI and buildGUI*/
		intitialiseGUI();
		buildGUI();
	}
		
	public void intitialiseGUI(){//This method creates the panels and components
		/*Creating new JPanels*/
		main = new JPanel(new BorderLayout());
		pic = new JPanel();
		buttons = new JPanel();
		user = new JPanel();
		top = new JPanel();

		searchButton = new JButton("Search");//Creating the search button
		favouritesButton = new JButton("Favourites");//Creating the favourites button
		
		users = new JComboBox<String>();//Creating the users combo box
		createNewUser = new JButton("Create New User");//Creating the users button
		
		title = new JLabel("Shark Tracker", SwingConstants.CENTER);//Creating the title label and setting the name for it
	}
	
	public void buildGUI(){//This method sets the layout for the GUI and adds components to the frame
		
		setLayout(new BorderLayout());//Sets the layout for the frame
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));//Sets the layout for the buttons panel
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));//Sets the layout for the title label and the user buttons/combo box
		
		add(main);//adds the outer panel to the frame
		main.add(buttons, (BorderLayout.SOUTH));//adds the buttons panel to the outer panel
		main.add(pic, BorderLayout.CENTER);//adds the picture panel to the outer panel
		main.add(top, BorderLayout.NORTH);//adds the label and user panel to the outer panel
		top.add(title, BorderLayout.NORTH);//adding the title label to the top panel
		top.add(user, BorderLayout.SOUTH);//adding the user panel to the top panel
		user.add(users);//adds the user combo box to the user panel
		user.add(createNewUser);//adds the user button to the user panel
		buttons.add(searchButton);//adds the search button to the button panel
		buttons.add(favouritesButton);//adds the favourites button to the buttons panel
		searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);//setting the alignment for the search button
		favouritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);//setting the alignment for the favourites button
		/*Loading the shark image into the panel*/
		try {
			sharkImage = ImageIO.read(new File("source/images/shark1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel image = new JLabel(new ImageIcon(sharkImage));//adding the shark image to a label
		main.add(image, BorderLayout.CENTER);//adding the label to the outer panel
		title.setFont(new Font("Serif", Font.PLAIN, 70));//Setting the font for the title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//setting the default close operation to exit
		setSize(700,700);//setting the size of the frame
		setTitle("Shark Tracker");//setting the title of the frame
	}
	public JButton getFavouritesButton(){//Get method for the favourites button
		return favouritesButton;//returning the favourites buttton
	}
	public JButton getSearchButton(){//Get method for the search button
		return searchButton;//returning the search button
	}
	public JButton getCreateButton(){//Get method for the create new user button
		return createNewUser;//returning the create new user button
	}
	public JComboBox<String> getComboBox(){//Get method for the combo box
		return users;//Retuning the users combo box
	}
	

	
	
	
}

	



	


