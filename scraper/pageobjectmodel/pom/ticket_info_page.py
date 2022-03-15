from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config
from selenium.webdriver.common.by import By


class TicketInformationPage(BasePage):
    expected_url = Config.ticketInfoUrl

    train_number_list = "train-info span:nth-child(1)"
    departure_time_list = "div.departure .departure-details span.font-light"
    arrival_time_list = ".arrival .departure-details span.font-light"

    def __init__(self, driver, is_present=True):
        super().__init__(driver=driver, is_present=is_present)

    def is_present(self):
        return self.expected_url == self.driver.get_current_url()

    def get_train_number(self):
        self.driver.get_text_for_elements(self.train_number_list)

    def get_departure_time(self):
        self.driver.get_text_for_elements(self.departure_time_list)

    def get_arrival_time(self):
        self.driver.get_text_for_elements(self.arrival_time_list)
