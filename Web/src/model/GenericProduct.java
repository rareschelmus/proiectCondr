package model;

public class GenericProduct implements Comparable<GenericProduct> {
	private String name;
	private String id;
	private String rating;
	private int    relatedRating;
	
	public String getId() {
		return id;
	}
	
	public int getRelatedRating() {
		return relatedRating;
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

	public int compareTo(GenericProduct arg0) {
		if (this.rating!=null){
		return Integer.parseInt(this.rating) - Integer.parseInt(arg0.getRating());
		}
		return relatedRating = arg0.getRelatedRating();
	}
	
	public void setRelatedRating(int relatedRating) {
		this.relatedRating = relatedRating;
	}
	
}
