package com.balsam.test1.cucumber;

import com.balsam.test1.BalsamTestApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = BalsamTestApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
