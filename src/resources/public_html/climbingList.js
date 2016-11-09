var climbingList = function() {

    var address = "http://wikthewiz.com";
    String.prototype.capitalizeFirstLetter = function() {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }

    var loadList = function($list, listData) {
        $list.empty();
        _.each(listData, function(route) {
            var $elem = $("<li>").text(route.Name);
            if (route.IsDone){
                $elem.addClass("is-done");
            }
            $list.append($elem);
        });
    };

    $.get(address + "/service/load")
    .done(function(data) {
        loadList($("#martin_bohus_list"), data.Martin.Bohus);
        loadList($("#daniel_bohus_list"), data.Daniel.Bohus);
        loadList($("#patrik_bohus_list"), data.Patrik.Bohus);

        loadList($("#martin_got_list"), data.Martin.Got);
        loadList($("#daniel_got_list"), data.Daniel.Got);
        loadList($("#patrik_got_list"), data.Patrik.Got);
    })
    .fail(function(msg) {
        alert("Sorry we failed to load the corrrect lists " + msg.statusText);
    });

    $.get(address + "/service/user")
    .done(function(user) {
        if (user === "") {
            $("#user").hide();
            $("#logout").hide();
        } else {
            $("#user").text("Welcome: " + user.capitalizeFirstLetter()).show();
            $("#logout").show();
        }
    })
    .fail(function(msg) {
        $("#user").hide();
    });
}();
