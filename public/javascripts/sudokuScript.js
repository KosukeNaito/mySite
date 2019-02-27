
document.addEventListener('DOMContentLoaded',addEvents,false);

function addEvents(){
  addButton();
  addInputs();
}


function addButton(){
  var sendButton = document.getElementById('sendButton');
  sendButton.addEventListener('click', onSendButton, false);
  var clearButton = document.getElementById('clearButton');
  clearButton.addEventListener('click', onClearButton, false);
}

function addInputs(){
  var inputs = document.getElementsByName('square');
  document.addEventListener('keydown', moveInputs, false);
}

function moveInputs(e){
  var nowFocusedId = document.activeElement.id;
  var row = parseInt(nowFocusedId.charAt(1), 10);
  var col = parseInt(nowFocusedId.charAt(2), 10);
  if(Number.isNaN(row) || Number.isNaN(col)){
    return;
  }

  if(e.keyCode == 37){//左
    if(col != 0){
      col--;
    }else{
      if(row != 0){
        row--;
        col = 8;
      }
    }
  }else if(e.keyCode == 38){//上
    if(row != 0){
        row--;
    }else{
      if(col != 0){
        col--;
        row = 8;
      }
    }
  }else if(e.keyCode == 39){//右
    if(col != 8){
      col++;
    }else{
      if(row != 8){
        row++;
        col = 0;
      }
    }
  }else if(e.keyCode == 40 || e.keyCode == 13){//下,エンター
    if(row != 8){
      row++;
    }else{
      if(col != 8){
        col++;
        row = 0;
      }
    }
  }

  document.getElementById("s" + row.toString() + col.toString()).focus();
}

function onSendButton(){
  ajaxSetUp();
}

function onClearButton(){
  for(var i=0; i<9; i++){
    for(var j=0; j<9; j++){
      document.getElementById("s" + i.toString() + j.toString()).value = "";
    }
  }
}

/*
9x9のボードに入力された値をString型の二次元配列に収めて返す
*/
function boardSetUp(){
  var board = new Array();
  for(var i=0; i<9; i++){
    var row = new Array();
    for(var j=0; j<9; j++){
      var masuValue = document.getElementById("s" + i.toString() + j.toString()).value;
      row.push(inputStringConverter(masuValue));
    }
    board.push(row);
  }
  return board;
}

function inputStringConverter(inputString){
  switch (inputString) {
    case "1":
    case "１":
    case "一":
      return 1;
      break;
    case "2":
    case "２":
    case "二":
      return 2;
      break;
    case "3":
    case "３":
    case "三":
      return 3;
      break;
    case "4":
    case "４":
    case "四":
      return 4;
      break;
    case "5":
    case "５":
    case "五":
      return 5;
      break;
    case "6":
    case "６":
    case "六":
      return 6;
      break;
    case "7":
    case "７":
    case "七":
      return 7;
      break;
    case "8":
    case "８":
    case "八":
      return 8;
      break;
    case "9":
    case "９":
    case "九":
      return 9;
      break;
    default:
      return 0;
  }
}


/*
ajaxによる通信を行う
9x9のボードの情報をJSONの形にしサーバに送る
*/
function ajaxSetUp(){
  var boardJson = JSON.stringify(boardSetUp(),undefined,1);
  console.log(boardJson);
  $.ajax({
      type: "POST",
      url: "/run",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: boardJson,
    }).done(function (data) {
      console.log("done");
      if(data == "miss"){
        alert('問題が間違ってます！');
      }else if(data == "muri"){
        alert('解けませんでした...')
      }else if(data.length==81){
        console.log(data);
        dispAnswer(data);
      }
      return data;
    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
    }).always(function () {
      console.log("always");
    });
}

function dispAnswer(str){
  var count = 0;
  for(var i=0; i<9; i++){
    for(var j=0; j<9; j++){
      if(str.charAt(count)<='9' && str.charAt(count) >= '1'){
        document.getElementById("s" + i.toString() + j.toString()).value = str.charAt(count);
        count++;
      }
    }
  }
}
