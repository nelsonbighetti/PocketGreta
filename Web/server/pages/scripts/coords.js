
var lat_last;
var lng_last;
var show_coords_popups;
var last_selected_id;
var delSpotMode;

function updateCoords(lat, lng){
    lat_last = lat
    lng_last = lng
}

function isShowPopups(){
    return show_coords_popups;
}