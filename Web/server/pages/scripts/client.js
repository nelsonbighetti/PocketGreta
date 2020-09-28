

function runClient() {
    axios.get('https://postavtezachotpozhaluysta.ru/rest/map/all').then(resp => {
        for (var key in resp.data){
            addObj(resp.data[key]);
        }
    });
}

runClient()