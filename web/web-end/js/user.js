
//信息： 用户名 电话 证件类型 证件号 邮箱 
var session;
$(function () {
  session = sessionStorage.getItem('session');//获取session_id;
    $.ajax({
    url:"/hhlab/admin/showAllUsers",
    type:'POST',
    data: JSON.stringify(
              {session_id : session,
             }),
    success:function(result){
      //初始化用户信息
     console.log(result);
     processData(result);
      
    },
    error:function(){
      alert("System inner error!");
    }
  });

});

function processData(result)
{     
       if(result.length == 0){
          $(".tbody").empty();//当前查询没有结果
           $(".tbody").append("当前数据库没有相关数据");
       }

         var user, phone, id_type, id, email,text;
            for(var i = 0; i<result.length;i++)
                { 
                   user = result[i].name;
                   phone = result[i].tel;
                   email = result[i].email;
                   id = result[i].certificateid;
                   id_type = result[i].certificate;

                   switch(id_type){
                    case 1: text = "身份证";break;
                    case 2: text = "护照";break;

                   }

                   if (i%2 == 0) { 
                    $(".tbody").append(
                        "<tr class=odd gradeX>"+
                            "<td>"+user+"</td>"+
                            "<td>"+phone+"</td>"+
                             "<td>"+email+"</td>"+ 
                            "<td>"+text+"</td>"+
                            "<td>"+id+"</td>"+
                            "<td><a href='userDetail.html?tel="+phone+"'><button>查看详情</button></a></td>"+   
                        "</tr>"
                      )
                    }
                    else{
                        $(".tbody").append(
                        "<tr class=odd gradeC>"+
                            "<td>"+user+"</td>"+
                            "<td>"+phone+"</td>"+
                             "<td>"+email+"</td>"+ 
                            "<td>"+text+"</td>"+
                            "<td>"+id+"</td>"+
                            "<td><a href='userDetail.html?tel="+phone+"'><button>查看详情</button></a></td>"+  
                        "</tr>"
                       )

                    }
                  
                }

}


function search(){

  var inputValue = $("#search").val();
  console.log(inputValue);
  var url = "/hhlab/admin/getuser";
  $.ajax({
    url : url ,
    type : 'POST',
    data : JSON.stringify({
          session_id : session,
          tel : inputValue,
    }),
    success:function(result){
      //初始化用户信息
      console.log(result);
     $(".tbody").empty();//先清除信息 显示查询信息
     processData(result);
      
    },
    error:function(){
      alert("System inner error!");
    }
  });


}