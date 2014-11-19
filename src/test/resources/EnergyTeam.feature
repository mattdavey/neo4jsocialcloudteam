Feature: Diamond energy workitem problem

  Background:

  Scenario: As a user I want to have an energy project that has a diamond set of dependencies which can be mapped to a team
    Given the following users are logged in
    | WorkItem | Start-Start | Start-Finish | Finish-Finish | Partial-Finish |
    | A        |             |              |               |                |
    | B        |             |              |               |                |
    | C        |             |              |               |                |
    | D        |             |              |               |                |
    When the following team is available
    | Resource | Role       |
    | Matt     | Engineer   |
    | Ross     | Management |
    Then the following alerts are generated
    | Alert | Message |
    | 1     |         |

