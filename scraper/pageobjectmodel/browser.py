from selenium import webdriver
from selenium.webdriver.chrome.options import Options as COptions


def get_browser(headless=True):

    options = COptions()
    options.headless = headless
    return webdriver.Chrome(options=options, executable_path="../../drivers/chromedriver")


