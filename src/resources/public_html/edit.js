var edit = function() {
    
    var address = "http://wikthewiz.com"
    var ListOption = {
        BOHUS:"bohus",
        GOT:"got"
    };
    var bohusList = [];
    var gotList = [];
    
    var toRemove = [];
    var isDoneMode = true;
    var currentUser = "";
    var onDoneClick = function(name, list, $elem) {
        var $input = $elem.find("input");
        if($input.data("IsDone") === true){
            $input.data("IsDone", false);
            $input.removeClass("is-done");
        } else {
            $input.data("IsDone", true);
            $input.addClass("is-done");
        }
    };
    
    var loadValues = function(listOption) {
        var l = [];
        
        if (listOption === ListOption.BOHUS){
            _.each($("#" + currentUser + "_bohus_list input:text"), function (elem){
                if (elem.value.length != 0){
                    var isDone = false;
                    if ($(elem).data("IsDone") === true){
                        isDone = true;
                    }

                    var e = {"Name":elem.value, "IsDone": isDone};
                    l.push(e);
                }
            });
        } else if(listOption === ListOption.GOT) {
            _.each($("#" + currentUser + "_got_list input:text"), function (elem){
                if (elem.value.length != 0){
                    var isDone = false;
                    if ($(elem).data("IsDone") === true){
                        isDone = true;
                    }

                    var e = {"Name":elem.value, "IsDone": isDone};
                    l.push(e);
                }
            });
        }else{
            alert("Unknown List:" + listOption);
        }

        return l;
    }

    var addSaveButton = function ($appendTo){
        var $save = $("<button type='button'>")
            .addClass("btn btn-success btn-sm")
            .text("save")
            .on("click", function () {
                $.ajax({
                       url:address + "/service/save",
                       type:"POST",
                       data:JSON.stringify({
                           Bohus: loadValues(ListOption.BOHUS), 
                           Got: loadValues(ListOption.GOT)}),
                       contentType:"application/json; charset=utf-8",
                       dataType:"json"})
                .done(function() {
                    window.location.href = address + "/index.html";
                })
                .fail(function() {
                     alert("sorry failed");
                });
       });
       $appendTo.append($save);
    }

    var addCancelButton = function ($appendTo) {
        var $cancel = $("<button type='button'>")
            .addClass("btn btn-default btn-sm")
            .text("cancel")
            .on("click", function (){
                location.reload();
             });

       $appendTo.append($cancel);
    }

    var addButtons = function($appendTo) {
        var $buttonGroup = $("<div/>").addClass("button-group");
        addSaveButton($buttonGroup);
        addCancelButton($buttonGroup);
        $appendTo.append($buttonGroup);
    }

    var onDeleteClick = function(route, list, $elementToRemove) {
        var index = -1;
        if (list.length === 1){
            $elementToRemove.val("");    
            return;
        }

        list.find(function(r, i){
          if (r === route){
            index = i;
            return true;
          }

          return false;
        });
        
        if (index >= 0){
          list.splice(index, 1);
        }
        
        $elementToRemove.remove();
    };

    var loadEditList = function($list, user, listData, listName) {
        var createListItem = function(route, list, id) {
            var mode = "done";
            var text = "+";
            if (!isDoneMode){
                mode = "remove";
                text = "-";
            }
            var $inputgroup = $("<div>");
            var $removeBtn = $("<span>")
              .addClass("input-group-addon " + mode)
              .attr("id", id)
              .text(text)
              .on("click", function() {
                  if (isDoneMode){
                      onDoneClick(route.Name, list, $inputgroup);
                  } else {
                      onDeleteClick(route.Name, list, $inputgroup);
                  }
              });

            var isDone = "";
            if (route.IsDone){
                isDone = "is-done";
            } 
            $inputgroup
              .addClass("input-group")
              .append($removeBtn)
              .append($("<input type='text' placeholder='route'>")
                      .addClass("form-control " + isDone)
                      .attr("aria-describedby", id)
                      .val(route.Name)
                      .keyup(function(obj){
                          var children = $list.find("input");
                          var last = children[children.length -1];
                          if (last.value.length != 0) {
                              var id = $(last).attr("aria-describedby");
                              var index = parseInt(id.split(listName + "_" + user + "_")[1]);
                              var $toInsert = createListItem("", list, listName + "_" + user + "_" + (index + 1));
                              var groups = $list.find(".input-group");
                              $(groups[groups.length -1]).after($toInsert);
                          }
                      }).data("IsDone", route.IsDone));

            return $inputgroup;
        };
        
        _.each(listData, function(route, index) {
            var $toAdd = createListItem(route, listData, listName + "_" + user + "_" + index);
            $list.append($toAdd);
        });

        var children = $list.find("input");
        if (children.length === 0){
            var $empty = createListItem("", listData, listName + "_" + user + "_" + 0);
            $list.append($empty);
        } else {
            var last = children[children.length -1];
            if (last.value.length != 0) {
                var id = $(last).attr("aria-describedby");
                var index = parseInt(id.split(listName + "_" + user + "_")[1]);
                var $empty = createListItem("", listData, listName + "_" + user + "_" + (index + 1));
                $list.append($empty);
            }
        }
    };

     var loadList = function($toAddTo, listData) {
        $list = $("<ul>");
        $toAddTo.append($list);
        _.each(listData, function(route) {
            var $elem = $("<li>").text(route.Name);
            if (route.IsDone){
                $elem.addClass("is-done");
            }
            $list.append($elem);
        });
    };

    String.prototype.capitalizeFirstLetter = function() {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }
    
    $.get(address + "/service/user", function(user) {
        currentUser = user;
        if (currentUser === "") {
            $("#" + user).hide();
        } else {
            $("#" + user).text("Welcome: " + currentUser.capitalizeFirstLetter()).show();
        }
    })
    .fail(function(msg) {
        $("#" + user).hide();
    });

    var createToggleButton = function() {
        var $toggleButton = $("<button type='button'>");
        $toggleButton
          .addClass("btn btn-info btn-sm btn-block")
          .text("toggle to delete")
          .on("click", function() {
            if (isDoneMode) {
                _.each($(".done"), function(btn){
                    $btn = $(btn);
                    $btn.removeClass("done");
                    $btn.addClass("remove");
                    $btn.text("-")
                });

                isDoneMode = false;
                $toggleButton.text("toggle to done");
            }
            else { 
                _.each($(".remove"), function(btn){
                    $btn = $(btn);
                    $btn.removeClass("remove");
                    $btn.addClass("done");
                    $btn.text("+");
                });
  
                $toggleButton.text("toggle to delete");
                isDoneMode = true;
            }
          });
          
          return $toggleButton;        
    }

    $.get(address + "/service/load")
    .done(function(data) {
        if (currentUser.length === 0) {
            window.location.replace(address + "/login.html");
        }

        bohusList = data[currentUser.capitalizeFirstLetter()].Bohus;
        gotList = data[currentUser.capitalizeFirstLetter()].Got;
        var userList = ["martin", "patrik", "daniel"];
        
        _.each(userList, function(user) {
            var userId = "#" + user;
            var $bohusList = $(userId  + "_bohus_list");
            var $gotList = $(userId  + "_got_list");
            if (user === currentUser) {
                $bohusList.append(createToggleButton());
                loadEditList($bohusList, user, bohusList, "bohus");

                $gotList.append(createToggleButton());
                loadEditList($gotList, user, gotList, "got");

                addButtons($gotList);
            } else {
                loadList($bohusList, data[user.capitalizeFirstLetter()].Bohus);
                loadList($gotList, data[user.capitalizeFirstLetter()].Got);
            }
        });
    })
    .fail(function(msg) {
        alert("Sorry we failed to load the corrrect lists " + msg.statusText);
    });

}();

