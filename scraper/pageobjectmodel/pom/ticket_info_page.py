from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config


class TicketInformationPage(BasePage):
    # Ticket Information Page URL for checking page presence
    expected_url = Config.ticketInfoUrl

    train_number_list = "train-status-landing .train-number.ml-2"
    departure_time_list = "#page-content train-status-landing  train-status-mini  .time-container.d-flex span:nth-child(1)"
    arrival_time_list = "#page-content > div > train-status-landing span.time.time-arr"

    def __init__(self, driver, is_present=True):
        super().__init__(driver=driver, is_present=is_present)

    # Check page URL and presence
    def is_present(self):
        return self.expected_url == self.driver.get_current_url()

    # Get data on Train Number
    def get_train_number(self):
        return self.driver.get_text_for_elements(self.train_number_list)

    # Get data on Departure Time
    def get_departure_time(self):
        return self.driver.get_text_for_elements(self.departure_time_list)

    # Get data on Arrival Time
    def get_arrival_time(self):
        return self.driver.get_text_for_elements(self.arrival_time_list)
