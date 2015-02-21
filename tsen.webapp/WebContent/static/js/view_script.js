var zones;
var backgrounds;

var world_version = -1;

var tableBuilt = false;
var stopUpdate = false;

;(function($) {
    $.zoneProp = function(param) {	

        this.set = function(param) {
            this.init = true;
            if(typeof param == 'number') {
                this.zoneID = param
            }
            else if(typeof param == 'object') {
                this.zoneID = param.closest( "td" ).attr( "zone_id" );
            }
            else
            {
                this.init = false;   
            }
            return this;
        };

        this.zoneName = function() {
            if (this.init)
                return $('th.zone_name[zone_id="' + this.zoneID + '"]');
        };

        this.getZoneName = function() {
            if (this.init)
                return this.zoneName().text();
        };

        this.setZoneName = function(name) {
            if (this.init)
                return this.zoneName().text(name);
        };

        this.zoneBackgroundSelector = function(bundle, channel) {
            if (this.init)
                return $('tr[background_bundle="' + bundle + '"][background_channel="' + channel + '"] td[zone_id="' + this.zoneID + '"] input.background_selector');
        };

        this.isZoneBackgroundSelectorChecked = function(bundle, channel) {
            if (this.init)
                return this.zoneBackgroundSelector(bundle, channel).prop("checked");
        };

        this.isZoneBackgroundSelectorDisabled = function(bundle, channel) {
            if (this.init)
                return this.zoneBackgroundSelector(bundle, channel).prop("disabled");
        };

        this.setZoneBackgroundSelectorChecked = function(bundle, channel, state) {
            if (this.init)
                this.zoneBackgroundSelector(bundle, channel).prop("checked", state);
        };

        this.setZoneBackgroundSelectorDisabled = function(bundle, channel, state) {
            if (this.init)
                this.zoneBackgroundSelector(bundle, channel).prop("disabled", state);
        };

        this.zoneBackgroundSelectors = function() {
            if (this.init)
                return $('td[zone_id="' + this.zoneID + '"] .background_selector ');
        };

        this.zoneLevel = function() {
            if (this.init)
                return $('td[zone_id="' + this.zoneID + '"] .zone_level ');
        };

        this.getZoneLevel = function() {
            if (this.init)
                return this.zoneLevel().val();
        };

        this.setZoneLevel = function(value) {
            if (this.init)
                this.zoneLevel().val(value).attr({lastval : value}).parent().find('.level_value').text(value);
        };

        this.setZoneLevelMuted = function(value) {
            if (this.init)
                value ? this.zoneLevel().addClass("mutedAll") : this.zoneLevel().removeClass("mutedAll");
        };

        this.setZoneLevelBackgroundMuted = function(value) {
            if (this.init)
                value ? this.zoneLevel().addClass("mutedBGM") : this.zoneLevel().removeClass("mutedBGM");
        };

        this.zoneMute = function() {
            if (this.init)
                return $('td[zone_id="' + this.zoneID + '"] .zone_mute ');
        };

        this.isZoneMuted = function() {
            if (this.init)
                return this.zoneMute().attr("value") == "Unmute All";
        };

        this.setZoneMuted = function(state) {
            if (this.init)
                state ? this.zoneMute().attr("value", "Unmute All").addClass("muted") : this.zoneMute().attr("value", "Mute All").removeClass("muted");
        };

        this.zoneMuteBackground = function() {
            if (this.init)
                return $('td[zone_id="' + this.zoneID + '"] .zone_mute_background ');
        };

        this.isZoneBackgroundMuted = function() {
            if (this.init)
                return this.zoneMuteBackground().attr("value") == "Unmute BGM";
        };

        this.setZoneBackgroundMuted = function(state) {
            if (this.init)
                state ? this.zoneMuteBackground().attr("value", "Unmute BGM").addClass("muted") : this.zoneMuteBackground().attr("value", "Mute BGM").removeClass("muted");
        };


        if(this.init) {
            return new $.zoneProp(param);
        } else {
            this.set(param);
            return this;
        }
    };
})(jQuery);

