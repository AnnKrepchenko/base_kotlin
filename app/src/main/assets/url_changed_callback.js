var oldHref = window.location.href;
this.Check;
var that = this;



this.Check = setInterval(function(){ detect() }, 100);

var detect = function(){
    if(that.oldHref!=window.location.href){
        that.oldHref = window.location.href;
        android.onUrlChange(that.oldHref);
    }
};


window.addEventListener("hashchange", function(){
    window.android.onUrlChange(window.location.href);
});
