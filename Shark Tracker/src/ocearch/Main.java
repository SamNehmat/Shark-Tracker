package ocearch;

import ocearch.controller.Controller;
import ocearch.controller.FavouritesController;
import ocearch.controller.UserController;
import ocearch.view.Favourites;
import ocearch.view.SearchFrame;
import ocearch.view.TitleScreen;

public class Main {
	
/*
 * The main class is used in order to create new instances of each frame in which is used within the program.
 * These frames include the Title screen, Search Frame and the Favourites Frame. It also initialises the frame controllers
 * for each of these frames in which controls the code behind the GUI.
 */
	
	public static void main(String[] args) {
		//Declaring new instances of each frame and their controllers.
		SearchFrame search = new SearchFrame();
		Favourites faves = new Favourites();
		TitleScreen title = new TitleScreen();
		UserController user = new UserController(title);
		FavouritesController favesCon = new FavouritesController(title, faves, search, user);
		Controller con = new Controller(search, favesCon, title);
		
		title.setVisible(true);//Making the title visible
		
	}

}
