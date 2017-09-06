$(function(){
  $("#login").on('click', function () {
      var username = $("#username").val();
      var password = $("#password").val();
    
      $.ajax({
        url: "/hhlab/admin/login",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(
              {log_name:username, 
               password:password
             }),

        success: function (result) {
          
          sessionStorage.setItem('session',result.message);
          window.location.href="index.html";
        },
        error: function () {
          alert("System error!");
        }
      });
  });
});