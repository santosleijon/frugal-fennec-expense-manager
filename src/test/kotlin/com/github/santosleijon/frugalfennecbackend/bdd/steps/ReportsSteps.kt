package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.utils.waitUntilOrThrow
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking

class ReportsSteps {

    private val reportsPageUrl = "http://localhost:8080/reports"

    @When("the user opens the reports page")
    fun openTheReportsPage() = runBlocking {
        CommonSteps.webDriver.get(reportsPageUrl)
    }

    @Then("the expense report shown contains the following values")
    fun assertExpenseReportContainsValues(values: DataTable): Unit = runBlocking {
        waitUntilOrThrow {
            val actualContent = CommonSteps.webDriver.getPageContent()

            val expectedValues = values.asMap().toMutableMap()

            expectedValues.remove("date")

            expectedValues.all { (date, amount) ->
                actualContent.contains(date)
                actualContent.contains(amount)
            }
        }
    }
}