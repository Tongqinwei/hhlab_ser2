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
//信息： 图书编号 ISBN13 图书名 作者 馆藏地 藏书量 

var username;
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
            data: {username: username},
            success: function (result) {
                
                //打出每个时间段的大表格
                for(var i = 0;i<result.data.length;i++)
                {
                    var unid,isbn13,title,author, place, used,total;
                    var date;

                    if (i%2==0) {
                    $(".tbody").append(
                        "<tr class=odd gradeX>"+
                            "<td>"+unid+"</td>"+
                            "<td>"+isbn13+"</td>"+
                            "<td>"+title+"</td>"+
                            "<td>"+author+"</td>"+
                            "<td>"+place+"</td>"+
                            "<td>"+used+"/"+total+"</td>"+
                        "</tr>"
                       )
                    }
                    else{
                      $(".tbody").append(
                        "<tr class=odd gradeC>"+
                            "<td>"+unid+"</td>"+
                            "<td>"+isbn13+"</td>"+
                            "<td>"+title+"</td>"+
                            "<td>"+author+"</td>"+
                            "<td>"+place+"</td>"+
                            "<td>"+used+"/"+total+"</td>"+
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