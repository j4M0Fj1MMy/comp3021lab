package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.BufferedWriter;


public class TextNote extends Note implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	public TextNote(String title) {
		super(title);
	}
	
	public TextNote(String title, String content) {
		super(title);
		this.content = content;
	}
	
	public TextNote(File f){
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	
	public String getContent() {
		return content;
	}
	
	private String getTextFromFile(String absolutePath){
		String result = "";
		//File file = new File(absolutePath);
		
		try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while(line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	public void exportTextToFile(String pathFolder) {
		if(pathFolder == "") {
			pathFolder = ".";
		}
		
		File file = new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		
		try (PrintWriter writer = new PrintWriter(file);){
			writer.print(this.content);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
