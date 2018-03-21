import java.io.IOException;

public class SQLInject {

	public static void main(String[] args) throws IOException {

		PasswordFinder pwf = new PasswordFinder("tin");
		
		System.out.println(pwf.getPassword());

	}

}
