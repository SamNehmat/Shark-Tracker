
package ocearch.controller;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import api.jaws.Jaws;
import api.jaws.Shark;
import ocearch.model.User;
import ocearch.view.Favourites;
import ocearch.view.SearchFrame;
import ocearch.view.SharkInfo;
import ocearch.view.TitleScreen;
/*
 * This class contains the code behind the favourites frame GUI
 */
public class FavouritesController {
	private Jaws j = new Jaws("s5HxyvUdVjzOi67y", "jU62ba5dgyl9tXI2", false);//Accessing the jaws API
	/* Declaring Variables */
	private ArrayList<Shark> favourites;
	private ArrayList<String> savedSharkNames;
	private TitleScreen ts;
	private Favourites f;
	private SearchFrame sf;
	private final double kclLong = -0.115825;
	private final double kclLat = 51.511660;
	private double sharkLat, sharkLong, longFromKCL, latFromKCL;
	private File favouritesCSV;
	private SharkInfo tempSI;
	private JLabel tempLabel;
	private UserController uc;
	private BufferedReader br;
	private User selectedUser;
	private String selectedUserName, jLabelName, firstWord;
	
	/**
	 * 
	 * @param ts
	 * @param f
	 * @param sf
	 * @param uc
	 */
	public FavouritesController(TitleScreen ts, Favourites f, SearchFrame sf, UserController uc){
		/*Referring to the current instances of the variables*/
		this.ts = ts;
		this.f = f;
		this.sf = sf;
		this.uc = uc;
		
		favourites = new ArrayList<Shark>();//Creating a new arraylist
		try {//Reading the CSV file
			readCSV();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setListeners();//Calling the method setListeners
		setComboBoxListeners();//Calling the method setComboBoxListeners
	}
	
	/**
	 * sets the listeners for the favourites frame
	 */
	public void setListeners(){

		(ts.getFavouritesButton()).addActionListener(new ActionListener(){//Adding an action listener for the favourites button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				f.getFavePanel().removeAll();//Removing all the components from the favourites panel
				JLabel jlSharkFav = new JLabel("Your favourite sharks are this far away from you right now:");
			    jlSharkFav.setAlignmentX(Component.TOP_ALIGNMENT);//Setting the alignment of the components
			    jlSharkFav.setFont(new Font("Serif", Font.PLAIN, 20));//Setting the font of the component
			    (f.getFavePanel()).add(jlSharkFav);//adding the label to the panel
			    String listString = "";
			    	for(Shark s: favourites){//Using a for loop to go around the favourites file and get the names and distances away from the user in miles
			    		findDistances(s);
					    listString = s.getName()+" Distance: "+distanceFromKCL()+" miles";
					    tempLabel = new JLabel(listString);
					    setListenersForFavesList(tempLabel);
					    (f.getFavePanel()).add(tempLabel);
					    
			    	}
			    	 f.setVisible(true);//Making the panel visible
			    	 /* Refreshing the panel */
					 f.repaint();
					 f.validate();
			    }});
		
			f.addWindowListener(new WindowAdapter(){//Adding a window listener
			@Override
			 public void windowClosing(WindowEvent arg0) {//When the window closes the arraylist will write to the CSV file
				    try {
						writeToCSV();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				    uc.getUserList().clear();//Clear the list of users so it can be repopulated again without old data
				  }

				  public void windowOpened(WindowEvent arg0) {}
				  public void windowClosed(WindowEvent arg0) {}
				  public void windowIconified(WindowEvent arg0) {}
				  public void windowDeiconified(WindowEvent arg0) {}
				  public void windowActivated(WindowEvent arg0) {}
				  public void windowDeactivated(WindowEvent arg0) {}

				});
		}
	/**
	 * Sets the combo box listeners
	 */
	public void setComboBoxListeners(){
		
		ts.getComboBox().addItemListener(new ItemListener(){//Adding an item listener to the combo box
			@Override
			public void itemStateChanged(ItemEvent e) { //when the state of the item in the jcombobox is change
				JComboBox comboBox = (JComboBox) e.getSource(); //the combobox is returned

                Object selected = comboBox.getSelectedItem(); //the selected item in the combo box is fetched
                selectedUserName = ""+selected; //it's made equal to the selected username
                selectedUser = uc.getUser(selectedUserName); //the user name object is returned using the username string from the jcombobox
               
                favouritesCSV = new File("source/data/"+selectedUserName+"favourites.csv"); //a new unique file object is made for the user which will hold the favourites
                
                try {
					readCSV(); //the CSV file which contains the favourites of the selected user is read and loaded
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}});
		}
	/**
	 * adds a shark to the favourites arraylist
	 * @param s
	 */
	public void addToFaves(Shark s){//
		favourites.add(s);//adding a shark from the favourites
	}
	/**
	 * removes a shark from the favourites arraylist
	 * @param s
	 */
	public void removefromFaves(Shark s){//method to remove a shark from the favourites
		favourites.remove(s);//Removing a shark from the favourites
	}
	/**
	 * 
	 * @return ArrayList<Shar> favourites
	 */
	public ArrayList<Shark> getFavourites(){
		return favourites;//return the favourites
	}
	/**
	 * 
	 * @return int favourites size
	 */
	public int getNumberOfFavourites(){
		return favourites.size();//return the number of favourites
	}
	/**
	 * reads the CSV file which holds the favourites for a user
	 * @throws IOException
	 */
	public void readCSV() throws IOException{
		savedSharkNames = null; // arraylist of favourites and name of the favourited sharks is emptied
		favourites = null;
		favourites = new ArrayList<Shark>();
		savedSharkNames = new ArrayList<String>();
		File uniqueFile = new File("source/data/"+selectedUserName+"favourites.csv"); //a new unique file object is made for the user which will hold the favourites
		uniqueFile.createNewFile(); //the file is created in the default directory 
		String csvFile = ("source/data/"+selectedUserName+"favourites.csv"); //the string contains the path of the new file
		String cvsSplitBy = (","); //what the words in the file are split up by
		br = new BufferedReader(new FileReader(csvFile)); //a new reader is created to read the file
	        
		String line = "";
		while ((line = br.readLine()) != null){ //while there is a new line containing a piece of text

			String[] cols = line.split(cvsSplitBy); //add text to a string array
			savedSharkNames.add(cols[0]); //adds the values in the string array to an arraylist
			}
		
		for(String s : savedSharkNames){ //for every name in the savedSharkNames list
			favourites.add(j.getShark(s)); //adds sharks with the names in the arraylist to an array list of sharks
		
		}
		}
	/**
	 * writes to a CSV file with a list of favourites for a user
	 * @throws FileNotFoundException
	 */
	public void writeToCSV() throws FileNotFoundException{

			favouritesCSV = new File("source/data/"+selectedUserName+"favourites.csv"); //a new unique file object is made for the user which will hold the favourites
			try {
				favouritesCSV.createNewFile(); //the file is created in the default directory 
			} catch (IOException e1) {
		
				e1.printStackTrace();
			}

		PrintWriter  writer = new PrintWriter(favouritesCSV); //a writer which writes to a file is created
		StringBuilder sb = new StringBuilder(); //a string builder which builds a string out of smaller strings is created

			for(Shark s : favourites){ //for every shark in the favourites arraylist
				sb.append(s.getName()); //the name is added to the end of the string builder
				sb.append(","); //a comma is added to the end of the string builder
				sb.append('\n');//a line space is added to the end of the string builder
			}
			writer.write(sb.toString()); //writes the sting to the file
			writer.close(); //closes the writer
		}
	/**
	 * finds the differences in longitude and latitude of a shaark relatives to KCL
	 * @param s
	 */
	public void findDistances(Shark s){
		sharkLong = j.getLastLocation(s.getName()).getLongitude();//Getting the shark longitude
		sharkLat = j.getLastLocation(s.getName()).getLatitude();//Getting the shark latitude
		longFromKCL = kclLong-sharkLong;//Calculating the longitude distance away from user location
		latFromKCL = kclLat-sharkLat;//Calculating latitude distance away from user location
	}
	/**
	 * returns longitude distance away from KCL
	 * @return longFromKCL
	 */
	public double longFromKCL(){
		return longFromKCL;//Return the longitude distance away from user location
	}
	/**
	 * returns latitude distance away from KCL
	 * @return latFromKCL
	 */
	public double latFromKCL(){
		return latFromKCL;	//Return the latitude distance away from user location
	}
	
	/**
	 * calculates the distance (in miles) from KCL using the latitude and longitude. returns asnwer to 2 decimal places
	 * @return double dist
	 */
	public double distanceFromKCL() {//Method to calculate the distance of each shark from the user location
		double theta = longFromKCL;
		double dist = Math.sin(deg2rad(kclLat)) * Math.sin(deg2rad(sharkLat)) + Math.cos(deg2rad(kclLat)) 
						* Math.cos(deg2rad(sharkLat)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = RoundTo2Decimals(dist);
		return dist;
	}
	/**
	 * converts degrees to radians
	 * @param deg
	 * @return
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	/**
	 * coverts radians to degrees
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	/**
	 * rounds double values to two decimal places
	 * @param val
	 * @return
	 */
	double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
    
       return Double.valueOf(df2.format(val));
	}
	/**
	 * sets listeners for the list of favourite's JLabel
	 * @param jl
	 */
	public void setListenersForFavesList(JLabel jl){
		
		jl.addMouseListener(new MouseListener(){ //adds a listener to a jlabel which holds the name and distanceFromKCL of a shark

			@Override
			public void mouseClicked(MouseEvent e) {
					
				jLabelName = jl.getText();
				String firstWord = null;
						if(jLabelName.contains(" ")){
						   firstWord = jLabelName.substring(0, jLabelName.indexOf(" ")); 
					
					removeSharkPanels();	
					tempSI = new SharkInfo(j.getShark(firstWord));//creating a new panel
					sf.add(tempSI);
					sf.repaint();
					sf.validate();
					f.dispose();
			}}
			
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			});}
	/**
	 * creates one shark panel for a given shark
	 * @param s
	 */
	public void createOneSharkPanel(Shark s){
		tempSI = new SharkInfo(s);//Creating a new shark info object
		sf.getSearchJPanel().removeAll();//Removing all of the components from the panel
		sf.getSearchJPanel().add(tempSI);//Adding the shark info object to the search panel
		/*Refreshing the frame*/
		sf.repaint();
		sf.validate();
	}
	
	/**
	 * removes all the sharkinfo panels in the search results pane
	 */
	public void removeSharkPanels(){
			sf.getSearchJPanel().removeAll();//removes all sharkinfo panels from the search results pane
		}
	
}
