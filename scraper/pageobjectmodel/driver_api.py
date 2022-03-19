# Driver API to interact with web elements

from selenium.webdriver import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC


class DriverAPI:

    def __init__(self, driver):
        self.driver = driver

    # Navigate to input URL
    def get_link(self, url):
        self.driver.get(url)
        self.driver.maximize_window()

    # Click on web element
    def click_on(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        element = WebDriverWait(self.driver, timeout).until(EC.element_to_be_clickable((method_used, element_method)))
        element.click()
        return element

    # Fill in data to fields on web
    def send_data(self, element_method, data, method_used=By.CSS_SELECTOR):
        element = self.get_element(element_method, method_used)
        element.send_keys(data)
        return element

    # Get web element
    def get_element(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        return self.find(element_method, method_used, timeout)

    # Get list of multiple web elements
    def get_elements(self, element_method):
        elements = self.driver.find_elements_by_css_selector(element_method)
        return elements

    # Try to find web element on page
    def find(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        try:
            element = WebDriverWait(self.driver, timeout).until(
                EC.presence_of_element_located((method_used, element_method)))

            return element
        except:
            return None

    # Get text from element
    def get_text(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        content = self.get_element(element_method, method_used, timeout)
        return content.text

    # Get list of text from list of elements
    def get_text_for_elements(self, element_method):
        elements = self.get_elements(element_method)
        list_text = []
        for element in elements:
            list_text.append(element.text)
        return list_text

    # Get current URL of the web page
    def get_current_url(self):
        return self.driver.current_url

    # Check for presence of element on web
    def check_element_present(self, element_method, method_used=By.CSS_SELECTOR, timeout=15):
        try:
            element = WebDriverWait(self.driver, timeout).until(
                EC.presence_of_element_located((element_method, method_used))
            )
            return (element is not None)
        except:
            return False

    # Check if web element is able to be clicked (for buttons)
    def check_element_clickable(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        try:
            element = self.wait_until_element_to_be_clickable(element_method, method_used, timeout)
            return (element is not None)
        except:
            return False

    # Check for web element presence on page
    def is_present_on_page(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        try:
            self.find(element_method, method_used, timeout)
            return True
        except:
            return False

    # Switch to iframe on web
    def switch_iframe(self, iframe_id):
        self.driver.switch_to.frame(iframe_id)

    # Switch out of iframe to default content
    def switch_default_content(self):
        self.driver.switch_to.default_content()

    # Close browser
    def quit(self):
        self.driver.quit()

    # Press enter button
    def click_enter(self, element_method, method_used=By.CSS_SELECTOR, timeout=10):
        WebDriverWait(self.driver, timeout).until(
            EC.presence_of_element_located((method_used, element_method))).send_keys(Keys.ENTER)
