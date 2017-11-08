var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#lp-out").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lp', function (lpmessage) {
            console.log(lpmessage.body);
            showPlate(JSON.parse(lpmessage.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showPlate(lpmessage) {
    console.log(lpmessage);
    $("#lp-out").append("<tr><td>" + lpmessage.licencePlate + "</td><td>" + lpmessage.plateCoordinates[0].x + "</td><td>" + lpmessage.plateCoordinates[0].y + "</td><td>" + lpmessage.plateCoordinates[1].x + "</td><td>" + lpmessage.plateCoordinates[1].y + "</td><td>" + lpmessage.plateCoordinates[2].x + "</td><td>" + lpmessage.plateCoordinates[2].y + "</td><td>" + lpmessage.plateCoordinates[3].x + "</td><td>" + lpmessage.plateCoordinates[3].y + "</td><td>" + lpmessage.uuid + "</td><td>" + lpmessage.confidence + "</td><td>" + lpmessage.draw + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});