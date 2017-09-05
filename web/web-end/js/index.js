$(function(){
    
    var session = sessionStorage.getItem('session');//获取session_id;
    console.log(session);
    $.ajax({
    	url: "/hhlab/user/info",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(
              {session_id : session,
             }),

        success: function (result) {
         var data =JSON.parse(result.user_detail);//解析json对象
         $("#user").text(data.user_name);
        },
        error: function () {
          alert("System error!");
        }

    });
	// var name = getCookie("Session");
	// alert(name);
});


