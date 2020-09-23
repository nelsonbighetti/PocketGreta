

function runClient() {
    axios.get('https://localhost:5000/rest/map/all').then(resp => {
        for (var key in resp.data){
            addObj(resp.data[key]);
        }
    });
}

runClient()