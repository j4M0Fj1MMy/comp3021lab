package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook {
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	
	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}

	public boolean createImageNote(String folderName, String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
	}
	
	public ArrayList<Folder> getFolders(){
		return folders;
	}

	public boolean insertNote(String folderName, Note note) {
		Folder f2 = null;
		for(Folder f:folders) {
			if(f.getName() == folderName) {
				f2 = f;
				break;
			}
		}
		if (f2 == null) {
			f2 = new Folder(folderName);
			folders.add(f2);
		}
		
		for (Note n: f2.getNotes()) {
			if (n.getTitle() == note.getTitle()) {
				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
		}

		f2.addNote(note);
		return true;
	}
	
	public void sortFolders() {
		for(Folder f:folders) {
			f.sortNotes();
		}
		Collections.sort(folders);
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> temp = new ArrayList<Note>();
		for(Folder f:folders) {
			temp.addAll(f.searchNotes(keywords));
		}
		return temp;
	}
	
	public boolean createTextNote(String folderName, String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
}
