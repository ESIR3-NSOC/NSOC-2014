function getSettings(){
    $.get( "./user", { } )
        .done(function(data1) {
            $("div.title").empty().text($.parseJSON(data1).name);
            $.get( "./event", { datetime : "now" , userid : $.parseJSON(data1).id })
                .done(function(data2) {
                    $("div.title").append(" - ");
                    $("div.title").append($.parseJSON(data2).location);
                });
        });  
};

getSettings();
