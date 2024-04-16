package org.blitzstriker.pages.flightreservation;

import org.blitzstriker.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightConfirmationPage extends AbstractPage {

    private static final Logger log = LoggerFactory.getLogger(FlightConfirmationPage.class);

    @FindBy(xpath = "//div[contains(text(), 'Total Price')]/../div[2]/p")
    private WebElement totalPriceElement;

    @FindBy(xpath = "//div[contains(text(), 'Flight Confirmation')]/../div[2]/p")
    private WebElement flightConfirmationIdElement;

    public FlightConfirmationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        this.wait.until(ExpectedConditions.visibilityOf(this.totalPriceElement));
        return this.totalPriceElement.isDisplayed();
    }

    public String getPrice() {
        String confirmation = this.flightConfirmationIdElement.getText();
        String totalPrice = this.totalPriceElement.getText();
        log.info("Flight Confirmation #: {}", confirmation);
        log.info("Total Price: {}", totalPrice);
        return totalPrice;
    }
}
