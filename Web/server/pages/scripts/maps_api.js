
var map;
var coordsPopup;

function initMap() {
    lat_default = 59.936362
    lng_default = 30.319476
    var loc = new google.maps.LatLng(lat_default, lng_default);
    var directionsService = new google.maps.DirectionsService;
    var directionsDisplay = new google.maps.DirectionsRenderer;

    var options={
        zoom:14,
        center:loc,
        mapTypeControlOptions: {
            mapTypeIds: []
        }, // here´s the array of controls
        disableDefaultUI: true, // a way to quickly hide all controls
        mapTypeControl: true,
        scaleControl: true,
        zoomControl: true,
        zoomControlOptions: {
            style: google.maps.ZoomControlStyle.LARGE
        },
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    map= new google.maps.Map(document.getElementById('map'),options);
    directionsDisplay.setMap(map);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            loc = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            map.setCenter(loc);
        });
    }

    coordsPopup = new google.maps.InfoWindow(
        {content: 'Click the map to get Lat/Lng!', position: loc});

    show_coords_popups = false;
    // Configure the click listener.
    updateCoords(lat_default, lng_default)
    map.addListener('click', function(mapsMouseEvent) {
        if(isShowPopups())
        {
            coordsPopup.close();

            // Create a new InfoWindow.
            coordsPopup = new google.maps.InfoWindow({position: mapsMouseEvent.latLng});
            coordsPopup.setContent(mapsMouseEvent.latLng.toString());
            coordsPopup.open(map);
        }

        lat = mapsMouseEvent.latLng.lat();
        lng = mapsMouseEvent.latLng.lng();
        updateCoords(lat, lng)
    });
}

function addObj(data){
    var latlng = new google.maps.LatLng(data["latitude"],data["longitude"]);
    var icon = {
        url: 'https://postavtezachotpozhaluysta.ru/resources/icons/'+data["type"]+'.png',
        scaledSize: new google.maps.Size(30, 30),
        origin: new google.maps.Point(0,0),
        anchor: new google.maps.Point(0, 0)
    };
    var marker = new google.maps.Marker({
        position: latlng,
        title: 'new marker',
        draggable: false,
        map: map,
        icon: icon
    });

    var spotInfoWindow = new google.maps.InfoWindow({
        content:'' +
            '<div class="info" style="text-align: center; display: block; overflow-wrap: break-word;">'+
            '<img src="'+'https://postavtezachotpozhaluysta.ru/resources/images/'+data["type"]+'.jpg'+'" width="200px" height="auto">'+
            '<br><br><address>'+data['details']+'</address>'+
            '<p class="details_header">'+data['descriptions']+'</p></div>'
    });

    marker.addListener('click',function () {
        spotInfoWindow.open(map, marker);
    });
}
