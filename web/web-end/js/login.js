$(function(){
  $("#login").on('click', function () {
      var username = $("#username").val();
      var password = $("#password").val();
      $.ajax({
        url: "",
        type: "POST",
        data: {username: username, password: password},
        success: function (result) {
          var items = result['data'];
          //alert(items[0].username);
          window.location.href="index.html";
        },
        error: function () {
          alert("System error!");
        }
      });
  });
});