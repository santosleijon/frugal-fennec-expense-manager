package com.github.santosleijon.frugalfennecbackend.bdd.webdriver

import com.github.santosleijon.frugalfennecbackend.bdd.utils.waitFor
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Actions

class ExtendedWebDriver(options: ChromeOptions) : ChromeDriver(options) {
    companion object {
        fun createWithWebDriverManager(): ExtendedWebDriver {
            WebDriverManager.chromedriver().setup()

            val options = ChromeOptions().addArguments("ignore-certificate-errors")

            return ExtendedWebDriver(options)
        }
    }

    fun getPageContent(): String {
        return findElement(By.tagName("body")).text
    }

    fun findInputElementByValue(value: String): WebElement {
        return findElement(By.xpath("//input[@value='$value']"))
    }

    suspend fun enterTextIntoElementWithId(text: String, id: String) {
        val element = findElement(By.id(id))
        clearInputElement(element)
        element.sendKeys(text)
        waitFor(500L)
    }

    suspend fun clickOnButton(buttonText: String) {
        val button = findElement(By.xpath("//button/*[text()='$buttonText']/.."))
        button.click()
        waitFor(2000L) // TODO: Remove fixed waiting time by replacing it with a conditional waiting loop
    }

    fun doubleClickOnElementWithText(text: String) {
        val accountNameCell = findElement(By.xpath("//*[contains(text(),'$text')]"))
        Actions(this).doubleClick(accountNameCell).perform()
    }

    fun enterValueIntoInputElement(inputElement: WebElement, newValue: String) {
        clearInputElement(inputElement)
        inputElement.sendKeys(newValue)
    }

    fun selectOptionFromDropdownElement(elementId: String, optionText: String) {
        this.findElement(
            By.xpath("//*[@id='$elementId']")
        ).click()

        this.findElement(
            By.xpath("//ul[@aria-labelledby='$elementId-label']/li[text()='$optionText']")
        ).click()
    }

    // Workaround since the clear() does not always work
    // https://stackoverflow.com/questions/50677760/selenium-clear-command-doesnt-clear-the-element
    private fun clearInputElement(inputElement: WebElement) {
        while (inputElement.getAttribute("value") != "") {
            inputElement.sendKeys(Keys.BACK_SPACE)
        }
    }
}