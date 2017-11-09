var VIEWBOX_X = 3840;
var VIEWBOX_Y = 2160;
var SAMPLE_X = 1600;
var SAMPLE_Y = 900;
var OFFSET_X = 180;

var vehicleData = [];
var stompClient = null;
var blockAnim = false;

function initUI() {
    // Connect to web-socket
    connect();

    function resize() {
        var newwidth = $(window).width()-20;
        var newheight = $(window).height()-80;
        $('#carpark_container').height(newheight).width(newwidth);
    }
    resize();

    $(window).resize(function(){
        resize();
    });

    $('#search-form').submit(function(e){
        var id = null;
        var lp = $('#autocomplete').val();
        $.each(vehicleData, function(i, data) {
            if (data.value.toLowerCase() === lp.toLowerCase()) {
                id = data.id;
            }
        });
        if (id !== null) {
            highlightVehicle(id);
        }
        return false;
    });

    $('#autocomplete').autocomplete({
        lookup: vehicleData,
        autoFocus: true,
        delay: 200,
        onSelect: function (suggestion) {
            highlightVehicle(suggestion.id);
        }
    });
}

function addVehicleData(id, licenseplate) {
    var data = {};
    data.value = licenseplate;
    data.id = id;
    vehicleData.push(data);
}

function removeVehicleData(id) {
    var index = -1;
    $.each(vehicleData, function(i, data) {
        if (data.id === id) {
            index = i;
        }
    });
    if (index > -1) {
        vehicleData.splice(index, 1);
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
                initVehicle(data.uuid, data.lp, data.x, data.y, data.color);
            }
            else if (event.hasOwnProperty("MoveVehicle")) {
                var data = event['MoveVehicle'];
                moveVehicle(data.uuid, data.lp, data.x, data.y, data.color);
            }
            else if (event.hasOwnProperty("RemoveVehicle")) {
                var data = event['RemoveVehicle'];
                removeVehicle(data.uuid);
            }
            else if (event.hasOwnProperty("ChangeLP")) {
                var data = event['ChangeLP'];
                changeLP(data.uuid, data.lp);
            }
            //console.log(event);
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

function initVehicle(id, licenseplate, dx, dy, color) {
    var defer = $.Deferred();
    var container = Snap("#carpark");
    Snap.load("asset/plate.svg", function(lp) {

        var rootelem = lp.select('#licenseplate');
        rootelem.attr({ id: 'lp_'+id, viewBox: '0 0 3840 2160' });

        var lpelem = lp.select('#licenseplate_number');
        if (licenseplate === undefined) {
            licenseplate = 'Unknown';
        }
        lpelem.node.innerHTML = licenseplate;

        if (color !== null) {
            var vehiclecolor = lp.select('#vehicle_color');
            vehiclecolor.attr('fill', color);
        }

        var mainlayer = lp.select('#main_layer');
        mainlayer.stop().animate({transform: 'T' + calcX(dx) + ',' + calcY(dy)}, 0);
        container.append(lp);

        /*
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
        */

        defer.resolve(lp);

        addVehicleData(id, licenseplate);
        intersectAllSpaces();
    });
    return defer.promise();
}

function moveVehicle(id, licenseplate, dx, dy, color) {
    var lp = Snap.select("#lp_"+id);
    if (blockAnim === false) {
        if (lp !== undefined && lp !== null) {
            if (color !== null) {
                var vehiclecolor = lp.select('#vehicle_color');
                vehiclecolor.attr('fill', color);
            }
            var mainlayer = lp.select('#main_layer');
            mainlayer.stop().animate({transform: 'T' + calcX(dx) + ',' + calcY(dy)}, 200, null, function () {
                intersectAllSpaces();
            });
        }
        else {
            initVehicle(id, licenseplate, dx, dy, color);
        }
    }
}

function highlightVehicle(id) {
    var lp = Snap.select("#lp_"+id);
    if (lp !== undefined && lp !== null) {
        blockAnim = true;

        var mainlayer = lp.select('#main_layer');

        var originalMatrix = mainlayer.transform().localMatrix;
        var scaledMatrix = mainlayer.transform().localMatrix;
        scaledMatrix.scale(2,2);

        mainlayer.stop().animate({ transform: scaledMatrix }, 1000, null, function() {
            mainlayer.stop().animate({ transform: originalMatrix }, 1000, null, function() {
                blockAnim = false;
            })
        });
    }
}

function removeVehicle(id) {
    var lp = Snap.select("#lp_"+id);
    if (lp !== undefined && lp !== null) {
        lp.remove();
        removeVehicleData(id);
        intersectAllSpaces();
    }
}

function changeLP(id, licenseplate) {
    var lp = Snap.select("#lp_"+id);
    if (lp !== undefined && lp !== null) {
        var lpelem = lp.select('#licenseplate_number');
        if (lpelem !== undefined && lpelem !== null) {
            lpelem.node.innerHTML = licenseplate;
        }
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

        $.each(vehicleData, function(j, data) {
            var lp = Snap.select("#lp_"+data.id);
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