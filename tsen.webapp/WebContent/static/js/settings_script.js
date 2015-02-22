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

$("#extractionPlanningsBtn").click(function() {
    $("#extractionPlanningsBtn").addClass("red");
    $("#extractionPlanningsIcon").addClass("blinking");
    $.post("./ade", {
            function: "sync_planning",
            startDate: $('#start_date input').val(),
            stopDate: $('#stop_date input').val()

        })
        .done(function(data) {
            $("#extractionPlanningsIcon").removeClass("blinking");
            if ($.parseJSON(data).info == "All good") {
                $("#extractionPlanningsBtn").removeClass("red").addClass("green");
            } else {
                alert($.parseJSON(data).info);
            }
        });
});

$("#applyconfigBtn").click(function() {
    $("#applyconfigBtn").addClass("red");
    $("#applyconfigIcon").addClass("blinking");
    $.post("./dbconf", {
            db_name: $('#data_path input').val(),
            db_login: $('#data_login input').val(),
            db_password: $('#data_password input').val()
        })
        .done(function(data) {
            $("#applyconfigIcon").removeClass("blinking");
            $("#applyconfigBtn").removeClass("red").addClass("green");
        });
});

$("#database").click(function() {
    $.get("./dbconf", { })
    .done(function(data) {
        var conf = $.parseJSON(data);
        $('#data_path input').val(conf.db_name),
        $('#data_login input').val(conf.db_login),
        $('#data_password input').val(conf.db_password)
        });
});


$('.menu li').click(function() {
    $('.menu li').removeClass('active');
    $(this).addClass('active');
    $('.view').removeClass('active');
    $('.view#' + $(this).attr('id')).addClass('active');
});