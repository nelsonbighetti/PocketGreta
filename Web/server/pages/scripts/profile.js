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

    await axios.post('https://localhost:5000/rest/auth/login', params
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

    await axios.post('https://localhost:5000/rest/auth/signup', params
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

    axios.post('https://localhost:5000/rest/refreshToken', params
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

        await axios.post('https://localhost:5000/rest/acc/', params
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
}

function logout() {
    eraseCookie("authenticationToken")
    eraseCookie("refreshToken")
    eraseCookie("username")
    eraseCookie("cookie_sessionid")
    eraseCookie("token_expires")

    setVisibility("login_btn", "visible")
    setVisibility("register_btn", "visible")
    setVisibility("profile", "hidden")
    setVisibility("rating", "hidden")
    setVisibility("logout_btn", "hidden")
    setVisibility("email", "hidden")
}
