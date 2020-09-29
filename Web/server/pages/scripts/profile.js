function setVisibility(id, visible) {
    document.getElementById(id).style.visibility = visible
}

function setCookie(name,value,days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

function displayLoginPopup(){
    setVisibility('login_error', 'hidden');
    setVisibility('register_error', 'hidden');
    setVisibility('dimmer', 'visible');
    setVisibility('register_popup', 'hidden');
    setVisibility('login_popup', 'visible');
}

function displayRegisterPopup(){
    setVisibility('login_error', 'hidden');
    setVisibility('register_error', 'hidden');
    setVisibility('dimmer', 'visible');
    setVisibility('register_popup', 'visible');
    setVisibility('login_popup', 'hidden');
}

function closePopups(){
    setVisibility('login_error', 'hidden');
    setVisibility('register_error', 'hidden');
    setVisibility('dimmer', 'hidden');
    setVisibility('register_popup', 'hidden');
    setVisibility('login_popup', 'hidden');
    setVisibility('add_spot_popup', 'hidden');
    setVisibility('add_spot_error', 'hidden');
    setVisibility('editor_del_spot_error','hidden');
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

function setProfileData(data){
    setCookie("authenticationToken", data['authenticationToken'])
    setCookie("refreshToken", data['refreshToken'])
    setCookie("username", data['username'])
}

async function loginCreds(email, password, userdriven){
    let params = {
        "email": email,
        "password": password
    }

    await axios.post('https://postavtezachotpozhaluysta.ru/rest/auth/login', params
    ).catch(error => {
        if(userdriven){
            document.getElementById("login_error").style.visibility = "visible"
        }
    }).then(response => {
        if(response.status===200){
            deleteAllCookies()
            setProfileData(response.data);
            setCookie("cookie_sessionid", response.data['cookie_sessionid'])
            closePopups();
            checkLogin();
        }
    })
    checkPrivileges()
}

async function dologin() {
    var email = document.getElementById("login_popup_email").value;
    var password = document.getElementById("login_popup_password").value;
    document.getElementById("login_error").style.visibility = "hidden"


    //let email = "BigAssTurtle@turtleisland.tr"
    //let password = "ShellIsLie"

    loginCreds(email, password, true)
}

async function doregister() {
    document.getElementById("register_error").style.visibility = "hidden"
    let username = document.getElementById("register_popup_login").value;
    let email = document.getElementById("register_popup_email").value;
    let password = document.getElementById("register_popup_password").value;

    let params = {
        "username": username,
        "email": email,
        "password": password
    }

    await axios.post('https://postavtezachotpozhaluysta.ru/rest/auth/signup', params
    ).catch(error => {
        document.getElementById("register_error").style.visibility = "visible"
    }).then(response => {
        if(response.status!==200){
            document.getElementById("register_error").style.visibility = "visible"
        }
        else{
            loginCreds(email, password, false)
        }
    })

}

function refreshToken(){
    let username = getCookie('username')
    let refreshToken = getCookie('refreshToken')
    let params = {
        "username": username,
        "refreshToken": refreshToken
    }

    axios.post('https://postavtezachotpozhaluysta.ru/rest/refreshToken', params
    ).catch(error => {
        document.getElementById("register_error").style.visibility = "visible"
    }).then(response => {
        if(response.status!==200){
            document.getElementById("register_error").style.visibility = "visible"
        }
        else{
            setProfileData(response.data)
        }
    })
}

async function checkLogin() {
    let username = getCookie('username')
    if(username){
        refreshToken()
        let authenticationToken = getCookie('authenticationToken')
        let cookie_sessionid = getCookie('cookie_sessionid')

        let params = {
            "username": username,
            "authenticationToken": authenticationToken,
            "refreshToken": refreshToken,
            "cookie_sessionid": cookie_sessionid
        }

        await axios.post('https://postavtezachotpozhaluysta.ru/rest/acc/', params
        ).catch(error => {
            document.getElementById("login_error").style.visibility = "visible"
        }).then(response => {
            if(response.status!==200){
                document.getElementById("login_error").style.visibility = "visible"
            }
            else{
                setVisibility("login_btn", "hidden")
                setVisibility("register_btn", "hidden")
                setVisibility("profile", "visible")
                setVisibility("rating", "visible")
                setVisibility("email", "visible")
                setVisibility("logout_btn", "visible")
                document.getElementById('profile_inner_text').innerHTML = response.data['username'];
                document.getElementById('rating_inner_text').innerHTML = response.data['bonuses'];
                document.getElementById('email_inner_text').innerHTML = response.data['email'];
            }
        })
    }
    checkPrivileges()
}

function hideAll(){
    closePopups()
    setVisibility("profile", "hidden")
    setVisibility("rating", "hidden")
    setVisibility("email", "hidden")

    setVisibility('edit_btn', 'hidden')
    setVisibility('edit_close_btn', 'hidden')
    setVisibility('editor_add_point', 'hidden')
    setVisibility('editor_del_point', 'hidden')
    setVisibility('editor_del_point_go_back', 'hidden')
    setVisibility('editor_del_point_cancel_selection', 'hidden')
    setVisibility('editor_del_point_confirm', 'hidden')
    setVisibility('editor_coords_confirm_point', 'hidden')
    setVisibility('editor_del_spot_error', 'hidden')
    delSpotMode = false
}

function logout() {
    eraseCookie("authenticationToken")
    eraseCookie("refreshToken")
    eraseCookie("username")
    eraseCookie("cookie_sessionid")
    eraseCookie("token_expires")

    setVisibility("login_btn", "visible")
    setVisibility("register_btn", "visible")
    setVisibility("logout_btn", "hidden")
    hideAll()
}

function displayDelSpotError(){
    setVisibility('dimmer', 'visible')
    setVisibility('editor_del_spot_error', 'visible')
}

function enableEditor(){
    setVisibility("profile", "hidden")
    setVisibility("rating", "hidden")
    setVisibility("email", "hidden")

    setVisibility("editor_add_point", "visible")
    setVisibility("editor_del_point", "visible")

    setVisibility("edit_btn", "hidden")
    setVisibility("edit_close_btn", "visible")
}

function disableEditor(){
    hideAll()

    setVisibility("profile", "visible")
    setVisibility("rating", "visible")
    setVisibility("email", "visible")
    setVisibility("edit_btn", "visible")
}

function addSpotPopup() {
    enableEditor();
    setVisibility('dimmer', 'visible');
    setVisibility('add_spot_popup', 'visible');
}

function addSpotHandleDate(){
    var selectBox = document.getElementById("add_spot_type_selector_options");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;

    if(selectedValue=="EVENT"){
        document.getElementById('add_spot_details_wrapper').innerHTML = '<input id="add_spot_details" type="text" placeholder="Event date" class="datepicker">'
        $('.datepicker').datepicker();
        //setVisibility('add_spot_details','visible')
    }
    else {
        document.getElementById('add_spot_details_wrapper').innerHTML = '<input id="add_spot_details" type="text" placeholder="Details">'
        //setVisibility('add_spot_details','visible')
    }
}

function selectCoordinates(){
    closePopups()
    setVisibility('editor_add_point','hidden')
    setVisibility('editor_del_point','hidden')
    setVisibility('editor_coords_confirm_point','visible')
    show_coords_popups = true;
}

function confirmCoords() {
    show_coords_popups = false;
    coordsPopup.close();
    setVisibility('dimmer','visible')
    setVisibility('editor_coords_confirm_point','hidden')
    setVisibility('editor_add_point','visible')
    setVisibility('editor_del_point','visible')
    document.getElementById("add_spot_lat").value = lat_last;
    document.getElementById("add_spot_lon").value = lng_last;
    setVisibility('add_spot_popup','visible')
}

async function sendNewSpot() {
    setVisibility('add_spot_error','hidden')
    let lat = document.getElementById("add_spot_lat").value;
    let lon = document.getElementById("add_spot_lon").value;
    let desc = document.getElementById("add_spot_description").value;
    let type = document.getElementById("add_spot_type_selector_options").value;
    let details = document.getElementById("add_spot_details").value;

    if(!lat || !lon){
        setVisibility('add_spot_error','visible')
        document.getElementById('add_spot_error').innerHTML = '<p>Cannot add spot: location not set</p>'
        return;
    }
    if(!desc){
        setVisibility('add_spot_error','visible')
        document.getElementById('add_spot_error').innerHTML = '<p>Cannot add spot: description not set</p>'
        return;
    }
    if(!type){
        setVisibility('add_spot_error','visible')
        document.getElementById('add_spot_error').innerHTML = '<p>Cannot add spot: type not set</p>'
        return;
    }
    if(type=="EVENT" && !details){
        setVisibility('add_spot_error','visible')
        document.getElementById('add_spot_error').innerHTML = '<p>Cannot add event: date not set</p>'
        return;
    }

    refreshToken()
    let authenticationToken = getCookie('authenticationToken')
    let cookie_sessionid = getCookie('cookie_sessionid')

    params = null
    if(type=="EVENT"){
        params = {
            "type" : type,
            "subtype" : "",
            "latitude" : lat,
            "longitude" : lon,
            "details" : "Event date : " + details,
            "descriptions" : desc,
            "authenticationToken": authenticationToken,
            "cookie_sessionid": cookie_sessionid
        }
    }
    else{
        params = {
            "type" : type,
            "subtype" : "",
            "latitude" : lat,
            "longitude" : lon,
            "details" : details,
            "descriptions" : desc,
            "authenticationToken": authenticationToken,
            "cookie_sessionid": cookie_sessionid
        }
    }

    await axios.post('https://postavtezachotpozhaluysta.ru/rest/map/add', params
    ).catch(error => {
        document.getElementById("add_spot_error").style.visibility = "visible"
        document.getElementById('add_spot_error').innerHTML = '<p>Cannot add spot: server error</p>'
    }).then(response => {
        if(response.status!==200){
            document.getElementById("add_spot_error").style.visibility = "visible"
            document.getElementById('add_spot_error').innerHTML = '<p>Cannot add spot: server error</p>'
        }
        else{
            location.reload();
        }
    })
}

function delSpotRoutine(){
    closePopups();
    setVisibility('editor_add_point','hidden')
    setVisibility('editor_del_point','hidden')
    setVisibility('editor_del_point_go_back','visible')
    delSpotMode = true;
}

function delSpotGoBack(){
    setVisibility('editor_add_point','visible')
    setVisibility('editor_del_point','visible')
    setVisibility('editor_del_point_go_back','hidden')
    delSpotMode = false;
}

async function delSpotConfirm(){
    setVisibility('editor_add_point','visible')
    setVisibility('editor_del_point','visible')
    setVisibility('editor_del_point_confirm','hidden')
    setVisibility('editor_del_point_cancel_selection','hidden')
    delSpotMode = false;


    refreshToken()
    let authenticationToken = getCookie('authenticationToken')
    let cookie_sessionid = getCookie('cookie_sessionid')


    params = {
        "id" : last_selected_id,
        "authenticationToken": authenticationToken,
        "cookie_sessionid": cookie_sessionid
    }

    await axios.post('https://postavtezachotpozhaluysta.ru/rest/map/delete', params
    ).catch(error => {
        displayDelSpotError();
    }).then(response => {
        if(response.status!==200){
            displayDelSpotError();
        }
        else{
            location.reload();
        }
    })

}

function delSpotShowConfirmation(){
    setVisibility('editor_del_point_go_back','hidden')
    setVisibility('editor_del_point_confirm','visible')
    setVisibility('editor_del_point_cancel_selection','visible')
}

function delSpotCancelSelection(){
    setVisibility('editor_del_point_confirm','hidden')
    setVisibility('editor_del_point_go_back','visible')
    setVisibility('editor_del_point_cancel_selection','hidden')
}

function editor_del_spot_error_confirm() {
    closePopups();
}

function displayEditorFeature(){
    setVisibility('edit_btn', 'visible')
}

async function checkPrivileges(){

    let authenticationToken = getCookie('authenticationToken')
    let cookie_sessionid = getCookie('cookie_sessionid')
    if(authenticationToken){
        params = {
            "authenticationToken": authenticationToken,
            "cookie_sessionid": cookie_sessionid
        }

        await axios.post('https://postavtezachotpozhaluysta.ru/rest/acc/role', params
        ).catch(error => {

        }).then(response => {
            if(response.status!==200){

            }
            else{
                if(response.data=='Admin') {
                    displayEditorFeature();
                }
            }
        })
    }
}