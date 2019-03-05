
document.addEventListener('DOMContentLoaded',addEvents,false);
var boardState;
var playerTurn = true;
var boardStream = new Array();

var BoardState = {
  EMPTY:0,
  BLACK:1,
  WHITE:2
};

function addEvents(){
  addImages();
  addButton();
}

function addImages(){
  var board = document.getElementById('board');
  board.addEventListener('click', sendCoordinatePlayer, false);
}

function addButton(){
  var comButton = document.getElementById('comButton');
  comButton.addEventListener('click', onComButton, false);
  var backButton = document.getElementById('backButton');
  backButton.addEventListener('click', onBackButton, false);
  var restartButton = document.getElementById('restartButton');
  restartButton.addEventListener('click', onRestartButton, false);
}


$(function initBoard(){
  boardState = new Array(8);
  for(var i=0; i<8; i++){
    var row = new Array(8);
    for(var j=0; j<8; j++){
      row[j] = BoardState.EMPTY;
    }
    boardState[i] = row;
  }
  boardState[3][3] = BoardState.BLACK;
  boardState[3][4] = BoardState.WHITE;
  boardState[4][3] = BoardState.WHITE;
  boardState[4][4] = BoardState.BLACK;
  addStream(BoardState.BLACK);
  var board = document.getElementById('board');
  var rect = board.getBoundingClientRect();
  var coordinate = new Object();
  var allDiv = "";
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      coordinate.x = i*50 + rect.left - window.pageXOffset + 5;
      coordinate.y = j*50 + rect.top - window.pageYOffset + 5;
      var oneDiv = '<div id="b' + i.toString() + j.toString() +'" style="position:absolute; top:'+coordinate.y.toString()+'px; left:'+ coordinate.x.toString()+'px;"></div>'
      allDiv = allDiv + oneDiv;
    }
  }
  var putPosition = document.getElementById('stone');
  putPosition.innerHTML = allDiv;
  updateBoard();
});

/**
* htmlを書き換えることで画像の表示を変更しボードを描画する
*/
function updateBoard(){
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      if(boardState[i][j] == BoardState.BLACK){
        var code = '<img src="assets/images/black.png" width="40" height="40"></img>';
        var id = 'b'+i.toString() + j.toString();
        var putPosition = document.getElementById(id);
        putPosition.innerHTML = code;
      }else if(boardState[i][j] == BoardState.WHITE){
        var code = '<img src="assets/images/white.png" width="40" height="40"></img>';
        var id = 'b'+i.toString() + j.toString();
        var putPosition = document.getElementById(id);
        putPosition.innerHTML = code;
      }else{
        var code = '';
        var id = 'b'+i.toString() + j.toString();
        var putPosition = document.getElementById(id);
        putPosition.innerHTML = code;
      }
    }
  }
  updateStoneCount();
}

/**
* 黒石白石の数をそれぞれ数えて描画する
*/
function updateStoneCount(){
  var blackStoneNum = 0;
  var whiteStoneNum = 0;
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      if(boardState[i][j] == BoardState.BLACK){
        blackStoneNum++;
      }else if(boardState[i][j] == BoardState.WHITE){
        whiteStoneNum++;
      }
    }
  }
  document.getElementById("blackStoneNum").innerHTML = blackStoneNum.toString();
  document.getElementById("whiteStoneNum").innerHTML = whiteStoneNum.toString();
}

/**
* インデックス情報を元に座標情報に変更し返す
*/
function getCoordinateFromIndex(x, y){
  var board = document.getElementById('board');
  var rect = board.getBoundingClientRect();
  var coordinate = new Object();
  coordinate.x = x*50 + rect.left - getX();//window.pageXOffset;
  coordinate.y = y*50 + rect.top - getY();//window.pageYOffset;
  //console.log(coordinate.x);
  //console.log(coordinate.y);
  return coordinate;
}

function getX() {
    var scroll;
    if(window.pageXOffset != undefined){
      scroll = window.pageXOffset;
    }else{
      scroll = document.documentElement.scrollLeft;
    }
    return scroll;
}

