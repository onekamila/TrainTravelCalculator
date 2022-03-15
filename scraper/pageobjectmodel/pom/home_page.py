from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config


class HomePage(BasePage):
    expected_url = Config.homepageUrl

    input_origin_field_CSS = "#mat-input-0"
    input_destination_field_CSS = "#mat-input-1"
    input_departure_date_CSS = "#mat-input-2"
    origin_dropdown_CSS = "#autocomplete__0 div:nth-child(1) > div.am-dropdown__option.am-simple-dropdown__option.ng-star-inserted"
    destination_dropdown_CSS = "#autocomplete__1 > div > div > div:nth-child(1) > div:nth-child(2)"
    search_train_button_CSS = "button.search-btn.ng-star-inserted"

    # def is_present(self):
    #     return self.driver.check_url(Config.homepageUrl)

    def fill_origin(self, content):
        self.driver.send_data(self.input_origin_field_CSS, content, timeout=10)

    def fill_destination(self, content):
        self.driver.send_data(self.input_destination_field_CSS, content, timeout=10)

    def fill_date(self, content):
        self.driver.send_data(self.input_departure_date_CSS, content, timeout=10)

    def click_origin_dropdown(self):
        self.driver.click_on(self.origin_dropdown_CSS, timeout=10)

    def click_destination_dropdown(self):
        self.driver.click_on(self.destination_dropdown_CSS, timeout=10)

    def click_search_train_button(self):
        self.driver.click_on(self.search_train_button_CSS, timeout=10)
