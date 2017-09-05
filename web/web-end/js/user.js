// <tr class="odd gradeX">
// <td>Trident</td>
// <td>Internet Explorer 4.0</td>
// <td>Win 95+</td>
// <td class="center">4</td>
// <td class="center">X</td>
// </tr>
// <tr class="even gradeC">
// <td>Trident</td>
// <td>Internet Explorer 5.0</td>
// <td>Win 95+</td>
// <td class="center">5</td>
// <td class="center">C</td>
// </tr>
//信息： 用户名 电话 证件类型 证件号 邮箱 
$(function () {

    $.ajax({
    url:'',
    type:'GET',
    success:function(result){
      //初始化用户信息
      console.log(username);
      processData(result);
      
    },
    error:function(){
      alert("System inner error!");
    }
  });
    });

function processData(result)
{
   //获取页面信息
    $.ajax({

            url: "",
            type: "POST",
            data: {},
            success: function (result) {
                var user, phone, id_type, id, email;
                
              
                for(var i = 0;i<result.data.length;i++)
                { 
                   if (i%2 == 0) { 
                    $(".tbody").append(
                        "<tr class=odd gradeX>"+
                            "<td>"+user+"</td>"+
                            "<td>"+phone+"</td>"+
                            "<td>"+id_type+"</td>"+
                            "<td>"+id_type+"</td>"+
                            "<td>"+email+"</td>"+    
                        "</tr>"
                      )
                    }
                    else{
                        $(".tbody").append(
                        "<tr class=odd gradeC>"+
                            "<td>"+user+"</td>"+
                            "<td>"+phone+"</td>"+
                            "<td>"+id_type+"</td>"+
                            "<td>"+id_type+"</td>"+
                            "<td>"+email+"</td>"+    
                        "</tr>"
                       )

                    }
                  
                }
               
                
            },
            error: function () {
                alert("System error!");
            }
        });
}