;(function($) {
    $.timer = function(func, time, autostart) {	
        this.set = function(func, time, autostart) {
            this.init = true;
            if(typeof func == 'object') {
                var paramList = ['autostart', 'time'];
                for(var arg in paramList) {if(func[paramList[arg]] != undefined) {eval(paramList[arg] + " = func[paramList[arg]]");}};
                func = func.action;
            }
            if(typeof func == 'function') {this.action = func;}
            if(!isNaN(time)) {this.intervalTime = time;}
            if(autostart && !this.isActive) {
                this.isActive = true;
                this.setTimer();
            }
            return this;
        };
        this.play = function(reset) {
            if(!this.isActive) {
                if(reset) {this.setTimer();}
                else {this.setTimer(this.remaining);}
                this.isActive = true;
            }
            return this;
        };
        this.stop = function() {
            this.isActive = false;
            this.remaining = this.intervalTime;
            this.clearTimer();
            return this;
        };
        this.reset = function() {
            this.isActive = false;
            this.play(true);
            return this;
        };
        this.clearTimer = function() {
            window.clearTimeout(this.timeoutObject);
        };
        this.setTimer = function(time) {
            var timer = this;
            if(typeof this.action != 'function') {return;}
            if(isNaN(time)) {time = this.intervalTime;}
            this.remaining = time;
            this.last = new Date();
            this.clearTimer();
            this.timeoutObject = window.setTimeout(function() {timer.go();}, time);
        };
        this.go = function() {
            if(this.isActive) {
                this.action();
                this.setTimer();
            }
        };

        if(this.init) {
            return new $.timer(func, time, autostart);
        } else {
            this.set(func, time, autostart);
            return this;
        }
    };
})(jQuery);

function getData(){
    $.get("./view", function(data,status) {
        $.each($.parseJSON(data), function (item, value) {
            if (item == "zones")
            {
                zones = value;
            }
            else if (item == "backgrounds")
            {
                backgrounds = value;
            }
            else if (item == "viewSettings")
            {
                processSettings(value);
            }
        });
        refreshTable();
    });
};

var timer = $.timer(function () {
    if (!stopUpdate)
        getData();
});


function refreshTable() {
    if (tableBuilt)
        fillTable();
    else
        generateTable();
};

function processSettings(settings) {
    if (timer.isActive == null)
        timer.set({ time : settings["page_RefreshTime"], autostart : settings["page_AutoRefresh"] });
    else if (timer.intervalTime != settings["page_RefreshTime"])
        timer.intervalTime = settings["page_RefreshTime"];
    else if (timer.isActive != settings["page_AutoRefresh"])
        settings["page_AutoRefresh"] ? timer.play(true) : timer.stop();
    if ($("#table-title h3:first-child").text() != settings["ms1_WorldName"])
        $("#table-title h3:first-child").text(settings["ms1_WorldName"])

        };


