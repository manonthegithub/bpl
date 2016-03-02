

$('form').submit(
    function(){
        var form = $('form');
        var o = {};
        var list = form.serializeArray();
        $.each(list, function() {
          if (o[this.name] !== undefined) {
              if (!o[this.name].push) {
                  o[this.name] = [o[this.name]];
              }
              o[this.name].push(this.value || '');
          } else {
              o[this.name] = this.value || '';
          }
        });
        var fileInput = form.find('input[type=file]')[0];
        if(fileInput.files[0]){
            var reader = new FileReader();
            reader.readAsBinaryString(fileInput.files[0]);
            reader.onloadend = function () {
                o[fileInput.name] = btoa(reader.result);
                o['imageLink'] = fileInput.files[0].name;

                $.ajax({
                  type: "POST",
                  url: './api/adm/boxes/',
                  data: JSON.stringify(o),
                  success: function(){},
                  contentType : "application/json"
                });
            };
        }

        /*var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
        xmlhttp.open("POST", "/json-handler");
        xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xmlhttp.send(requestBody);*/

        /*$.ajax({
            type: "POST",
            url: $(this).attr('action'),
            contentType : 'application/json',
            async: true,
            data: requestBody,
            success: function () {
                alert("Thanks!");
            }
        });*/

        return false;

    }
);
