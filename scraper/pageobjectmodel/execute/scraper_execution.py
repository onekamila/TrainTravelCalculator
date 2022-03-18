import time

from scraper.pageobjectmodel.browser import get_browser
from scraper.pageobjectmodel.execute.config import Config
from scraper.pageobjectmodel.pom.ticket_info_page import TicketInformationPage
from scraper.pageobjectmodel.pom.home_page import HomePage
from scraper.pageobjectmodel.driver_api import DriverAPI


class ScraperExecution:

    def __init__(self, driver):
        self.driver = DriverAPI(driver)

    def search_train(self, origin_info, destination_info, date_info):
        self.driver.get_link(Config.homepageUrl)
        home = HomePage(self.driver)
        home.fill_origin(origin_info)
        home.fill_destination(destination_info)
        home.fill_date(date_info)
        home.click_search_train_button()
        time.sleep(10)  # Time sleep for page relocating and page loading
        return self.scrape_train_data()

    def scrape_train_data(self):
        ticket = TicketInformationPage(self.driver)
        train_number_list = ticket.get_train_number()
        departure_time_list = ticket.get_departure_time()
        arrival_time_list = ticket.get_arrival_time()
        return [train_number_list, departure_time_list, arrival_time_list] 
    #new code: 
    @staticmethod
    def scrapeCombine(scrape_train_data):
        group = [[scrape_train_data.train_number_list], [scrape_train_data.departure_time_list],[scrape_train_data.arrival_time_list]]
        trainInfo = []
        index = 0
        for i in range(len(group[0])):
            trainInfo.append([])
            for j in range(len(group)):
                trainInfo[index].append(group[j][index])
        index += 1
        return trainInfo

if __name__ == "__main__":
    driver = get_browser()
    scrape = ScraperExecution(driver)
    scrape.search_train("PHL", "BOS", "03/16/2022")
    scrape.driver.quit()
