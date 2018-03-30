package ocearch.view;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/*
 * This class creates the favourites frame and adds components to it. The code behind each component is within
 * the favourites controller.
 */
public class Favourites extends JFrame {
	//Declaring variables
	private JPanel jpFav;
	private JScrollPane jpScroll;
	
	public Favourites(){//This is the main method for the favourites class, it simply calls other methods
		initaliseGUI();//Calling the initaliseGUI method
		BuildGUI();//Calling the BuildGUI method
	}
	public void initaliseGUI(){//This method sets the size of the frame and adds panels to it
		setSize(900,300);//Setting the size of the frame
	    setLocationRelativeTo(null);//Setting the location of the frame to the middle of the screen
	    jpFav = new JPanel();//Creating a new panel
	    jpScroll = new JScrollPane(jpFav);//Creating a new scroll pane
	}
	public void BuildGUI(){;//This method sets the layout for the GUI and adds the scroll pane to the frame
		jpFav.setLayout(new BoxLayout(jpFav, BoxLayout.Y_AXIS));//This sets the layout of the panel
		add(jpScroll);//Adds the scroll pane to the frame
	}
	public JPanel getFavePanel(){//get method for the favourites panel
		return jpFav;//Returning the favourites panel
	}
	

	
}

