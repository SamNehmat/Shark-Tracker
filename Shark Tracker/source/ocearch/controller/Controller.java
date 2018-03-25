package ocearch.controller;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import api.jaws.Jaws;
import api.jaws.Shark;
import ocearch.view.SearchFrame;
import ocearch.view.SharkInfo;
import ocearch.view.TitleScreen;
/*
 * This class contains the code for the listeners, and is the code behind the GUI in the title screen and the search frame.
 */
public class Controller {
	//Initialising variables
	private Jaws j = new Jaws("s5HxyvUdVjzOi67y", "jU62ba5dgyl9tXI2", false);
	private ArrayList<String> sharkNameList;
	private ArrayList<Shark> tempSearchList1, tempSearchList2, sharkSearchResults;
	private ArrayList<Shark> sharkList;
	private SearchFrame sf;
	private FavouritesController fc;
	private SharkInfo tempSI;
	private String selectedTrackingRange, selectedGenderBox, selectedStageOfLife, selectedTagLocation, nameSearch, searchedSharkName;
	private JOptionPane confirmation, error;
	private JDialog dialog;
	private TitleScreen ts;
	private JScrollPane scrollbar;

	/**
	 * 
	 * @param sf
	 * @param fc
	 * @param ts
	 */
	public Controller(SearchFrame sf, FavouritesController fc, TitleScreen ts){		
		this.sf = sf;//Referring to the current instance of the search frame
		this.fc = fc;//Referring to the current instance of the favourites controller
		this.ts = ts;//Referring to the current instance of the title screen
		
		sharkNameList = j.getSharkNames();//Obtaining shark names through the API
		sharkList = new ArrayList<Shark>();//Creating a shark arraylist
		setSharkList();//Method to obtain the sharks from the API and add them to the arraylist
		setSearchFrameListeners();//Calling the listener method for the search frame
		setTitleScreenListeners();//Calling the listener method for the title screen
		}
	
	/**
	 * queries the API
	 * @return API
	 */
	public Jaws queryAPI(){ 
		return j;//Returning Jaws
	}
		
	/**
	 * obtains the sharks from the API and add them to the arraylist
	 */
	public void setSharkList(){

		for(String s : sharkNameList){//Creating a for loop in order to go through each shark from the API
			sharkList.add(j.getShark(s));//Adding each shark to the arraylist
		}
	}
	
	/**
	 * returning the number of sharks
	 * @return int sharklist size
	 */
	public int numberOfSharks(){//Get method for 
		return sharkList.size();//Returning the number of sharks
	}
	
	
	/**
	 * obtains the jcombobox inputs
	 */
	public void setSearchAttributes(){
		selectedTrackingRange = sf.returnTrackingRange();//returning the tracking range and placing it into the variable
		selectedGenderBox = sf.returnGenderBox();//returning the gender and placing it into the variable
		selectedStageOfLife = sf.returnStageOfLife();//returning the stage of life and placing it into the variable
		selectedTagLocation = sf.returnTagLocation();//returning the location and placing it into the variable
	}
	
