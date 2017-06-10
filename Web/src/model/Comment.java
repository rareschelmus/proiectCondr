package model;

public class Comment  implements Comparable<Comment> {
		private String comment;
		private String rating;
		private String userId;
		private String commentId;
		private String userImage;
		private String badTags;
		private String goodTags;
		private String userName;
		private String canEdit;
		private java.sql.Date date;
		
		public void setDate(java.sql.Date date) {
			this.date = date;
		}
		
		public String getCanEdit() {
			return canEdit;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public String getGoodTags() {
			return goodTags;
		}
		
		public String getUserImage() {
			return userImage;
		}
		
		public void setBadTags(String badTags) {
			this.badTags = badTags;
		}
		
		public void setGoodTags(String tags) {
			this.goodTags = tags;
		}
		
		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}
		
		public String getBadTags() {
			return badTags;
		}
		
		public String getComment() {
			return comment;
		}
		
		public String getCommentID() {
			return commentId;
		}
		
		public String getRating() {
			return rating;
		}
		
		public String getUserId() {
			return userId;
		}
		
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public void setComment(String comment) {
			this.comment = comment;
		}
		
		public void setCommentId(String commentId) {
			this.commentId = commentId;
		}
		
		public void setRating(String rating) {
			this.rating = rating;
		}
		
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public void setCanEdit(String canEdit) {
			this.canEdit = canEdit;
		}
		
		public java.sql.Date getDate() {
			return date;
		}

		public int compareTo(Comment arg0) {
			return arg0.getDate().compareTo(this.date);
		}
}
