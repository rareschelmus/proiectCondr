package model;

public class TopProduct implements Comparable<TopProduct> {
	private String name;
	private String id;
	private String rating;
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}

	public int compareTo(TopProduct arg0) {
		return Integer.parseInt(this.rating) - Integer.parseInt(arg0.getRating());
	}
	
}
