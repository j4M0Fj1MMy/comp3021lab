package base;

import java.util.Date;
import java.util.Objects;
import java.io.Serializable;
import java.util.ArrayList;

public class Note implements Comparable<Note>,Serializable{
	private Date date;
	private String title;
	private static final long serialVersionUID = 1L;
	
	
	public Note(String title) {
		this.title = title;
		date = new Date(System.currentTimeMillis());
	}
	
	public String getTitle() {
		return this.title;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		return Objects.equals(title, other.title);
	}

	@Override
	public int compareTo(Note o) {
		// TODO Auto-generated method stub
		if(this.date.compareTo(o.date) < 0) {
			return 1;
		}
		else if (this.date.compareTo(o.date) > 0  ) {
			return -1;
		}
		else
			return 0;
	}
	
	public String toString() {
		return date.toString() + "\t" + title;
	}
	
}
