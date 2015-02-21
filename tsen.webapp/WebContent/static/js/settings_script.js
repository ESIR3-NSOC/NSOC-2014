$("#listProjectBtn").click(function() {
    $("#listProjectBtn").addClass("red");
    $("#listProjectIcon").addClass("blinking");
    $.post("./ade", {
            function: "list_project",
            cookie: $('#jsessionid input').val()
        })
        .done(function(data) {
            $("#listProjectIcon").removeClass("blinking");
            if ($.parseJSON(data).info == "All good") {
                $("#listProjectBtn").removeClass("red").addClass("green");
                $("#projectList").empty();
                $.get("./projects", function(data, status) {
                    $.each($.parseJSON(data), function(key, project) {
                        var paperItem = $('<option>').text(project["name"]).attr({
                            value: project['id']
                        });
                        $("#projectList").append($(paperItem));
                    });
                });
            } else {
                alert($.parseJSON(data).info);
            }
        });
});

$("#projectList").change(function () {
    
     $.post("./project", {
            id: $( "#projectList option:selected" ).attr("value")
        })
        .done(function(data) {
            //alert($.parseJSON(data).name);
     });
});

$("#treeExtractionBtn").click(function() {
    $("#treeExtractionBtn").addClass("red");
    $("#treeExtractionIcon").addClass("blinking");
    $.post("./ade", {
            function: "browse",
            cookie: $('#jsessionid input').val()
        })
        .done(function(data) {
            $("#treeExtractionIcon").removeClass("blinking");
            if ($.parseJSON(data).info == "All good") {
                $("#treeExtractionBtn").removeClass("red").addClass("green");
            } else {
                alert($.parseJSON(data).info);
            }
        });
});