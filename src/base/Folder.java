package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Folder implements Comparable<Folder>, Serializable{
	private ArrayList<Note> notes;
	private String name;
	private static final long serialVersionUID = 1L;
	
	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}
	
	public void addNote(Note note) {
		notes.add(note);
		
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Note> getNotes() {
		return notes;
	}
	
	public String toString() {
		int nText = 0;
		int nImage = 0;
		for(Note n:notes) {
			if (n instanceof TextNote) 
				nText++;
			else
				nImage++;
		}
		return name + ":" + nText + ":" + nImage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		if(this.name.compareTo(o.name) < 0) {
			return -1;
		}
		else if( this.name.compareTo(o.getName()) > 0) {
			return 1;
		}
		else
			return 0;
	}
	
	public void sortNotes() {
		Collections.sort(notes);
		//System.out.println("suceed");
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> result = new ArrayList<Note>();	
		List<Integer> list = new ArrayList<Integer>();
		int count;
		
		String[] tokens = keywords.split(" ");
		//System.out.println(keywords);
		//System.out.println(tokens[1]);
		//List<String> string = Arrays.asList(tokens);
		//System.out.println(string);
		for(int i=0; i<tokens.length; i++) {
			if(tokens[i].equals("or") || tokens[i].equals("OR")) {
				list.add(i);
				//System.out.println(i);
			}
		}
		//do checking
		for(Note n:notes) {
			count = 0;
			for(int i=0; i<tokens.length; i++) {
				if(i == list.get(count)) {
					i++;
					count++;
					if(i == tokens.length-1) {
						result.add(n);
					}
					continue;
				}
				
				if(n instanceof ImageNote) {
					//case 1: match
					if(n.getTitle().toLowerCase().contains(tokens[i].toLowerCase())) {
						if(i == tokens.length-1) {
							result.add(n);
						}
					}
					//case 2: check OR condition
					else if(i+1 == list.get(count)) {
						if(n.getTitle().toLowerCase().contains(tokens[i+2].toLowerCase())) {
							if(i == tokens.length-1) {
								result.add(n);
							}
						}
						else {
							break;
						}
					}
					//case 3: no match
					else {
						break;
					}
				}
				else {
					//case 1: match
					if(n.getTitle().toLowerCase().contains(tokens[i].toLowerCase())
							|| ((TextNote)n).getContent().toLowerCase().contains(tokens[i].toLowerCase())) {
						if(i == tokens.length-1) {
							result.add(n);
						}
					}
					//case 2: check OR condition
					else if(i+1 == list.get(count)) {
						if(n.getTitle().toLowerCase().contains(tokens[i+2].toLowerCase())
								|| ((TextNote)n).getContent().toLowerCase().contains(tokens[i+2].toLowerCase())) {
							if(i == tokens.length-1) {
								result.add(n);
							}
						}
						else {
							break;
						}
					}
					//case 3: no match
					else {
						break;
					}
				}

			}
			

		}
		return result;
	}
}
