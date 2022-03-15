from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config


class HomePage(BasePage):
    expected_url = Config.homepageUrl

    input_origin_field_CSS = "#page-content  .amtrak-ff-body .from-station input"
    input_destination_field_CSS = "#mat-input-1"
    input_departure_date_CSS = "#mat-input-2"
    origin_dropdown_CSS = "#autocomplete__0 div:nth-child(1) > div.am-dropdown__option.am-simple-dropdown__option.ng-star-inserted"
    destination_dropdown_CSS = "#autocomplete__1 > div > div > div:nth-child(1) > div:nth-child(2)"
    search_train_button_CSS = "button.search-btn.ng-star-inserted"

    def __init__(self, driver, is_present=True):
        super().__init__(driver=driver, is_present=is_present)

    def is_present(self):
        return self.expected_url == self.driver.get_current_url()

    def fill_origin(self, content):
        self.driver.send_data(self.input_origin_field_CSS, content, timeout=20)

    def fill_destination(self, content):
        self.driver.send_data(self.input_destination_field_CSS, content, timeout=10)

    def fill_date(self, content):
        self.driver.send_data(self.input_departure_date_CSS, content, timeout=10)

    def click_search_train_button(self):
        self.driver.click_on(self.search_train_button_CSS, timeout=10)
