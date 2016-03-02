$(document).ready(
    function(){
        var canv =  document.createElement("canvas");
        var pattern = Trianglify({
            width:  $( document ).height(),
            height: $( document ).width()
        });
        pattern.canvas(canv);
        var url = canv.toDataURL('image/jpeg');
        document.body.style.background = 'url(' + url + ')'+ ' no-repeat center center fixed';
    }
)