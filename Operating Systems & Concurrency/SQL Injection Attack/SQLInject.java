import java.io.IOException;

public class SQLInject {
	
	/* creates a password finder with the username of the user as the argument.
	 * prints the password to the console
	 */
	public static void main(String[] args) throws IOException {
		PasswordFinder pwf = new PasswordFinder("tin");
		
		System.out.println(pwf.getPassword());

	}
	
	/* DISCLAIMER:
	 * Permission was given by Dr Andrew Coles to attack the URL given and no other URL.
	 */

}
