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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateGenerator {
	private String templateJson;
	private final String helpArgs = "-help";
	private final String currentDir;
	private final String fileType = "tpl";
	protected Map params;
	protected Map templateParams;
	protected String FileName;

	// annotation
	protected String aPath;
	protected String aExtension;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// if no argument list all template available
		// -t template -h list all arguments of this template and doc
		TemplateGenerator generator = new TemplateGenerator();
		generator.generate(args);
	}

	public TemplateGenerator() {
		this.currentDir = System.getProperty("user.dir") + File.separatorChar;
	}
	
	public void generate(String[] args) {
		if(args.length < 1)
		{
			this.listAllTemplates();
		} else {
			this.readAllParameters(args);
			try {
				this.createTemplate(args);
			} catch(Exception e) {
				this.error(e.getMessage());
			}
		}
	}

	protected void createTemplate(String[] args) throws Exception {

		// check if parameter -t exist
		if(!this.params.containsKey("-t")) {
			throw new Exception("Please specify the template. Ex. -t=templateName");
		} else {
			File template = new File(this.currentDir + this.params.get("-t") + this.fileType);

			// check if template exist
			if(!template.exists())
				throw new Exception(this.params.get("-t") + " not exist. Please run the TemplateGenerator.jar without argument to list available template");

			//read arguments of template
			
			if(args.length < 2) {
				this.readTemplateParameter(template);
			}
		}
	}

	public void readTemplateParameter(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			boolean isInsideStart = false;
			String templateParams[];
			
			while((line = reader.readLine()) != null) {

				if(isInsideStart) {
					templateParams = line.split(",");
				}
				
				// check @start and @end
				if(line.startsWith("@start")) {
					isInsideStart = true;
				} else if(line.startsWith("@end")) {
					isInsideStart = false;
				}
			}
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void error(String error) {
		System.err.println("\nError: " + error);
	}

	/**
	 * Read parameter pass in cli
	 * @param args 
	 */
	protected void readAllParameters(String[] args) {
		this.params = new HashMap();
		String[] param = null;
		for (String arg : args) {
			if(arg.contains("=")) {
				param = arg.split("=");
				this.params.put(param[0], param[1]);				
			}
		}
	}

	public void listAllTemplates() {
		this.createHeader("List of Template");
		File folder = new File(this.currentDir);
		File[] files = folder.listFiles();
		boolean hasTemplate = false;
		BufferedReader reader = null;

		// read all file name within the current directory
		for (File file : files) {
			if (file.isFile()) {
				String name = file.getName();
				try {
					// read only the file with template extension
					String type = name.substring(name.lastIndexOf(this.fileType));
					int index = name.lastIndexOf(".");
					if(index > 0) {
						if(name.substring(index + 1).equals(this.fileType)) {
							hasTemplate = true;
							reader = new BufferedReader(new FileReader(file));
							String doc = reader.readLine();
							System.out.println(name.substring(0, index) + ": " + doc);
						}
					}
				} catch (Exception e) {
					// disregard filename without extension
				}
			}
		}
		if(!hasTemplate) {
			System.err.println("No Available Template");
		} else {
			this.basicUsage();
		}
	}

	protected void basicUsage() {
		System.out.println("\n\nBasic Usage: java -jar TemplateGenerator.jar fileName -t=templateName\n");
	}

	protected void templateUsage() {

	}

	/**
	 * Display the header inside the box
	 * @param header 
	 */
	public void createHeader(String header) {
		String box = "";
		String line = "*";
		int maxLength = header.length() + 8;
		for(int i = 0; i < maxLength; i++) {
			line+="-";
		}
		line+="*\n";
		box+=line;
		box+="|    " + header + "    |\n";
		box+=line;
		System.out.println(box);
	}

}
//TemplateGenerator.jar fileName -t MyModel -cls=User -id=user_id -pup=[username,password,age,location]