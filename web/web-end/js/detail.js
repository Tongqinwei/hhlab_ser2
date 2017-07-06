// <tr>
// <td>1</td>
// <td>Mark</td>
// <td>Otto</td>
// <td>@mdo</td>
// </tr>


//信息： 订单编号 相关用户 创建时间 状态 详情（封面，书名 ，作者， 编号， 待定 预计还书时间和 实际还书时间）

var order_num;
$(function () {

	applyId = GetQueryString(order_num);

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
            	var user , establish_time, state, details;
            	var img, title, unid. author；
            	$("#order_num").val(order_num);
            	$("#establish_time").val(establish_time);
            	$("#user").val(user);
            	$("#state").val(state);
                
                //打出每个时间段的大表格
                for(var i = 0;i<result.data.length;i++)
                {
                    var order_num,user,establish_time;
                    var date;
                    $(".tbody").append(
                        "<tr class=odd gradeX>"+
                            "<td>"+order_num+"</td>"+
                            "<td>"+user+"</td>"+
                            "<td>"+title+"</td>"+
                            "<td>"+establish_time+"</td>"+
                            "<a href= detail.html?order_num="+order_num+"><td>查看详情 </td></a>"+    
                       "</tr>"
                       )
                  
                }
               
                
            },
            error: function () {
                alert("System error!");
            }
        });
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return (r[2]); return null;
}