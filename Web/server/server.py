from flask_cors import CORS, cross_origin
from flask import Flask, request, abort, jsonify
from flask import send_file
import requests
import json
app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

uri = "457f5a38fd2e.ngrok.io"

@app.errorhandler(404)
def resource_not_found(e):
    return jsonify(error=str(e)), 404

@app.route('/rest/auth/login', methods=['POST'])
def login():
    path = "/rest/auth/login"
    creds = json.loads(request.data.decode("utf-8"))

    s = requests.Session()
    r = s.post('http://' + uri + path, json={"email": creds['email'], "password": creds['password']})
    if(r.status_code!=200):
        abort(404, description="Resource not found")
    payload = json.loads(r.content.decode("utf-8"))
    cookie = s.cookies.get_dict(uri)
    payload['cookie_sessionid'] = cookie['JSESSIONID']
    return payload

@app.route('/rest/acc/', methods=['POST', 'GET'])
def get_rating():
    payload = json.loads(request.data.decode("utf-8"))
    headers = {
        "Authorization" : "Bearer "+payload['authenticationToken'],
        "Cookie" : "JSESSIONID="+payload['cookie_sessionid']+"; Path=/; Domain=."+uri+"; HttpOnly;"
    }

    resp = requests.post('http://' + uri + "/rest/acc/"+payload['username'], headers=headers)
    if (resp.status_code != 200):
        abort(404, description="Resource not found")
    return resp.json()

@app.route('/rest/auth/signup', methods=['POST', 'GET'])
def register():
    creds = json.loads(request.data.decode("utf-8"))

    resp = requests.post('http://' + uri + "/rest/auth/signup", json={"username": creds['username'], "email": creds['email'], "password": creds['password']})

    if (resp.status_code != 200):
        abort(404, description="Invalid data")

    if(resp.content.decode('utf-8')!='User Registration Successful'):
        abort(404, description="Invalid data")

    return resp.content.decode('utf-8')

@app.route('/rest/refreshToken', methods=['POST', 'GET'])
def refresh_token():
    data = json.loads(request.data.decode("utf-8"))

    resp = requests.post('http://' + uri + "/rest/auth/refresh/token", json={"username": data['username'], "refreshToken": data['refreshToken']})

    if (resp.status_code != 200):
        abort(404, description="Invalid data")

    return resp.content.decode('utf-8')

@app.route('/rest/map/all', methods=['GET'])
def getMapObjects():
    resp = requests.get('http://' + uri + "/rest/map/all")
    if (resp.status_code != 200):
        abort(404, description="Invalid request")

    return resp.content.decode('utf-8')

@app.route('/rest/map/add', methods=['POST'])
def addMapObject():
    data = json.loads(request.data.decode("utf-8"))
    headers = {
        "Authorization": "Bearer " + data['authenticationToken'],
        "Cookie": "JSESSIONID=" + data['cookie_sessionid'] + "; Path=/; Domain=." + uri + "; HttpOnly;"
    }

    payload = {
        "descriptions" : data["descriptions"],
        "details" : data["details"],
        "latitude" : data["latitude"],
        "longitude" : data["longitude"],
        "subtype" : data["subtype"],
        "type" : data["type"]
    }

    resp = requests.post('http://' + uri + "/rest/map/add", json=payload, headers=headers)
    resp_str = resp.content.decode('utf-8')
    if (resp.status_code != 200 or resp_str!='ok'):
        abort(404, description="Resource not found")
    return resp_str


@app.route('/', methods=['GET'])
def index():
    return send_file('./pages/index.html', mimetype='text/html')

@app.route('/scripts/<string:name>', methods=['GET'])
def get_script(name):
    return send_file('./pages/scripts/'+name, mimetype='text/javascript')

@app.route('/stylesheets/<string:name>', methods=['GET'])
def get_styles(name):
    return send_file('./pages/stylesheets/'+name, mimetype='text/css')


@app.route('/resources/icons/<string:name>', methods=['GET'])
def get_resource(name):
    return send_file('./resources/icons/'+name, mimetype='image/gif')

@app.route('/resources/images/<string:name>', methods=['GET'])
def get_image(name):
    return send_file('./resources/images/'+name, mimetype='image/gif')


if __name__ == '__main__':
    app.run(host= '0.0.0.0', debug=True, port=443, ssl_context=('certs/localhost.crt', 'certs/localhost.key'))