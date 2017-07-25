var nilfunc = function() {};
var animtime = 200;

function successMessage(result, message) {
    if (result.message === "success") {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html(message);
    }
}

function cookieRedisplay(result) {
    if (result.message === "success") {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html("Cookie name: " + result.cookiename + " | Cookie value: " + result.cookievalue);
    } else {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html("No cookie data found!");
    }
}

function sessionRedisplay(result) {
    if (result.message === "success") {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html("Username data: " + result.username);
    } else {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html("No session username data found!");
    }
}

function getCookieRequest() {
    $.ajax({
        url: "/getnewcookie",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                successMessage(result, "Successfully got new cookie.");
            });
        }
    });
}

function showCookieRequest() {
    $.ajax({
        url: "/showcookie",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                cookieRedisplay(result);
            });
        }
    });
}

function removeCookieRequest() {
    $.ajax({
        url: "/revokecookie",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                successMessage(result, "Revoked cookie.");
            });
        }
    });
}

function getSessionRequest() {
    var usernameInput = prompt("Please enter your username:", "");

    $.ajax({
        url: "/addtosession",
        type: "POST",
        data: JSON.stringify({
            "username" : usernameInput
        }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                successMessage(result, "Successfully added username data to session.");
            });
        }
    });
}

function showSessionDataRequest() {
    $.ajax({
        url: "/showsessiondata",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                sessionRedisplay(result);
            });
        }
    });
}

function removeSessionDataRequest() {
    $.ajax({
        url: "/removefromsession",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                successMessage(result, "Removed username data from session.");
            });
        }
    });
}