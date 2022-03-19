# Amtrak Home Page
from scraper.pageobjectmodel.pom.base.base_page import BasePage
from scraper.pageobjectmodel.execute.config import Config


class HomePage(BasePage):
    # Home Page URL for checking page presence
    expected_url = Config.homepageUrl

    # CSS Selector for locating page elements to interact with
    train_status_option_CSS = "#top_navigation_body > header > div.page-header__navbar.show-navbar > div.wrapper.am-g > div.site-secondary-nav.am-js__site-secondary-nav > ul > li:nth-child(1) > btn-travel-status"
    pick_date_option_CSS = "#top_nav_wrapper > travel-status > div.position-fixed.travel-status.shadow-2.ng-star-inserted > div > travel-status-form > form > div.select-date.section.px-lg-4.mb-4.mb-lg-0.position-relative > div.d-flex.flex-row.flex-lg-column.justify-content-start.justify-content-lg-between.align-items-center.align-items-lg-stretch > div.order-1.order-lg-2.d-flex.d-lg-block.justify-content-center.align-items-center > am-dropdown > div > div.am-dropdown-btn > button" #".select-date.section"
    date_list_CSS = "#top_nav_wrapper > travel-status .travel-status am-datepicker-standalone  ngb-datepicker > div.ngb-dp-months ngb-datepicker-month-view   span"
    input_destination_field_CSS = "#autocomplete-combox__2 .mat-form-field-flex input"
    input_departure_date_CSS = "#autocomplete-combox__3 .mat-form-field-flex input"
    check_status_button_CSS = "#top_nav_wrapper > travel-status button.status_submit"

    def __init__(self, driver, is_present=True):
        super().__init__(driver=driver, is_present=is_present)

    # Check page URL and presence
    def is_present(self):
        return self.expected_url == self.driver.get_current_url()

    # Fill in search box place of origin data for train searching purpose
    def fill_origin(self, content):
        self.driver.send_data(self.input_destination_field_CSS, content)

    # Fill in search box place of destination data for train searching purpose
    def fill_destination(self, content):
        self.driver.send_data(self.input_departure_date_CSS, content)

    # Fill in search box departure date data for train searching purpose
    def choose_date(self, date):
        self.driver.click_on(self.pick_date_option_CSS)
        self.driver.pick_date(self.date_list_CSS, date)

    # Click on "Find Train" button to search for trains
    def click_search_train_button(self):
        self.driver.click_on(self.check_status_button_CSS, timeout=15)

    # Search by Train Status
    def click_option_check_train_status(self):
        self.driver.click_on(self.train_status_option_CSS)
