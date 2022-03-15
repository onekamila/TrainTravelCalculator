from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin

app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

def runScraper(origin, dest):
    # TODO: Scrape the data here
    return [
        {
            "origin": origin, 
            "dest": dest,
            "train_name": "train123",
            "scheduled_departure": "time and date here",
            "scheduled_arrival": "time and date here",
        },
        {
            "origin": origin, 
            "dest": dest,
            "train_name": "train145",
            "scheduled_departure": "time and date here",
            "scheduled_arrival": "time and date here",
        },
    ]

# http://127.0.0.1:8080/?origin=Philly&destination=NewYork
@app.route("/")
@cross_origin()
def index():
    origin = request.args.get('origin')
    dest = request.args.get('destination')
    res = runScraper(origin, dest)
    return jsonify(res)

if __name__ == "__main__":
    app.run(debug=True, port=8080)