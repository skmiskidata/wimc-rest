var VIEWBOX_X = 3840;
var VIEWBOX_Y = 2160;
var SAMPLE_X = 1600;
var SAMPLE_Y = 900;
var OFFSET_X = 180;

var licenseplateIds = [];

var stompClient = null;

function addPlateId(id) {
    licenseplateIds.push(id);
}

function removePlateId(id) {
    var index = licenseplateIds.indexOf(id);
    if (index > -1) {
        licenseplateIds.splice(index, 1);
    }
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/track', function (lpmessage) {
            var event = JSON.parse(lpmessage.body);
            if (event.hasOwnProperty('InitVehicle')) {
                var data = event['InitVehicle'];
                initVehicle(data.uuid, data.lp, data.x, data.y);
            }
            else if (event.hasOwnProperty("MoveVehicle")) {
                var data = event['MoveVehicle'];
                moveVehicle(data.uuid, data.lp, data.x, data.y);
            }
            else if (event.hasOwnProperty("RemoveVehicle")) {
                var data = event['RemoveVehicle'];
                removeVehicle(data.uuid);
            }
            console.log(event);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function calcX(x) {
    return (x / SAMPLE_X * VIEWBOX_X) - OFFSET_X;
}

function calcY(y) {
    return VIEWBOX_Y - (y / SAMPLE_Y * VIEWBOX_Y);
}

function initVehicle(id, licenseplate, dx, dy) {
    var defer = $.Deferred();
    var container = Snap("#carpark");
    Snap.load("asset/plate.svg", function(lp) {

        var rootelem = lp.select('#licenseplate');
        rootelem.attr({ id: 'lp_'+id, viewBox: '0 0 3840 2160' });

        var lpelem = lp.select('#licenseplate_number');
        lpelem.node.innerHTML = licenseplate;

        var mainlayer = lp.select('#main_layer');
        mainlayer.stop().animate({transform: 'T' + calcX(dx) + ',' + calcY(dy)}, 0);
        container.append(lp);

        mainlayer.hover(function() {
                var localMatrix = mainlayer.transform().localMatrix;
                if (localMatrix.a === 1 && localMatrix.d === 1) {
                    var transform = new Snap.Matrix();
                    transform.add(localMatrix);
                    transform.scale(1.5);
                    mainlayer.stop().animate({transform: transform}, 200);
                }
            },
            function() {
                var localMatrix = mainlayer.transform().localMatrix;
                if (localMatrix.a == 1.5 && localMatrix.d == 1.5) {
                    var transform = new Snap.Matrix();
                    transform.add(localMatrix);
                    transform.scale();
                    mainlayer.stop().animate({transform: transform}, 200);
                }
            });
        defer.resolve(lp);

        addPlateId(id);
        intersectAllSpaces();
    });
    return defer.promise();
}

function moveVehicle(id, licenseplate, dx, dy) {
    var lp = Snap.select("#lp_"+id);
    if (lp !== undefined && lp !== null) {
        var mainlayer = lp.select('#main_layer');
        mainlayer.stop().animate({transform: 'T' + calcX(dx) + ',' + calcY(dy)}, 200);
    }
    else {
        initVehicle(id, licenseplate, dx, dy);
    }

    intersectAllSpaces();
}

function removeVehicle(id) {
    var lp = Snap.select("#lp_"+id);
    if (lp !== undefined && lp !== null) {
        lp.remove();
        removePlateId(id);
        intersectAllSpaces();
    }
}

function setSpaceOccupancy(id, occupied) {
    var light = Snap.select("#Light_"+id);
    if (occupied) {
        light.attr('fill', 'darkred');
    }
    else {
        light.attr('fill', '#009640');
    }
}

function intersectAllSpaces() {
    var allSpaces = ['A1','A2','A3','A4','A5','A6'];
    $.each(allSpaces, function(i, spaceId) {
        var intersect = false;
        var space = Snap.select("#Parking_space_"+spaceId);

        $.each(licenseplateIds, function(j, lpId) {
            var lp = Snap.select("#lp_"+lpId);
            var result = intersectRect(space, lp);
            if (!intersect && result) {
                intersect = true;
            }
        });
        setSpaceOccupancy(spaceId, intersect);
    });
}

function intersectRect(r1, r2) {
    var r1 = r1.node.getBoundingClientRect();
    var r2 = r2.node.getBoundingClientRect();

    return !(r2.left > r1.right ||
        r2.right < r1.left ||
        r2.top > r1.bottom ||
        r2.bottom < r1.top);
}