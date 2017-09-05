//信息：封面  ISBN13 图书名 作者 藏书量 操作
var session;
$(function () {
  session = sessionStorage.getItem('session');//获取session_id;
    $.ajax({
    url:"/hhlab/admin/showAllBooks",
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


   //处理页面信息
    for(var i = 0;i<result.length;i++)
    {
          var unid,isbn13,title,author, used,total, img;
          isbn13 = result[i].isbn13;
          title = result[i].title;
          author = result[i].author;
          img = result[i].image;
          used = result[i].storage_cb;
          total = result[i].storage;



            if (i%2==0) {
                   $(".tbody").append(
                     "<tr class=odd gradeX>"+
                            "<td><image style='max-height:120px;' src='"+img+"'/></td>"+
                            "<td>"+isbn13+"</td>"+
                            "<td>"+title+"</td>"+
                            "<td>"+author+"</td>"+
                            "<td>"+used+"/"+total+"</td>"+
                            "<td><a href='book_Detail.html?unid=null&isbn="+isbn13+"'><button>查看详情</button></a></td>"+
                        "</tr>"
                       );
                     
                    }
                    else{
                      $(".tbody").append(
                       "<tr class=odd gradeX>"+
                            "<td><image style='max-height:120px;' src='"+img+"'/></td>"+
                            "<td>"+isbn13+"</td>"+
                            "<td>"+title+"</td>"+
                            "<td>"+author+"</td>"+
                            "<td>"+used+"/"+total+"</td>"+
                            "<td><a href='book_Detail.html?unid=null&isbn="+isbn13+"'><button>查看详情</button></a></td>"+
                        "</tr>"
                       )
                    }
                  
                }
               
}

function search(){

  var inputValue = $("#search").val();
  console.log(inputValue);
  var url = "/hhlab/search_book?key="+encodeURI(inputValue);
  console.log(url);
  $.ajax({
    url : url ,
    type : 'GET',
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
