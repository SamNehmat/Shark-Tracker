package ocearch.model;

import java.util.ArrayList;

import api.jaws.Shark;
/*
 * This class creates a user object
 */
public class User {
	//Declaring variables
	private String userName;//Creating a string for the user name
	
	public User(String userName){
		this.userName = userName;//Reffering to the current instance of the user name
	}

	public String getName(){//Return method for the name of the user
		return userName;//Returning the name
	}

}
