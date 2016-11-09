var logout = function(){
    $.post("http://localhost:8080/service/logout"); 
    window.location.replace("http://localhost:8080");
};
