document.addEventListener('DOMContentLoaded',addEvents,false);

function addEvents(){
  addButton();
}

function addButton(){
  var registButton = document.getElementById('registButton');
  registButton.addEventListener('click', onRegistButton, false);
}

function onRegistButton(){
  var userName = document.getElementById("name").value;
  var pass = document.getElementById("password").value;
  if(userName == "" || pass == ""){
    alert("ユーザ名とパスワードをそれぞれ10文字以内で入力してください");
    return;
  }
  var userInfoStr = userName + "," + pass;
  $.ajax({
      type: "POST",
      url: "/regist",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: userInfoStr,
    }).done(function (data) {
      console.log("done");
      console.log(data);
      if(data == "success"){
        alert("登録に成功しました。");
      }else if(data == "duplication"){
        alert("ユーザ名が重複しています。");
      }else if(data == "unknown"){
        alert("不明なエラーが発生しました");
      }
    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
      alert('不明なエラーです。');
    }).always(function () {
      console.log("always");
    });
}
