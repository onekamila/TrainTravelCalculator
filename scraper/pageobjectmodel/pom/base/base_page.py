from scraper.pageobjectmodel.pom.base.base_po import BasePageObject


class BasePage(BasePageObject):
    expect_url = ""

    def is_present(self):
        if self.expect_url == "":
            raise Exception("URL page not implemented")
        return self.expect_url == self.driver.get_current_url()

    def get_expect_url(self):
        return self.expect_url

