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
// var2:   orderid
// var3:   session_id
// var4:   tel 用户电话
//信息： 订单编号 相关用户 创建时间 状态操作

var session;
var search_type = 'orderid';
$(function () {
  session = sessionStorage.getItem('session');//获取session_id;
});

function processData(result)
{
    if(result.length == 0){
          $(".tbody").empty();//当前查询没有结果
           $(".tbody").append("当前数据库没有相关数据");
       }

                //打出每个时间段的大表格
                for(var i = 0;i<result.length;i++)
                {
                    var order_num,user,establish_time,order_state;
                    var text;
                    order_num = result[i].orderid;
                    user = result[i].unionid;
                    establish_time = result[i].ordertime;
                    order_state = result[i].orderstate;
                    switch(order_state){
                        case 1: text = "未完成";break;
                        case 2: text = "已确认";break;
                        case 3: text = "已付款";break;
                        case 4: text = "已完成";break;
                        case 5: text = "已失效";break;
                    }
                    if (i%2==0){
                    $(".tbody").append(
                        "<tr class=odd gradeX>"+
                            "<td>"+order_num+"</td>"+
                            "<td>"+user+"</td>"+
                            "<td>"+establish_time+"</td>"+
                            "<td>"+text+"</td>"+
                            "<td><a href='detail.html?order_num="+order_num+"'><button>查看详情</button></a></td>"+
                        "</tr>"
                       )
                    }
                    else{
                       $(".tbody").append(
                        "<tr class=odd gradeC>"+
                            "<td>"+order_num+"</td>"+
                            "<td>"+user+"</td>"+
                            "<td>"+establish_time+"</td>"+
                            "<td>"+text+"</td>"+
                             "<td><a href='detail.html?order_num="+order_num+"'><button>查看详情</button></a></td>"+
                        "</tr>"
                      )
                    }

                }

}

function selected(event)
{
   console.log(event.value);
   search_type = event.value;

}


function search(){
  var type ;
  var inputValue = $("#search").val();
  console.log(inputValue);

  var url = "/hhlab/showOrderForm";
  if (search_type == 'tel') {
   $.ajax({
    url : url ,
    type : 'POST',
    data :JSON.stringify({
        mode : "3",
        session_id : session,
        tel : inputValue,

    }),
    success:function(result){
      //初始化用户信息
      console.log(result);
       $(".tbody").empty();
     processData(result);

    },
    error:function(){
      alert("System inner error!");
    }
  });

  }
  else{
     $.ajax({
    url : url ,
    type : 'POST',
    data :JSON.stringify({
        mode : "1",
        session_id : session,
        orderid : inputValue,

    }),
    success:function(result){
      //初始化用户信息
      console.log(result);
       $(".tbody").empty();
     processData(result);

    },
    error:function(){
      alert("System inner error!");
    }
  });

  }


}
