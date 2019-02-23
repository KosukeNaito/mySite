
document.addEventListener('DOMContentLoaded',addEvents,false);
var boardState;

var State = {
  EMPTY:0,
  BLACK:1,
  WHITE:2
};

$(function initBoard(){
  boardState = new Array(8);
  for(var i=0; i<8; i++){
    var row = new Array(8);
    for(var j=0; j<8; j++){
      row[j] = State.EMPTY;
    }
    boardState[i] = row;
  }
  console.log(boardState);
  boardState[3][3] = State.BLACK;
  boardState[3][4] = State.WHITE;
  boardState[4][3] = State.WHITE;
  boardState[4][4] = State.BLACK;
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

function updateBoard(){
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      if(boardState[i][j] == State.BLACK){
        var code = '<img src="assets/images/black.png" width="40" height="40"></img>';
        var id = 'b'+i.toString() + j.toString();
        var putPosition = document.getElementById(id);
        putPosition.innerHTML = code;
      }else if(boardState[i][j] == State.WHITE){
        var code = '<img src="assets/images/white.png" width="40" height="40"></img>';
        var id = 'b'+i.toString() + j.toString();
        var putPosition = document.getElementById(id);
        putPosition.innerHTML = code;
      }
    }
  }
}

function getCoordinateFromIndex(x, y){
  var board = document.getElementById('board');
  var rect = board.getBoundingClientRect();
  var coordinate = new Object();
  coordinate.x = x*50 + rect.left - window.pageXOffset;
  coordinate.y = y*50 + rect.top - window.pageYOffset;
  console.log(coordinate.x);
  console.log(coordinate.y);
  return coordinate;
}

function addEvents(){
  addImages();
}

function addImages(){
  var board = document.getElementById('board');
  board.addEventListener('click', sendCoordinate, false);
}

function sendCoordinate(e){
  var coordinate = getIndexFromCoordinate(e.x, e.y);
  var xy = coordinate.x.toString() + coordinate.y.toString();
  var boardStateStr = "";
  for(var i=0; i<8; i++){
    for(var j=0; j<8; j++){
      boardStateStr += boardState[i][j].toString();
    }
  }
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
      console.log("done");

    }).fail(function (jqXHR, statusText, errorThrown) {
      console.log("fail");
    }).always(function () {
      console.log("always");
    });
}

function getIndexFromCoordinate(x, y){
  var board = document.getElementById('board');
  var rect = board.getBoundingClientRect();
  var coordinate = new Object();
  coordinate.x = x - rect.left + window.pageXOffset;
  coordinate.y = y - rect.top + window.pageYOffset;
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
