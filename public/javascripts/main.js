var nilfunc = function() {};

function cookieDisplayGet(result) {
    if (result.message === "success") {
        $("#hidewrap").show(400, nilfunc);
        $("#hiddenmessage").html("Successfully got new cookie.");
    }
}

function getCookieRequest() {
    $.ajax({
        url: "/getnewcookie",
        type: 'GET',
        success: function(result) {
            $("#hidewrap").hide(400, function() {
                cookieDisplayGet(result)
            })
        }
    });
}

function cookieRedisplay(result) {
    if (result.message === "success") {
        $("#hidewrap").show(400, nilfunc);
        $("#hiddenmessage").html("Cookie name: " + result.cookiename + " | Cookie value: " + result.cookievalue);
    } else {
        $("#hidewrap").show(400, nilfunc);
        $("#hiddenmessage").html("No cookie data found!");
    }
}

function showCookieRequest() {
    $.ajax({
        url: "/showcookie",
        type: 'GET',
        success: function(result) {
            $("#hidewrap").hide(400, function() {
                cookieRedisplay(result)
            })
        }
    });
}

function cookieRevokeGet(result) {
    if (result.message === "success") {
        $("#hidewrap").show(400, nilfunc);
        $("#hiddenmessage").html("Revoked cookie.");
    }
}

function removeCookieRequest() {
    $.ajax({
        url: "/revokecookie",
        type: 'GET',
        success: function(result) {
            $("#hidewrap").hide(400, function() {
                cookieRevokeGet(result)
            })
        }
    });
}