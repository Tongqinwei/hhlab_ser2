var session;
$(function () {
session = sessionStorage.getItem('session');//获取session_id;
// var file = $("#exampleInputFile")
//     $.ajax({
//     url:"/",
//     type:'POST',
//     data: JSON.stringify(
//               {session_id : session,
//              }),
//     success:function(result){
//       //初始化用户信息
//      console.log(result);
//      processData(result);
      
//     },
//     error:function(){
//       alert("System inner error!");
//     }
//   });

});

function upFile(){
  var file = $("#exampleInputFile").files;
  console.log(file);
  alert("文件上传成功");
}

function getFile(file){
  var File = file;
}