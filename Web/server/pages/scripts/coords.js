
var lat_last;
var lng_last;
var show_coords_popups;

function updateCoords(lat, lng){
    lat_last = lat
    lng_last = lng
}

function getLastCoords(){
    return {"lat":lat_last, "lng":lng_last}
}

function isShowPopups(){
    return show_coords_popups;
}

