$(document).ready(function(){
   var $form = $('form');
   $form.submit(
    function(){

      var btn = $('.btn');
      btn.prop('disabled',true);
      $("#spinner").show();

      $.post($(this).attr('action'), $(this).serialize(), function(response){
            $("#submit-result").text("Ваше сообщение было успешно отправлено");
            $('input').val("");
            $('textarea').val("");
      })
        .fail(function() {
            $("#submit-result").text("Что-то пошло не так, попробуйте отправить сообщение позднее");
        })
        .always(function(){
            $("#spinner").hide();
             $("#submit-result").show().delay(3000).fadeOut();
            setTimeout(function(){
                  btn.prop('disabled', false);
              }, 3000);
        });

        return false;
    }
   );
});