function generateTable() {

    tableBuilt = false;

    $('div#table').empty().append($('<table>').addClass("table-fill"));

    $('table.table-fill').append($('<thead>').append($('<tr>').append($('<th>').addClass("form_container"))));
    $.each(zones, function (key, zone)
           {
               $('table.table-fill thead tr th.form_container').append($('<form>').attr({id : ("form_zone_" + zone['id'])}));
               $('table.table-fill thead tr').append($('<th>').addClass("text-center").addClass("zone_name").attr({zone_id : zone['id']}));
           });


    $('table.table-fill').append($('<tfoot>').append($('<tr>').append($('<td>'))));
    $.each(zones, function (key, zone)
           {
               var td = $('<td>').addClass("text-center").addClass("zone_settings").attr({zone_id : zone['id']});
               $(td).append($('<div>').addClass("zone_level").attr({lastval : "0"})).append('<br>');
               $(td).append($('<div>').addClass("level_value").text('0')).append('<br>');
               $(td).append($('<div>').addClass("button_container")
                            .append($('<input>').addClass("zone_mute button").attr({type : "submit", value : "Mute All"}))
                            .append($('<input>').addClass("zone_mute_background button").attr({type : "submit", value : "Mute BGM"}))
                           );
               $('table.table-fill tfoot tr').append($(td));
           });


    $('table.table-fill').append($('<tbody>'));
    $.each(backgrounds, function (key_b, background)
           {
               var tr = $('<tr>').attr({background_bundle : background["cobraNet"]['bundle'], background_channel : background["cobraNet"]['channel']});
               $(tr).append($('<td>').addClass("background_name"));
               $.each(zones, function (key_z, zone)
                      {
                          var td = $('<td>').addClass("text-center").attr({zone_id : zone['id']});
                          $(td).append($('<div>').addClass("background_selector_container").append($('<input>').addClass("background_selector").attr({type : "checkbox", name : "select", value : "0", form : ("form_zone_" + zone['id']), disabled : ""})).append('<span>'));
                          $(tr).append($(td));            
                      });        
               $('table.table-fill tbody').append(tr);
           });


    $('.zone_level').noUiSlider({
        start: 0,
        orientation: "vertical",
        direction: "rtl",
        range: {
            'min': 0,
            'max': 100
        }
    });






    $('.zone_level').on({
        slide: function () {
            stopUpdate = true;
            if ((parseInt($(this).val()) <= (parseInt($(this).attr("lastval")) - 5)) || (parseInt($(this).val()) >= (parseInt($(this).attr("lastval")) + 5))) {
                $(this).attr({
                    lastval: Math.round($(this).val())
                });
                $.post("./control", {
                    control: "zoneVolumeLevel",
                    level: Math.round($(this).val()),
                    zoneid: $(this).parent().attr("zone_id")
                });
            }
            $(this).parent().find('.level_value').text(Math.round($(this).val()));
        },

        set: function () {
            $(this).attr({
                lastval: Math.round($(this).val())
            });
            $.post("./control", {
                control: "zoneVolumeLevel",
                level: Math.round($(this).val()),
                zoneid: $(this).parent().attr("zone_id")
            });
            stopUpdate = false;
        }
    });

    $('.background_selector').change(function(){
        var $zone = $.zoneProp($(this));
        $.post( "./control", { control: "zoneBackgroundSelection", zoneid: $zone.zoneID, inputid: $(this).attr( "background_id" ) });
        $zone.zoneBackgroundSelectors().prop('checked', false);
        $(this).prop('checked', true);
        stopUpdate = false;
    });


    $('.zone_mute').click(function(){
        var $zone = $.zoneProp($(this));
        $.post( "./control", { control: "mute", zoneid: $zone.zoneID } );
        $zone.setZoneLevelMuted(!$zone.isZoneMuted());
        $zone.setZoneMuted(!$zone.isZoneMuted());
        stopUpdate = false;

    });

    $('.zone_mute_background').click(function(){
        var $zone = $.zoneProp($(this));
        $.post( "./control", { control: "mutebackground", zoneid: $zone.zoneID } );
        $zone.setZoneLevelBackgroundMuted(!$zone.isZoneBackgroundMuted());
        $zone.setZoneBackgroundMuted(!$zone.isZoneBackgroundMuted());
        stopUpdate = false;
    });


    tableBuilt = true;
    fillTable()
};
function fillTable() {
    var elmnt;

    $.each(backgrounds, function (key, value) {
        elmnt = $('tr[background_bundle="' + value["cobraNet"]["bundle"] + '"][background_channel="' + value["cobraNet"]["channel"] + '"] td.background_name');
        if (elmnt.text() != value["name"])
            elmnt.text(value["name"])
            });

    $.each(zones, function (key, zone) {
        var $zone = $.zoneProp(zone["id"]);

        ($zone.getZoneName() != zone["name"]) ? $zone.setZoneName(zone["name"]) : false;

        ($zone.getZoneLevel() != zone["level"]) ? $zone.setZoneLevel(zone["level"]) : false;

        ($zone.isZoneMuted() != zone["muted"]) ? $zone.setZoneLevelMuted(zone["muted"]) : false;

        ($zone.isZoneMuted() != zone["muted"]) ? $zone.setZoneMuted(zone["muted"]) : false;

        ($zone.isZoneBackgroundMuted() != zone["backgroundMuted"]) ? $zone.setZoneLevelBackgroundMuted(zone["backgroundMuted"]) : false;

        ($zone.isZoneBackgroundMuted() != zone["backgroundMuted"]) ? $zone.setZoneBackgroundMuted(zone["backgroundMuted"]) : false;

        !$zone.isZoneBackgroundSelectorChecked(zone["cobraNet"]["bundle"], zone["cobraNet"]["channel"]) ? $zone.zoneBackgroundSelectors().prop('checked', false) : false;

        !$zone.isZoneBackgroundSelectorChecked(zone["cobraNet"]["bundle"], zone["cobraNet"]["channel"]) ? $zone.setZoneBackgroundSelectorChecked(zone["cobraNet"]["bundle"], zone["cobraNet"]["channel"], true) : false;

        $.each(zone["backgrounds"], function (key, background) {
            var $background = $.zoneProp(zone["id"]);

            $background.setZoneBackgroundSelectorDisabled(background["cobraNet"]["bundle"], background["cobraNet"]["channel"], false);

            $background.zoneBackgroundSelector(background["cobraNet"]["bundle"], background["cobraNet"]["channel"]).attr("background_id", key);

        });

        if (Object.keys(zone["backgrounds"]).length != $('td[zone_id="' + zone["id"] + '"] input[type="checkbox"][disabled!="disabled"]').length)
            tableBuilt = false;
    });

    if (zones.length != $('th.zone_name').length)
        tableBuilt = false;

    if (backgrounds.length != $('td.background_name').length)
        tableBuilt = false;
};






getData();