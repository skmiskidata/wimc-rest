var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lp', function (lpmessage) {
            console.log(lpmessage.body);
            //showPlate(JSON.parse(lpmessage.body));
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

function initVehicle(id, licenseplate) {
    var defer = $.Deferred();
    var container = Snap("#carpark");
    Snap.load("asset/plate.svg", function(lp) {

        var rootelem = lp.select('#licenseplate');
        rootelem.attr({ id: 'lp_'+id, viewBox: '0 0 3840 2160' });

        var lpelem = lp.select('#licenseplate_number');
        lpelem.node.innerHTML = licenseplate;

        var mainlayer = lp.select('#main_layer');
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
    });
    return defer.promise();
}

function moveVehicle(id, dx, dy) {
    var lp = Snap.select("#lp_"+id);
    var mainlayer = lp.select('#main_layer');
    mainlayer.stop().animate({ transform: 'T'+dx+','+dy }, 200);
}
