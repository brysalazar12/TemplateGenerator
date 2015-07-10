/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templategenerator;

/**
 *
 * @author Admin
 */
import com.google.gson.Gson;

public class TemplateGenerator {
	private String templateJson;
	private String helpArgs = "-help";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// if no argument list all template available
		// -t template -h list all arguments of this template and doc
	}
	
	public void generate(String[] args) {

	}

	public void readTemplateJson() {

	}

	public void readDoc() {

	}
}

// TemplateGenerator.jar templateName param1 param2 param3

//TemplateGenerator.jar -t MyModel -cls=User -id=user_id -pup=[username,password,age,location]