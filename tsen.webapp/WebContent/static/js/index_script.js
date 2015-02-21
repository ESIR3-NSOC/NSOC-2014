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

function getAbout(){
    $.get("./about", function(data,status) {
        $('#aboutContent').prepend(data);
    });   
};

getAbout();