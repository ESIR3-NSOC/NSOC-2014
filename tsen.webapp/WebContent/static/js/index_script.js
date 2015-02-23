$('[href="#popup"]').click(function(e){
    e.preventDefault();
    $("body").addClass("show-popup");
});

function closePopup(){
    $("body").removeClass("show-popup");
}

$("#close").click(function(){
    closePopup();
});

$(document).keyup(function(e) {
    if (e.keyCode == 27) {  
        closePopup();     
    }
});

function getRoom(){
    $.get( "./user", { } )
        .done(function(data1) {
            $("div.title").empty().text($.parseJSON(data1).name);
            $.get( "./event", { datetime : "now" , userid : $.parseJSON(data1).id })
                .done(function(data2) {
                    if (data2=="null")
                    {
                        $("#vote_box").remove()
                    }
                });
        });  
};

function getSettings(){
    $.get( "./admin", { } )
        .done(function(data1) {
            if (data1=="false")
            {
                $("#settings_box").remove()
            }
        });  
};

getRoom();
getSettings();