var eventHandler = function(event) {
    var type = event.target.nodeName.toLowerCase();
    if (type == 'input' || type == 'textarea') {
        var rect = event.target.getBoundingClientRect();
        var scrollY = android.calculateVerticalScroll(rect.top + rect.height);
        document.body.style.marginBottom="400px";
        if (scrollY > 0) {
            window.scrollBy(0, scrollY);
        }
    } else{
        document.body.style.marginBottom="0px";
    }
};
document.body.addEventListener('focus', eventHandler, true);
document.body.addEventListener('click', eventHandler, true);

