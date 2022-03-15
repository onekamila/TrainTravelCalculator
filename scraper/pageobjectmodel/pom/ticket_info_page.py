from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config
from selenium.webdriver.common.by import By


class TicketInformationPage(BasePage):
    expected_url = Config.ticketInfoUrl

    # CSS Selector
    train_number_list = "train-info span:nth-child(1)"
    departure_time_list = "div.departure .departure-details span.font-light"
    arrival_time_list = ".arrival .departure-details span.font-light"

    def get_train_number(self):
        self.driver.get_text(self.train_number_list)

    def get_departure_time(self):
        self.driver.get_text(self.departure_time_list)

    def get_arrival_time(self):
        self.driver.get_text(self.arrival_time_list)
