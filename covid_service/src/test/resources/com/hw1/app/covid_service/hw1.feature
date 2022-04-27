Feature: Search Covid-19 statistics

    Scenario: Search statistics of USA for the last year

        When I access 'https://blazedemo.com/'
        Then page title should be "Covid-19"
        And I want to search statistics from "USA"
        And I search for the "last year"
        Then country title should be "USA"
        And statitic differential text should be last "356 days"

    Scenario: Search statistics of USA for the last month

        When I access 'https://blazedemo.com/'
        Then page title should be "Covid-19"
        And I want to search statistics from "USA"
        And I search for the "last month"
        Then country title should be "USA"
        And statitic differential text should be last "30 days"

    Scenario: Search statistics of USA for the last week

        When I access 'https://blazedemo.com/'
        Then page title should be "Covid-19"
        And I want to search statistics from "USA"
        And I search for the "last week"
        Then country title should be "USA"
        And statitic differential text should be last "7 days"

    Scenario: Search statistics of France for the today

        When I access 'https://blazedemo.com/'
        Then page title should be "Covid-19"
        And I want to search statistics from "France"
        And I search for the "today"
        Then country title should be "France"
        And statitic differential text should be last "1 day"

    Scenario: Search statistics of France for the last week

        When I access 'https://blazedemo.com/'
        Then page title should be "Covid-19"
        And I want to search statistics from "France"
        And I search for the "last week"
        Then country title should be "France"
        And statitic differential text should be last "7 days"

