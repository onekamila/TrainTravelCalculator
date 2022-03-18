# Amtrak Home Page

from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.config import Config


class HomePage(BasePage):
    # Home Page URL for checking page presence
    expected_url = Config.homepageUrl

    # CSS Selector for locating page elements to interact with
    input_origin_field_CSS = "#page-content  .amtrak-ff-body .from-station input"
    input_destination_field_CSS = "#mat-input-1"
    input_departure_date_CSS = "#mat-input-2"
    search_train_button_CSS = "button.search-btn.ng-star-inserted"

    def __init__(self, driver, is_present=True):
        super().__init__(driver=driver, is_present=is_present)

    # Check page URL and presence
    def is_present(self):
        return self.expected_url == self.driver.get_current_url()

    # Fill in search box place of origin data for train searching purpose
    def fill_origin(self, content):
        self.driver.send_data(self.input_origin_field_CSS, content)

    # Fill in search box place of destination data for train searching purpose
    def fill_destination(self, content):
        self.driver.send_data(self.input_destination_field_CSS, content)

    # Fill in search box departure date data for train searching purpose
    def fill_date(self, content):
        self.driver.send_data(self.input_departure_date_CSS, content)
        self.driver.click_enter(self.input_departure_date_CSS)

    # Click on "Find Train" button to search for trains
    def click_search_train_button(self):
        self.driver.click_on(self.search_train_button_CSS, timeout=15)
