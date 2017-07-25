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
        $("#hiddenmessage").html("Session data: " + result.sessiondata);
    } else {
        $("#hidewrap").show(animtime, nilfunc);
        $("#hiddenmessage").html("No session data found!");
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
    $.ajax({
        url: "/addtosession",
        type: "GET",
        success: function(result) {
            $("#hidewrap").hide(animtime, function() {
                successMessage(result, "Successfully added data to session.");
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
                successMessage(result, "Removed data from session.");
            });
        }
    });
}