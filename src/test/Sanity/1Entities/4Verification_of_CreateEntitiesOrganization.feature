Feature: 4Verification_of_CreateEntitiesOrganization sanity scenario for entities feature organization

#####################   Test organization entity ####################################
  @sanity @organization
  Scenario Outline: Add organization entity with just mandatory fields and save it
              ############# LOG - IN start #################
    Given navigate to "http://10.32.5.73/ui/" on browser
    When type into "login_userNameField" the text "wstu5"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
         ############# LOG - IN end #################
    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityOrgButton"

    And wait for "organization_organizationTitle" is displaying on screen with timeout of "<timeout>"
    And Hold for "3" seconds
    When Insert data from "<jsonPath>" to "organization_Registration"
    When Insert data from "<jsonPath>" to "organization_Tipo"
    And press on ENTER
    And Hold for "3" seconds

    And press on button "organization_activeStatus"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And type into "organization_numberOfMembers" the text "11"
    And type into "organization_id" the text "777"
    And type into "organization_description" the text "description"
    And Hold for "1" seconds
    # set activeFrom date to 1/1/2018
    And press on button "organization_activeFromDate"
    And Hold for "1" seconds
    And press on button "organization_date"
    And Hold for "1" seconds
    And press on button "organization_date"
    And Hold for "1" seconds
    And press on button "organization_year2018"
    And Hold for "1" seconds
    And press on button "organization_jan"
    And Hold for "1" seconds
    And press on button "organization_first"
    And Hold for "1" seconds

        ########################## Test MAP Output - START ###########################################
    And wait for "map_AddDirection" is displaying on screen with timeout of "<timeout>"
    And press on button "map_AddDirection"
    And wait for "map_Definir" is displaying on screen with timeout of "<timeout>"
    And press on button "map_Definir"
    And Hold for "2" seconds
    ####### Insert country
    And wait for "map_pais" is displaying on screen with timeout of "<timeout>"
#    Then press on button "map_pais"
    Then type into "map_pais" the text "<country>"
    And Hold for "2" seconds
    And press on ENTER
    ####### Insert State
    Then type into "map_Estado" the text "<State>"
    And Hold for "2" seconds
    And press on ENTER
    ####### SAVE MAP FORM - Start
    And Hold for "2" seconds
    And press on button "common_saveButtonOK"
    ####### SAVE MAP FORM - End
    #################### Save form - Start
    And Hold for "3" seconds
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And press on button "common_saveButton"
    And Hold for "2" seconds
    #################### Save form - End
    Then get Element ID from "organization_organizationId"
    # check display on screen
    Then The expected  "organization_companyNameView" is Equal to the actual "La Familia"
    Then The expected  "organization_typeView" is Equal to the actual "Cartel"
    Then The expected  "organization_activeStatusView" is Equal to the actual "Activo"
    Then The expected  "organization_numberOfMembersView" is Equal to the actual "11"
    Then The expected  "organization_idView" is Equal to the actual "777"
    Then The expected  "organization_activeFromView" is Equal to the actual "01/01/2018"
    Then The expected  "organization_descriptionView" is Equal to the actual "description"


    Examples:


      | timeout | country | State  | jsonPath                               |
      | 10      | Mexico  | México | src\main\resources\personInfoData.json |