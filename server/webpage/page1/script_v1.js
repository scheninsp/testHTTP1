function positionBox(){
    if (!document.getElementById) return false;
    if (!document.getElementById("moveobj")) return false;
	var elem = document.getElementById("moveobj");
	elem.style.position = "absolute";
	elem.style.left = "50px";
	elem.style.top = "100px";
	movement = setTimeout("moveBox()",5000);
}


function moveBox(){
    if (!document.getElementById) return false;
    if (!document.getElementById("moveobj")) return false;
	var elem = document.getElementById("moveobj");

	var xpos = parseInt(elem.style.left);
	var ypos = parseInt(elem.style.top);
	if(xpos == 200 && ypos == 100){
		return true;
	}
	
	if(xpos<200) { xpos++; }
    if(xpos>200) { xpos--; }
    if(ypos<100) { ypos++; }
    if(ypos>100) { ypos--; }
	
	elem.style.left = xpos + "px";
	elem.style.top = ypos + "px";
	movement = setTimeout("moveBox()",10);
}

function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      oldonload();
      func();
    }
  }
}
addLoadEvent(positionBox);