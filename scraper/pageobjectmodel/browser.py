from selenium import webdriver
from selenium.webdriver.chrome.options import Options as COptions


# Initiate a new Chrome browser
def get_browser(headless=True):
    options = COptions()
    options.headless = headless
    driver = webdriver.Chrome(options=options, executable_path="drivers/chromedriver")
    return driver
