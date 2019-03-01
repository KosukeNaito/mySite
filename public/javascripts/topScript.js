document.addEventListener('DOMContentLoaded',addEventsAtTop,false);

function addEventsAtTop(){
  addLinkAtTop();
}

function addLinkAtTop(){
  var shirokuroButton2 = document.getElementById('shirokuro2');
  shirokuroButton2.addEventListener('click', shirokuroLoginConfirm, false);
  var numplaButton2 = document.getElementById('numpla2');
  numplaButton2.addEventListener('click', numplaLoginConfirm, false);
}