/**
*
**/
function getY() {
    var scroll;
    if(window.pageYOffset != undefined){
      scroll = window.pageYOffset;
    }else{
      scroll = document.documentElement.scrollTop;
    }
    return scroll;
}

function addStream(turn){
  var firstStream = new Object();
  firstStream.turn = turn;
  firstStream.board = $.extend(true, {}, boardState);
  boardStream.push(firstStream);
}

function onBackButton(){
  if(boardStream.length >= 2){
      boardState = $.extend(true, {}, boardStream[boardStream.length-2].board);
    if(boardStream[boardStream.length-2].turn == BoardState.BLACK){
      playerTurn = true;
    }else{
      playerTurn = false;
    }
    boardStream.pop();
    updateBoard();
    console.log("back");
    return;
  }
  console.log("cantBack");
}

function onComButton(){
  playerTurn = false;
  var boardStateStr = getBoardStateStr();
  $.ajax({
      type: "POST",
      url: "/com",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: boardStateStr,
    }).done(function (data) {
      console.log("done");
      console.log(data);
      if(data.length == 64){
        var count = 0;
        for(var i=0; i<8; i++){
          for(var j=0; j<8; j++){
            boardState[i][j] = parseInt(data.charAt(count));
            count++;
          }
        }
        addStream(BoardState.BLACK);
        updateBoard();
      }else if(data == "cantPut"){
        alert("置けませんでした。あなたのターンです");
        console.log("cantPut");
      }
      playerTurn = true;
    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
      alert('不明なエラーです。');
    }).always(function () {
      console.log("always");
    });
}

function onRestartButton(){
  location.href = '\\shirokuro';
}

function getBoardStateStr(){
  var boardStateStr = "";
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      boardStateStr += boardState[i][j].toString();
    }
  }
  return boardStateStr;
}

function sendCoordinatePlayer(e){
  if(!playerTurn){
    alert("COMのターンです。COMボタンを押してください。")
    console.log("not player turn");
    return;
  }
  var coordinate = getIndexFromCoordinate(e.x, e.y);
  if(boardState[coordinate.x][coordinate.y] != BoardState.EMPTY){
    console.log("stone is already put");
    return;
  }
  var xy = coordinate.x.toString() + coordinate.y.toString();
  var boardStateStr = getBoardStateStr();
  var sendStr = xy + "," + boardStateStr;
  $.ajax({
      type: "POST",
      url: "/put",
      dataType: "text",
      contentType:"text/plain",
      cache: false,
      async: false,
      data: sendStr,
    }).done(function (data) {
      //console.log("done");
      //console.log(data);
      if(data.length == 64){
        playerTurn = false;
        var count = 0;
        for(var i=0; i<8; i++){
          for(var j=0; j<8; j++){
            boardState[i][j] = parseInt(data.charAt(count));
            count++;
          }
        }
        addStream(BoardState.WHITE);
        updateBoard();
      }else if(data == "cantPut"){
        //console.log("cantPut");
        return;
      }
    }).fail(function (jqXHR, statusText, errorThrown) {
      //console.log("fail");
      alert('不明なエラーです。');
    }).always(function () {
      //console.log("always");
    });
}

function getIndexFromCoordinate(x, y){
  var board = document.getElementById('board');
  var rect = board.getBoundingClientRect();
  var coordinate = new Object();
  coordinate.x = x - rect.left - getX();//window.pageXOffset;
  coordinate.y = y - rect.top - getY();//window.pageYOffset;
  console.log(coordinate.x);
  console.log(coordinate.y);
  var isXdecided = true;
  var isYdecided = true;
  var index = new Object();
  for(var i=0; i<8; i++){
    if(coordinate.x >= 50*i && coordinate.x <= 50*(i+1) && isXdecided){
      index.x = i;
      isXdecided = false;
    }
    if(coordinate.y >= 50*i && coordinate.y <= 50*(i+1) && isYdecided){
      index.y = i;
      isYdecided = false;
    }
  }
  console.log(index.x);
  console.log(index.y);
  return index;
}