	/**
	 * finds the search results based off the user inputs
	 */
	public void search(){
		//Creating new arraylists
		tempSearchList1 = new ArrayList<Shark>();
		tempSearchList2 = new ArrayList<Shark>();
		sharkSearchResults = new ArrayList<Shark>(); 
		
		for(Shark s1 : sharkList){//for loop to search for sharks based of the input
			if((s1.getGender()).equals(selectedGenderBox) || selectedGenderBox.equals("All")){//obtaining list of sharks from the selected gender (obtained from user input)
				tempSearchList1.add(s1);//Add results
			}
		}
		for(Shark s2 : tempSearchList1){
			if(s2.getStageOfLife().equals(selectedStageOfLife) || selectedStageOfLife.equals("All")){//obtaining list of sharks from the selected stage of life (obtained from user input)
				tempSearchList2.add(s2);//Add results
			}
		}	
		for(Shark s3 : tempSearchList2){
			if(s3.getTagLocation().equals(selectedTagLocation) || selectedTagLocation.equals("All")){//obtaining list of sharks from the selected tag location(obtained from user input)
				sharkSearchResults.add(s3);//Add results
			}
		}	
	}
	/**
	 * clears the search panel
	 */
	public void clearSearch(){
		tempSearchList1 = null;
		sharkSearchResults = null;
	}
	/**
	 * Adds listeners for the title screen
	 */
	public void setTitleScreenListeners(){
		if(checkFavouriteList() == true){ts.getFavouritesButton().setEnabled(false);}else{ts.getFavouritesButton().setEnabled(true);}
		
		ts.getSearchButton().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sf.setVisible(true);//When frame loads, make it visible
			}});
	}
	/**
	 * Sets action listeners for the shark info
	 * @param SI
	 */
	public void setSharkInfoListeners(SharkInfo SI){
		SI.getFollowButton().addActionListener(new ActionListener(){//adding a action listener to the follow button
			public void actionPerformed(ActionEvent arg0) {
				fc.addToFaves(SI.getShark());//adding the shark to the favourites
				confirmation = new JOptionPane(SI.getShark().getName()+" has been added to your favourites",JOptionPane.PLAIN_MESSAGE);//message box to confirm it has been added
				dialog = confirmation.createDialog("Added to Favourites!");
				/*Setting the preferences for the dialog box*/
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				SI.remove(SI.getFollowButton());//Removing the follow button
				SI.add(SI.getUnfollowButton(), BorderLayout.PAGE_END);//Placing the following button
				/* refreshing the panel */
				SI.repaint();
				SI.validate();
				/*If the favourites list .isempty returns false (favourites exist) then enable the favourites button. otherwise disable the favourites button*/
				if(checkFavouriteList() == false){ts.getFavouritesButton().setEnabled(true);}else{ts.getFavouritesButton().setEnabled(false);};
				//refreshing the title screen.
				ts.repaint();
				ts.validate();
				}	
				});
		
		SI.getUnfollowButton().addActionListener(new ActionListener(){//adding a action listener to the unfollow button
			public void actionPerformed(ActionEvent arg0) {
				fc.removefromFaves(SI.getShark());//removing the shark to the favourites
				confirmation = new JOptionPane(SI.getShark().getName()+" has been removed from your favourites",JOptionPane.PLAIN_MESSAGE);//message box to confirm it has been removed
				dialog = confirmation.createDialog("Removed from Favourites!");
				/*Setting the preferences for the dialog box*/
				dialog.setAlwaysOnTop(true);
				dialog.setModal(true);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				SI.remove(SI.getUnfollowButton());//Removing the following button
				SI.add(SI.getFollowButton(), BorderLayout.PAGE_END);//Placing the follow button
				/* refreshing the panel */
				SI.repaint();
				SI.validate();
				/*If the favourites list .isempty returns false (favourites exist) then enable the favourites button. otherwise disable the favourites button*/
				if(checkFavouriteList() == false){ts.getFavouritesButton().setEnabled(true);}else{ts.getFavouritesButton().setEnabled(false);};
				//refreshing the title screen.
				ts.repaint();
				ts.validate();
				}
				});
		
		}
	/**
	 * sets the search frame listeners
	 */
	public void setSearchFrameListeners(){
		
		sf.getSearchButtonButton().addActionListener(new ActionListener(){//This is the action listener for the search button. It controls what happens when the search button is pressed.
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSearchAttributes();//This calls a method in which is used in order to obtain information about the sharks.
				clearSearch();//Clearing the search attributes
				sf.getSearchJPanel().removeAll();//Clearing the search panel
				search();//Creating new search attributes*
				createSharkPanels();//Create the shark panels
				(sf.getScrollbar()).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//Disabling the horizontal scrollbar for the panel
				sf.validate();//Refreshing all of the subcomponents in the panel
				sf.repaint();//Resetting the layout and the look of the panel
				
			}});
		
		sf.getSearchByNameButton().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				nameSearch = JOptionPane.showInputDialog("Enter Shark Name:");
				searchedSharkName = ""+nameSearch;
				for(Shark s1 : sharkList){//for loop to search for sharks based of the input
					if((s1.getName()).equals(searchedSharkName)){
						fc.removeSharkPanels();	
						tempSI = new SharkInfo(s1);//creating a new panel
						setSharkInfoListeners(tempSI);//adding listeners
						sf.add(tempSI);
					}
					sf.repaint();
					sf.validate();
			}}});
	}
	/**
	 * checks if the favourites list is empty
	 * @return boolean
	 */
	public boolean checkFavouriteList(){
		return fc.getFavourites().isEmpty();//return whether or not it is empty
	}
	
	/**
	 * creates shark panels based off the search results
	 */
	public void createSharkPanels(){
		for(Shark s5 : sharkSearchResults){//adding a loop to go around until all panels are made
			
			tempSI = new SharkInfo(s5);//creating a new panel
			setSharkInfoListeners(tempSI);//adding listeners
			sf.getSearchJPanel().add(tempSI);//adds new sharkinfo panel to the search results pane
		
		}
		sf.repaint();
		sf.validate();
	}


}
