$.fn.getAbsLocation=function() {    
    var tempArr=[];  
    this.each(function(){  
        var elmt = this;     
        var offsetTop = elmt.offsetTop;     
        var offsetLeft = elmt.offsetLeft;     
        var offsetHeight = elmt.offsetHeight;   
        var offsetWidth=elmt.offsetWidth;  
        while (elmt = elmt.offsetParent) {     
            if (elmt.style.position == 'absolute'|| elmt.style.position == 'relative'||(elmt.style.overflow != 'visible' && elmt.style.overflow != '')) {     
               break;     
            }     
             offsetTop += elmt.offsetTop;     
             offsetLeft += elmt.offsetLeft;     
        }  
        tempArr[tempArr.length]={ Top: offsetTop, Left: offsetLeft, Width: offsetWidth, Height: offsetHeight };     
    });  
    return tempArr;  
}