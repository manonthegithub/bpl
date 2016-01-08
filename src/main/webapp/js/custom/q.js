/**
 * Created by Админ on 10/18/2015.
 */
/*$.ajax({

    //Use the URL in Instagram Document
    url: 'https://instagram.com/irina_pozina?__a=1',
    dataType: "jsonp",
    jsonp: "callback",
    data: {
      /!*  q: "select title,abstract,url from search.news where query=\"cat\"",*!/
        format: "json"
    },

    //To get response
    success: function(result){

        //Exemple, if is okay or not
        if(result.meta.code == 200){
            var jsonObj = result;
        }else{
            alert('fail');
        }

    },
    error: function(result){
        console.log(result);
    }
})*/

$.ajax({
    url: 'https://instagram.com/irina_pozina?__a=1',
    dataType: 'JSON',
    jsonpCallback: 'callback',
    type: 'GET',
    success: function (data) {
        console.log(data);
    }
});