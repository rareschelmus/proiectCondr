package common;

public class Product {
	private String id,name,description,img1,img2,img3;
   public Product(String id,String name,String description,String img1) {
	   this.name=name;
	   this.description=description;
	   this.id=id;
	   this.img1=img1;
   }
   public Product(String id,String name,String description,String img1,String img2,String img3) {
	   this.name=name;
	   this.description=description;
	   this.id=id;
	   this.img1=img1;
	   this.img2=img2;
	   this.img3=img3;
   }
   public String getName() {
	   return name;
   }
   public String getDescription() {
	   if (description==null) return "Something good";
	   return description;
   }
   public String getId() {
	   return id;
   }
   public String getImage() {
	   return img1;
   }
   public String getImage2() {
	   return img2;
   }
   public String getImage3() {
	   return img3;
   }
 }
