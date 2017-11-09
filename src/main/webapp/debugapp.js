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
        stompClient.subscribe('/topic/debugOut', function (lpmessage) {
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

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}


function showPlate(lpmessage) {
    console.log(lpmessage);
    var myTimestamp = new Date().getTime();

    var myNode = document.getElementById("cnt");
    var children = myNode.childNodes;
    children.forEach(function (item) {
        console.log(item);
        if (item.vId == lpmessage.uuid) {
            myNode.removeChild(item);
        }
    });

    var children = myNode.childNodes;
    children.forEach(function (item) {
        console.log(item);
        if (item.timeCreated) {
            if (item.timeCreated < (myTimestamp - 500)) {
                myNode.removeChild(item);
            }
        }
    });


    var scale = 1;

    var uuidInternal = uuidv4();


    if (lpmessage.plateCoordinates) {
        $("#cnt").append("<div id='" + uuidInternal + "' style='position: absolute; " +
            " top: " + (lpmessage.plateCoordinates[0].y / scale) + "px; " +
            " left: " + (lpmessage.plateCoordinates[0].x / scale) + "px; " +
            " width: " + ((lpmessage.plateCoordinates[1].x - lpmessage.plateCoordinates[0].x ) / scale) + "px; " +
            " height: " + ((lpmessage.plateCoordinates[2].y - lpmessage.plateCoordinates[1].y ) / scale) + "px; " +
            " border-color: black;" +
            " border-width: 1px;" +
            " border-style: solid;" +
            "' >" + lpmessage.licencePlate +
            "</div>");
    }
    if (lpmessage.carBounding) {
        $("#cnt").append("<div id='" + uuidInternal + "-2' style='position: absolute; " +
            " top: " + (lpmessage.carBounding.y1 / scale) + "px; " +
            " left: " + (lpmessage.carBounding.x1 / scale) + "px; " +
            " width: " + ((lpmessage.carBounding.x2 - lpmessage.carBounding.x1 ) / scale) + "px; " +
            " height: " + ((lpmessage.carBounding.y3 - lpmessage.carBounding.y2 ) / scale) + "px; " +
            " border-color: " + lpmessage.carColor + ";" +
            " border-width: 2px;" +
            " border-style: solid;" +
            "' >" + lpmessage.carMake + " - " + lpmessage.carMakeConfidence + "<br/>" +
            lpmessage.uuid + "<br/>" +
            lpmessage.carColor + " - " + lpmessage.carColorConfidence + "<br/>" +
            lpmessage.licencePlate + " - " + lpmessage.licencePlateConfidence + "<br/>" +
            "conf:  - " + lpmessage.confidence + "<br/>" +
            "camId:  - " + lpmessage.cameraSource +
            "</div>");
    }

    var myNodeCreated1 = document.getElementById(uuidInternal);
    var myNodeCreated2 = document.getElementById(uuidInternal + "-2");

    if (myNodeCreated1) {
        myNodeCreated1.timeCreated = myTimestamp;
    }
    if (myNodeCreated2) {
        myNodeCreated2.timeCreated = myTimestamp;
        myNodeCreated2.vId = lpmessage.uuid;
    }


}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
