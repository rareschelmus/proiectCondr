         var httpG = new XMLHttpRequest();
         var httpF = new XMLHttpRequest();
         var url = "/Web/Controller/Login";
         httpG.open("POST", url, true);
         httpF.open("POST", url, true);

         //Send the proper header information along with the request
         httpG.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
         httpF.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");

         httpG.onreadystatechange = function() {//Call a function when the state changes.
             if(httpG.readyState == 4 && httpG.status == 200) {
                 if (httpG.responseText=="++")  {
                	 window.location.replace("http://localhost:8080/Web/Controller/MainPageModel");
                 } else {
                	 window.location.replace("http://localhost:8080/Web/Controller/LoginFail");
                 }
             }
         }
         
         httpF.onreadystatechange = function() {//Call a function when the state changes.
             if(httpF.readyState == 4 && httpF.status == 200) {
            	 if (httpF.responseText=="++")  {
                	 window.location.replace("http://localhost:8080/Web/Controller/MainPageModel");
                 } else {
                	 window.location.replace("http://localhost:8080/Web/Controller/LoginFail");
                 }
             }
         }
         
         window.fbAsyncInit = function() {
       	  FB.init({
       	    appId      : '291893044602593',
       	    cookie     : true,  // enable cookies to allow the server to access 
       	                        // the session
       	    xfbml      : true,  // parse social plugins on this page
       	    version    : 'v2.8' // use graph api version 2.8
       	  });
         };
         
      // Load the SDK asynchronously
         (function(d, s, id) {
           var js, fjs = d.getElementsByTagName(s)[0];
           if (d.getElementById(id)) return;
           js = d.createElement(s); js.id = id;
           js.src = "//connect.facebook.net/en_US/sdk.js";
           fjs.parentNode.insertBefore(js, fjs);
         }(document, 'script', 'facebook-jssdk'));
      
         function fb_login() {
        	    FB.login( function (response) {
        	      if (response.authResponse) {
        	            console.log('Welcome!  Fetching your information.... ');
        	            
        	            access_token = response.authResponse.accessToken; //get access token
        	            user_id = response.authResponse.userID; //get FB UID
        	            name = response.name;
        	           
        	            FB.api('/me?access_token='+access_token, {scope : 'email' } ,function (response) {
        	                var email = response.email;
        	                var name = response.name;   
        	                console.log(access_token);
        	                httpF.send("token="+access_token+"&mt=f");     
        	            });
        	            

        	        } else {
        	            //user hit cancel button
        	            console.log('User cancelled login or did not fully authorize.');
        	        }
        	    },{scope : 'email' });
        	    
        	 
         }
         
         var googleUser = {};
         var startApp = function() {
           gapi.load('auth2', function(){
             // Retrieve the singleton for the GoogleAuth library and set up the client.
             auth2 = gapi.auth2.init({
               client_id: '494217629857-3qlnbam99rn8v2o3521c3nuq7lpp1o8f.apps.googleusercontent.com',
               cookiepolicy: 'single_host_origin',
               // Request scopes in addition to 'profile' and 'email'
               //scope: 'additional_scope'
             });
             
             attachSigninGoogle(document.getElementById('customBtn'));
           });
         };

         function attachSigninGoogle(element) {
           console.log(element.id);
           auth2.attachClickHandler(element, {},
               function(googleUser) {
        	     httpG.send("token="+googleUser.getAuthResponse().id_token+"&mt=g&name="+googleUser.getBasicProfile().getName()+"&url="+googleUser.getBasicProfile().getImageUrl());  
               }, function(error) {
                 alert(JSON.stringify(error, undefined, 2));
               });
         }
         
         var http = new XMLHttpRequest();
         var url = "/Web/Controller/Logout";
         http.open("POST", url, true);
         http.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
         
         http.onreadystatechange = function() {//Call a function when the state changes.
             if(http.readyState == 4 && http.status == 200) {
                 console.log("log out ok");
                  window.location.replace("http://localhost:8080/Web/Controller/M");
             }
         }
         
         function logout() {
           http.send("");
         }
         
         function deleteAction() {
        	 var http = new XMLHttpRequest();
             var url = "/Web/Controller/DeleteAcount";
             http.open("POST", url, true);
             http.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
             http.send("");
             http.onreadystatechange = function() {//Call a function when the state changes.
                 if(http.readyState == 4 && http.status == 200) {
                     console.log("log out ok");
                      window.location.replace("http://localhost:8080/Web/Controller/M");
                 }
             }
             
         }
         
         
         function eanSearch() {
      	   var element = document.getElementById("active_search_type");
      	   element.innerHTML = document.getElementById("eanSearch").innerHTML;
      	   var d = document.getElementById("searchText");
      	    d.setAttribute("placeholder","Search barcode");
      	    d.setAttribute("name", "EAN");
         }
         function productSearch() {
      	  var e = document.getElementById("active_search_type");
      	   e.innerHTML = document.getElementById("productSearch").innerHTML;
      	   var d = document.getElementById("searchText");
     	    d.setAttribute("placeholder","Search product name");
     	    d.setAttribute("name", "Product");
         }
         function brandSearch() {
       	  var e = document.getElementById("active_search_type");
       	   e.innerHTML = document.getElementById("brandSearch").innerHTML;
       	   var d = document.getElementById("searchText");
           	d.setAttribute("name", "Brand");
      	    d.setAttribute("placeholder","Search brand name");
          }
         function categorySearch() {
        	  var e = document.getElementById("active_search_type");
        	   e.innerHTML = document.getElementById("categorySearch").innerHTML;
        	   var d = document.getElementById("searchText");
        	    d.setAttribute("name", "Category");
       	        d.setAttribute("placeholder","Search category name");
           }

         