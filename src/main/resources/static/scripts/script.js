$(function(){

/*  
    $(window).resize(function() {
        //alert($(window).height() + " " + $(window).width())
    });
*/
    $(".data-exib").each(function (idx, elem) {
        
        if ($(elem).is(":input")) {
            
            if ($(elem).val() !== "" ) $(elem).val($.format.toBrowserTimeZone($(elem).val(), 'E, dd/MM/yyyy HH:mm'));

        } else {

            if ($(elem).text() !== "" ) $(elem).text($.format.toBrowserTimeZone($(elem).text(), 'E, dd/MM/yyyy HH:mm'));

        }
    });
    //<script>document.write($.format.date("<%= detalhe[i].datacriacao %>", "dd/MM/yyyy HH:mm"))</script>

    $('body').on('click','input[type="button"]', (function(event){
        
        let id = $(this).attr('id');
        let formData= $('form').serializeJSON();
        
        alert(JSON.stringify(formData,null,4));
        $('#' + id).prop('disabled', true); // AVOID MULTIPLE SUBMITS
        event.preventDefault();
        $.ajax({
            method: "post",
            contentType: "application/json",
            url: "login",
            dataType: 'json',
            data: JSON.stringify(formData)
        })

        .done(function(msg){

            alert("Sucesso!")
            alert(msg);
            $('#' + id).prop('disabled', false);
            $('form').submit();
            
        })

        .fail(function(err){
    
            alert("Ocorreu um erro, favor tente novamente")
            //alert($('#'+$(this).attr('id')));
            $('#'+$(this).attr('id')).prop('disabled', false);
            console.log(Date.now()+JSON.stringify(err,null,4))
        })

    }))
    /*
    $('body').on('click','input[type="button"]', (function(event){
 
        let id = $(this).attr('id').slice(3);
        let op = $(this).attr('id').slice(0, 3);
        let formData= $('#tr'+id+' :input').serializeArray();
        switch (op){ 

            case "sub":

                $('#' + op + id).prop('disabled', true); // AVOID MULTIPLE SUBMITS
                event.preventDefault();
                $.ajax({
                    method: "post",
                    url: "/approve",
                    data: formData
                })
                .done(function( msg ) {
                    
                    $('#dataapr' + id).val($.format.toBrowserTimeZone(msg.dataaprov,'E, dd/MM/yyyy HH:mm'));
                    $('#aprov' + id).val(msg.nomeaprov);
                    
                    if (msg.status == 'APROVADO') { 

                        $('#btn-d' + id).replaceWith('<span id=btn-d'+ msg.id +'><input class="btn btn-primary" type="button" id="dow'+ msg.id +'" value="Download"></span>')

                    } else {

                        $('#btn-d' + id).replaceWith('<span id=btn-d'+ msg.id +'></span>')

                    }
                    alert("Orcamento: " + id + " atualizado com sucesso!")
                    $('#' + op + id).prop('disabled', false);
                })
    
                .fail(function(err){
    
                    alert("Ocorreu um erro, favor tente novamente")
                    $('#sub' + id).prop('disabled', false);
                    console.log(Date.now()+JSON.stringify(err,null,4))
                })
                break;

            case "dow":

                $('#' + op + id).prop('disabled', true); // AVOID MULTIPLE SUBMITS
                event.preventDefault();
                $.ajax({
                    method: "post",
                    url: "/download",
                    data: formData
                })
                .done(function() {
                    
                    alert("Download vai começar")
                    window.open('/public/pdf/'+id+'.pdf','_blank')
                    $('#' + op + id).prop('disabled', false);
                })
    
                .fail(function(err){
    
                    alert("Ocorreu um erro, favor tente novamente")
                    $('#dow' + id).prop('disabled', false);
                    console.log(Date.now()+JSON.stringify(err,null,4))
                })
                break;

            case "res":

                if (confirm("Deseja resetar a senha?")) {

                    $('#' + op + id).prop('disabled', true); // AVOID MULTIPLE SUBMITS
                    event.preventDefault();

                    $.ajax({
                        method: "post",
                        url: "/reset",
                        data: formData
                    })
                    .done(function() {
                        
                        alert("Reset de senha solicitado!")
                        $('#' + op + id).prop('disabled', false);
                    })
        
                    .fail(function(err){
        
                        alert("Ocorreu um erro, favor tente novamente")
                        $('#dow' + id).prop('disabled', false);
                        console.log(Date.now()+JSON.stringify(err,null,4))
                    })                    
                    
                } 
                break;

        }
        
    }))
*/
    $('#valor').add('#desconto').blur(function(){

        let valor = $('#valor').val();
        let desconto = $('#desconto').val();
        $('#vFinal').val(Math.round(valor*(1- desconto/100)));
    })

    $('#desconto').blur(function(){

        if ($('#desconto').val() > 100){
            alert("Insira um valor menor que 100%!");
            $('#desconto').val('0');
        }
    })

    $('input').on('input', function(){

        $(this).next().removeClass('errorShow').addClass("error");
        $(this).removeClass('invalid').addClass("valid");
    })

    if (typeof(data) !== 'undefined') {
        //console.log(JSON.stringify(data,null,4))
        if (('cliente' in data)){
            //console.log(JSON.stringify(data,null,4))
            for (let i=0; i < data.cliente.rowCount; i++) {
                // <option value="<%= montaData.cliente[i].nomeCliente %>">
                $('#nomeList').append("<option value='" + data.cliente.rows[i].nomecliente + "'")

            }
        }

        if (('equip' in data)){
            for (let i=0; i < data.equip.rowCount; i++) {

                $('#equipList').append("<option value='" + data.equip.rows[i].nomeequip + "'")

            }
        }
    }

    var full;
    $("#nomeCliente").blur(function(){

        let nomeCliente= $("#nomeCliente").val().toUpperCase(); 
        $("#nomeCliente").val($("#nomeCliente").val().toUpperCase());
        //let idCliente = $("#nomeList").find('option[value="' + nomeCliente + '"]').attr('id');
        
        if (!nomeCliente) { 
            limpaCliente();
            return;
        }

        fetch("/formFill/cnpj/" + nomeCliente)

        .then(function(res){

            return res.json()
        })

        .then(function(res){
            
            cnpj.value = res.rows[0].cnpj;
            cnpj.readOnly = true;
            nomeCompleto.value = res.rows[0].nomecompleto;
            nomeCompleto.readOnly = true;
            responsavel.value = res.rows[0].responsavel
            $('#respList option').remove();
            for (let i=0; i< res.rowCount; i++){
                $('#respList').append("<option value='" + res.rows[i].responsavel + "'")
            }
            departamento.value = res.rows[0].departamento;

            full = res;
            
            return full;
            
        })

        .catch(function() {

            limpaCliente();

        })

    });


    $("#nomeEquip").blur(function(){

        let nomeEquip= $("#nomeEquip").val().toUpperCase();    
        $("#nomeEquip").val($("#nomeEquip").val().toUpperCase())  

        if (!nomeEquip) { 
            limpaEquip();
            return;
        }

        fetch("/formFill/serialNumber/" + nomeEquip)

        .then(function(res){

            return res.json()
        })

        .then(function(res){

            $('#serialList option').remove();
            for (let i=0; i< res.rowCount; i++){
                $('#serialList').append("<option value='" + res.rows[i].serialnumber + "'")
            }
            full = res;
            
            return full;
            
        })

        .catch(function() {

            limpaEquip();

        })

    });

    // SUBMIT ON ENTER
    $('input').keypress(function(e) {
        if(e.which == 13) {
            sendForm();
        }
    });
})

function limpaCliente(){

    cnpj.value = "";
    cnpj.readOnly = false;
    nomeCompleto.value = "";
    nomeCompleto.readOnly = false;
    responsavel.value = "";
    $('#respList option').remove();
    departamento.value = "";

}

function limpaEquip(){

    nomeEquip.value = "";
    serialNumber.value = "";
    $('#serialList option').remove();

}


// DESNECESSARIO
//function sendForm(){
//
//    let formData = $('#form').serializeArray();
//    let errorFree = true;
//
//    for (let input in formData){
//        
//        if (!$('input[name='+formData[input].login+']').val()){
//            $('input[name='+formData[input].login+']').next().removeClass('error').addClass("errorShow");
//            $('input[name='+formData[input].login+']').removeClass('valid').addClass("invalid");
//            errorFree = false;
//        }
//    }
//
//    if (errorFree){
//        
//        $("#send").prop('disabled', true);
//        $("#form").submit();
//    }
//}