package com.github.santosleijon.frugalfennecbackend.bdd.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions

class ReportsSteps {

    private val pageUrl = "https://localhost:8080"

    @When("the user opens the reports page")
    fun openTheReportsPage() = runBlocking {
        CommonSteps.webDriver.get(pageUrl)
        CommonSteps.webDriver.clickOnButton("Reports")
    }

    @Then("the expense report shown contains the following values")
    fun assertExpenseReportContainsValues(values: DataTable) {
        val actualContent = CommonSteps.webDriver.getPageContent()

        val expectedValues = values.asMap().toMutableMap()

        expectedValues.remove("date")

        expectedValues.forEach { (date, amount) ->
            Assertions.assertThat(actualContent).contains(date)
            Assertions.assertThat(actualContent).contains(amount)
        }
    }
}