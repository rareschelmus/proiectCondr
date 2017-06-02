package model;

public class UserCommentData
	{
		private String userID;
		private String userName;
		private String comment;
		private String userImageUrl;
		public UserCommentData(String userId, String userName, String comment, String userImageUrl)
		{
			this.userID = userId;
			this.userName = userName;
			this.comment = comment;
			this.userImageUrl = userImageUrl;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public String getComment() {
			return comment;
		}
		
		public String getUserID() {
			return userID;
		}
		
		public String getUserImageUrl() {
			return userImageUrl;
		}
		
		public void setComment(String comment) {
			this.comment = comment;
		}
		
		public void setUserID(String userID) {
			this.userID = userID;
		}
		
		public void setUserImageUrl(String userImageUrl) {
			this.userImageUrl = userImageUrl;
		}
		
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
	
	}
