from scraper.pageobjectmodel.driver_api import DriverAPI


class BasePageObject:
    def __init__(self, driver, is_present=True):
        self.driver = DriverAPI(driver)
        if is_present is True:
            assert self.is_present()

    def is_present(self):
        return False
