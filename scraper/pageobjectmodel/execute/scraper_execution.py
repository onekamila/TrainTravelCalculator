import time

from scraper.pageobjectmodel.execute.config import Config
from scraper.pageobjectmodel.pom.ticket_info_page import TicketInformationPage
from scraper.pageobjectmodel.pom.home_page import HomePage
from scraper.pageobjectmodel.driver_api import DriverAPI


class ScraperExecution:

    def __init__(self, driver):
        self.driver = DriverAPI(driver)

    def search_train(self, origin_info, destination_info, date_info):
        # Navigate to Amtrak Home Page
        self.driver.get_link(Config.homepageUrl)
        # Create object of Home Page
        home = HomePage(self.driver)
        # Click on option to search for train status
        home.click_option_check_train_status()
        # Enter train information to search
        home.fill_origin(origin_info)
        home.fill_destination(destination_info)
        home.choose_date(date_info)
        # Click search button
        home.click_search_train_button()
        time.sleep(10)  # Time sleep for page relocating and page loading
        return self.scrape_train_data()

    def scrape_train_data(self):
        # Create object of Ticket Information Page
        ticket = TicketInformationPage(self.driver)
        # Get train information
        train_number_list = ticket.get_train_number()
        departure_time_list = ticket.get_departure_time()
        arrival_time_list = ticket.get_arrival_time()
        # Reformat train information to serve backend input
        train_data = self.reformat_train_info(train_number_list, departure_time_list, arrival_time_list)
        return train_data

    @staticmethod
    def reformat_train_info(train_number_list, departure_time_list, arrival_time_list):
        output_list = []
        for i in range(len(train_number_list)):
            train_entity = {
                "train_name": train_number_list[i],
                "scheduled_departure": departure_time_list[i][9:],
                "scheduled_arrival": arrival_time_list[i][9:],
            }
            output_list.append(train_entity)
        return output_list
