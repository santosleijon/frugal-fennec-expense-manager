package com.github.santosleijon.frugalfennecbackend

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    glue = ["com.github.santosleijon.frugalfennecbackend.steps"],
    features = ["src/test/resources/features"],
)
class RunCucumberTest