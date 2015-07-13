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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateGenerator {
	private String templateJson;
	private final String helpArgs = "-help";
	private final String currentDir;
	private final String fileType = "tpl"; // file type of template
	protected Map params; // param in cli
	protected Map<String,String> templateParams; // param in template
	protected Map<String,String> templateParamsDoc;

	protected ArrayList<String> templateArgs;

	// annotation
	protected String filePath = ""; // path of file that will be generated
	protected String fileNameType; // extension of fileName
	protected String fileName; // fileName that will be generated
	protected String fileNamePrefix = ""; // prefix to fileName that will be generated
	protected String fileNameSuffix = ""; // suffix to fileName that will be generated

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
		this.templateArgs = new ArrayList<>();
		this.templateParams = new HashMap<>();
		this.templateParamsDoc = new HashMap<>();
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
		if(args[0].startsWith("-"))
			throw new Exception("Invalid format. It should be \"fileName -t=temaplateName -args1=args1 -args2=args2\"");

		// check if parameter -t exist
		if(!this.params.containsKey("-t"))
			throw new Exception("Please specify the template. Ex. -t=templateName");

		this.fileName = args[0];
		this.params.put("fileName", this.fileName);
		this.templateParams.put("fileName", "fileName");
		File template = new File(this.currentDir + this.params.get("-t") + "." + this.fileType);

		// check if template exist
		if(!template.exists())
			throw new Exception(this.params.get("-t") + " not exist. Please run the TemplateGenerator.jar without argument to list available template");

		//read arguments of template
		
		if(args.length >= 2) {
			this.readTemplateParameter(template);
		}
	}

	public void readTemplateParameter(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			boolean isInsideStart = false;
		
			
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.startsWith("@end"))
					break;
				
				if(isInsideStart) {
					if(line.startsWith("-")) {
						String[] tplArgs = line.split("\\|");
						String[] keys = tplArgs[0].split(":");
						
						this.templateParams.put(keys[1], keys[0]);
						this.templateParamsDoc.put(keys[0], tplArgs[1].substring(4));
						this.templateArgs.add(line);
					}

					if(line.startsWith("@path"))
						this.filePath = line.replace("@path:", "");

					if(line.startsWith("@fileType"))
						this.fileNameType = line.replace("@fileType:", "");

					if(line.startsWith("@filePrefix"))
						this.fileNamePrefix = line.replace("@filePrefix:", "");

					if(line.startsWith("@fileSuffix"))
						this.fileNameSuffix = line.replace("@fileSuffix:", "");
				}
				
				// check @start and @end
				if(line.startsWith("@start"))
					isInsideStart = true;
			}
			reader.close();
			
			if(this.params.containsKey("-help")) {
				this.templateUsage();
			} else {
				this.createFile(file);
			}
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void createFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			boolean isAfterEnd = false;
			String fileName = this.filePath + this.fileNamePrefix + this.fileName + this.fileNameSuffix + "." + this.fileNameType;
			File newFile = new File(fileName);
			if(!newFile.createNewFile()) {
				this.error(fileName + " already exist.");
				return;
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
				Pattern pattern = Pattern.compile("\\{\\w+\\}");
				Matcher matcher;
				
				while((line = reader.readLine()) != null) {
					
					if(isAfterEnd) {
						matcher = pattern.matcher(line);
						if(matcher.find()) {
							String match = matcher.group();
							String key = match.replace("{", "").replace("}", "");
							if(this.templateParams.get(key) != null) {
								line = line.replace(match, this.params.get(this.templateParams.get(key)).toString());
							}
						}
						writer.write(line + "\n");
					}
					
					if(line.startsWith("@end")) {
						isAfterEnd = true;
					}
				}
			}
			
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TemplateGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	protected void error(String error) {
		System.out.println("\nError: " + error);
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
			} else if(arg.contains("-help")) {
				this.params.put("-help", "");
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

							// read doc
							String doc = reader.readLine();
							if(doc == null) {
								doc = "No Documentation.";
							}
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
		this.createHeader("List of " + this.params.get("-t").toString() + " arguments");
		for(Entry<String,String> entry : this.templateParams.entrySet()) {
			System.out.println(entry.getValue() + ": " + this.templateParamsDoc.get(entry.getValue()));
		}
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