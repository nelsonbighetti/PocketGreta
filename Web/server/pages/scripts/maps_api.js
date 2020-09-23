
var map;
function initMap() {
    var loc = new google.maps.LatLng(59.936362, 30.319476);
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
}

function addObj(data){
    var latlng = new google.maps.LatLng(data["latitude"],data["longitude"]);
    var icon = {
        url: 'https://localhost:5000/resources/icons/'+data["type"]+'.png',
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

    var infoWindow = new google.maps.InfoWindow({
        content:'' +
            '<div class="info" style="text-align: center; display: block; overflow-wrap: break-word;">'+
            '<img src="'+'https://localhost:5000/resources/images/'+data["type"]+'.jpg'+'" width="200px" height="auto">'+
            '<br><br><address>'+data['details']+'</address>'+
            '<p class="details_header">'+data['descriptions']+'</p></div>'
    });

    marker.addListener('click',function () {
        infoWindow.open(map, marker);
    });
}
