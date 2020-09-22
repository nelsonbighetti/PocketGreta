

function runClient() {
    axios.get('http://localhost:5000/rest/map/all').then(resp => {
        for (var key in resp.data){
            addObj(resp.data[key]);
        }
    });
}

runClient()