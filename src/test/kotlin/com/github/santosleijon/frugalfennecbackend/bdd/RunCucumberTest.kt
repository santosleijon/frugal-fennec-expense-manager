package com.github.santosleijon.frugalfennecbackend.bdd

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    glue = ["com.github.santosleijon.frugalfennecbackend.bdd.steps"],
    features = ["src/test/resources/bdd/features"],
)
class RunCucumberTest