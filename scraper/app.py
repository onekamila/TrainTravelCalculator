from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin

from scraper.pageobjectmodel.browser import get_browser
from scraper.pageobjectmodel.execute.scraper_execution import ScraperExecution

app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


def runScraper(origin, dest, departure_date):
    driver = get_browser()
    scrape = ScraperExecution(driver)
    trains = scrape.search_train(origin, dest, departure_date)
    scrape.driver.quit()
    return trains


# http://127.0.0.1:8080/?origin=Philly&destination=NewYork
@app.route("/")
@cross_origin()
def index():
    origin = request.args.get('origin')
    dest = request.args.get('destination')
    departure_date = request.args.get('departure_date')
    res = runScraper(origin, dest, departure_date)
    return jsonify(res)


if __name__ == "__main__":
    app.run(debug=True, port=8080)
