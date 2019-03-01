document.addEventListener('DOMContentLoaded',addEventsAtLogin,false);

function addEventsAtLogin(){
  addLink();
}

function addLink(){
  var shirokuroButton = document.getElementById('shirokuro');
  shirokuroButton.addEventListener('click', shirokuroLoginConfirm, false);
  var numplaButton = document.getElementById('numpla');
  numplaButton.addEventListener('click', numplaLoginConfirm, false);
}

function makeSendMessage(userName, pass){
  var userName = window.prompt("ユーザー名を入力してください。","");
  var pass = window.prompt("パスワードを入力してください。","");

  if(userName == null || pass == null){
    return "";
  }else if(userName == "" || pass == ""){
    alert("ユーザー名とパスワードをそれぞれ10文字以内で入力してください");
    return "";
  }
  return userName + "," + pass;
}

function shirokuroLoginConfirm(){
  var userInfoStr = makeSendMessage();
  if(userInfoStr==""){
    return;
  }
  $.ajax({
      type: "POST",
      url: "/login",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: userInfoStr,
    }).done(function (data) {
      console.log("done");
      console.log(data);
      if(data == "true"){
        location.href = '\\shirokuro';
        //window.location.href = '\\shirokuro';
      }else if(data == "false"){
        alert("ユーザー名もしくはパスワードが違います。");
      }
    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
      alert('不明なエラーです。');
    }).always(function () {
      console.log("always");
    });
}

function numplaLoginConfirm(){
  var userInfoStr = makeSendMessage();
  $.ajax({
      type: "POST",
      url: "/login",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: userInfoStr,
    }).done(function (data) {
      console.log("done");
      console.log(data);
      if(data == "true"){
        location.href = '\\numpla';
        //window.location.href = '\\numpla';
      }else if(data == "false"){
        alert("ユーザー名もしくはパスワードが違います。");
      }
    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
      alert('不明なエラーです。');
    }).always(function () {
      console.log("always");
    });
}
