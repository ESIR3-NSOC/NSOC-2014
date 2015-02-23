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

function resetDShapeColor(){
    $("#too_hot span").css({ background: "#953f00" });
    $("#hot span").css({ background: "#953f00" });
    $("#cold span").css({ background: "#953f00" });
    $("#too_cold span").css({ background: "#953f00" });
};


$("#too_hot").click(function() {
    $.post("./vote", {
            rate: "++"
        })
        .done(function(data) {
            resetDShapeColor();
            $("#too_hot span").css({ background: "#07b500" });
        });
})
$("#hot").click(function() {
    $.post("./vote", {
            rate: "+"
        })
        .done(function(data) {
            resetDShapeColor();
            $("#hot span").css({ background: "#07b500" });
        });
})
$("#cold").click(function() {
    $.post("./vote", {
            rate: "-"
        })
        .done(function(data) {
            resetDShapeColor();
            $("#cold span").css({ background: "#07b500" });
        });
})
$("#too_cold").click(function() {
    $.post("./vote", {
            rate: "--"
        })
        .done(function(data) {
            resetDShapeColor();
            $("#too_cold span").css({ background: "#07b500" });
        });
});

getSettings();
