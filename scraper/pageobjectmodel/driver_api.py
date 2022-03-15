from selenium.webdriver import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC


class DriverAPI:

    def __init__(self, driver):
        self.driver = driver

    def get_link(self, url):
        self.driver.get(url)
        self.driver.maximize_window()

    def click_on(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        element = WebDriverWait(self.driver, timeout).until(EC.element_to_be_clickable((method_used, element_method)))
        element.click()
        return element

    def send_data(self, element_method, data, method_used=By.CSS_SELECTOR):
        element = self.get_element(element_method, method_used)
        element.send_keys(data)
        return element

    def get_element(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        return self.find(element_method, method_used, timeout)

    def get_elements(self, element_method):
        elements = self.driver.find_elements_by_css_selector(element_method)
        return elements

    def find(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        try:
            element = WebDriverWait(self.driver, timeout).until(
                EC.presence_of_element_located((method_used, element_method)))

            return element
        except:
            return None

    def get_text(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        content = self.get_element(element_method, method_used, timeout)
        return content.text

    def get_text_for_elements(self, element_method):
        elements = self.get_elements(element_method)
        list_text = []
        for element in elements:
            list_text.append(element.text)
        return list_text

    def is_present_on_page(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):

        if self.get_element(element_method, method_used, timeout) is not None:
            return True
        else:
            return False

    def get_current_url(self):
        return self.driver.current_url

    def quit(self):
        self.driver.quit()

    def click_enter(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        WebDriverWait(self.driver, timeout).until(
            EC.presence_of_element_located((method_used, element_method))).send_keys(Keys.ENTER)
