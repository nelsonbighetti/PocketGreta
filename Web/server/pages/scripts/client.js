

function runClient() {
    axios.get('https://176.119.157.59:5000/rest/map/all').then(resp => {
        for (var key in resp.data){
            addObj(resp.data[key]);
        }
    });
}

runClient()