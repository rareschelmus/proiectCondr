package common;

public class Product {
	String id,name,description;
   public Product(String id,String name,String description) {
	   this.name=name;
	   this.description=description;
	   this.id=id;
   }
   public String getName() {
	   return name;
   }
   public String getDescription() {
	   return description;
   }
   public String getId() {
	   return id;
   }
 }
