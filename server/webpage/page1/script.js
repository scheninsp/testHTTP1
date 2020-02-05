
function positionBox(){
	var div = document.getElementById("moveobj");
	div.style.width = "100px";
	div.style.height = "100px";
	div.style.position = "absolute";
	div.style.top = "200px";  
	div.style.left = "200px";
	
	var box1 = document.getElementById("box1");
	
	document.onkeydown = function(e) {
		var speed = 10;
		switch(e.which){
			case 38:
			  div.style.top = parseInt(div.style.top) - speed + "px";
			  box1.style.backgroundImage="url(./mario3.gif)";
			  break;
			case 40:
				div.style.top = parseInt(div.style.top) + speed + "px";
				box1.style.backgroundImage="url(./mario3.gif)";
				break;
			case 37:
				div.style.left = parseInt(div.style.left) - speed + "px";
				box1.style.backgroundImage="url(./mario3.gif)";
				break;
			case 39:
				div.style.left = parseInt(div.style.left) + speed + "px";
				box1.style.backgroundImage="url(./mario3.gif)";
				break;
		}
	}

	document.onkeyup = function(ev){
		var oEvent = ev || event;
		var keyCode = oEvent.keyCode;
		switch(keyCode){
			case 37:
				left=false;
				box1.style.backgroundImage="url(./mario2.gif)";
				break;
			case 38:
				top=false;
				box1.style.backgroundImage="url(./mario2.gif)";
				break;
			case 39:
				right=false;
				box1.style.backgroundImage="url(./mario2.gif)";
				break;
			case 40:
				bottom=false;
				box1.style.backgroundImage="url(./mario2.gif)";
				break;
		}
	}	
}

//引用来自其他文件的js时必须加入下面这些代码并把上面改写为函数
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