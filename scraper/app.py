from flask import Flask, jsonify, request

app = Flask(__name__)


def runScraper(origin, dest, departure_date):
    # TODO: Scrape the data here
    return [
        {
            "origin": origin,
            "dest": dest,
            "departure_date": departure_date,
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
def index():
    origin = request.args.get('origin')
    dest = request.args.get('destination')
    res = runScraper(origin, dest)
    return jsonify(res)


if __name__ == "__main__":
    app.run(debug=True, port=8080)
