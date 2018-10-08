Feature: 1Verification_of_CreateEntitiesPerson sanity scenario for entities Person feature

#####
# ###############   Login to the system ####################################
  @sanity @person
  Scenario Outline: Log in to WS system

    Given navigate to "https://3.213.176.52" on browser
    When type into "login_userNameField" the text "wstu6"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then Hold for "15" seconds
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"

         ############# LOG - IN end #################
    Then check for error messages using the image "<errorImage>"
    Then check for error messages using the image "<greenOkButtonImage>"

    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityPersonButton"
    And wait for "person_FirstNameField" is displaying on screen with timeout of "<timeout>"
    When Insert data from "<jsonPath>" to "person_FirstNameField"
    When Insert data from "<jsonPath>" to "person_LastNameField"
    And Hold for "3" seconds
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And press on button "common_saveButton"
    And Hold for "5" seconds
    And get Element ID from "person_WsID"
    And press on ENTER
    And press on tab number "1" of "topbar_tabsOnMainToolbar"
    And wait for "Common_EditButton" is displaying on screen with timeout of "<timeout>"
    And press on button "Common_EditButton"
    And type into "person_RFC" the text "<RFC>"
#    And type into "person_LastMotherNameField" the text "madre name"
    And press on button "person_Gender"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "person_civilState"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "person_illegalCitizen"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "person_religion"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And type into "person_CURP" the text "curp"
    And Hold for "1" seconds
    And press on button "person_visaForUSA"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "person_bloodType"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "person_country"
    And Hold for "3" seconds
    And press on ENTER
    And Hold for "5" seconds
    And press on button "person_state"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    # set birth date to 1/1/2010
    And press on button "person_birthDate"
    And Hold for "1" seconds
    And press on button "person_date"
    And Hold for "1" seconds
    And press on button "person_date"
    And Hold for "1" seconds
    And press on button "person_year2010"
    And Hold for "1" seconds
    And press on button "person_jan"
    And Hold for "1" seconds
    And press on button "person_first"
    And Hold for "1" seconds
    # set death date to 1/1/2018
    And press on button "person_deathDate"
    And Hold for "1" seconds
    And press on button "person_date"
    And Hold for "1" seconds
    And press on button "person_date"
    And Hold for "1" seconds
    And press on button "person_year2018"
    And Hold for "1" seconds
    And press on button "person_jan"
    And Hold for "1" seconds
    And press on button "person_first"
    And Hold for "5" seconds

########################### Test MAP Output - START ###########################################
    And wait for "map_AddDirection" is displaying on screen with timeout of "<timeout>"
    And press on button "map_AddDirection"

    And wait for "map_Definir" is displaying on screen with timeout of "<timeout>"
    And press on button "map_Definir"

    ####### Insert country
    And wait for "map_pais" is displaying on screen with timeout of "<timeout>"
#    Then press on button "map_pais"
    Then type into "map_pais" the text "Mexico"
    And Hold for "2" seconds
    And press on ENTER
    ####### Insert State
    Then type into "map_Estado" the text "México"
    And Hold for "2" seconds
    And press on ENTER
    ####### Insert Delegation
    Then type into "map_delegaci" the text "Acolman"
    And Hold for "2" seconds
    And press on ENTER
    And Hold for "2" seconds
 ################  Assert the Actual Data with the Expected Data from coordinates - Start
    Then press on button "map_OpenCoordinates"
    ######## Assert lat

    Then check if the value "<lat>" equal to the value in the field "map_lat"
    ####### Assert long
    Then check if the value "<lng>" equal to the value in the field "map_long"
    And press on button "common_saveButtonOK"
################  Assert the Actual Data with the Expected Data from coordinates - End
     ########################## Test MAP Output - END ###########################################


    Then press on button "common_saveButton"

    Then wait for "person_editPersonSpan" is displaying on screen with timeout of "5"

    Then press on button "person_editPersonSpan"
    Then Hold for "2" seconds
    Then check for error messages using the image "<errorImage>"
    # check display on screen

    Then The expected  "person_genderView" is Equal to the actual "Femenino"
    Then The expected  "person_civilStateView" is Equal to the actual "Casado(a)"
    Then The expected  "person_relegionView" is Equal to the actual "Ateo"
    Then The expected  "person_illegalCitizenView" is Equal to the actual "Sí"
    Then The expected  "person_rfcView" is Equal to the actual "<RFC>"
    Then The expected  "person_curpView" is Equal to the actual "curp"
    Then The expected  "person_visaView" is Equal to the actual "Sí"
    Then The expected  "person_bloodTypeView" is Equal to the actual "A rh +"
    Then The expected  "person_birthPlaceView" is Equal to the actual "badakhshān, afganistán"
    Then The expected  "person_birthDateView" is Equal to the actual "01/01/2010"
    Then The expected  "person_deathDateView" is Equal to the actual "01/01/2018"

    Examples:
      | timeout | RFC      | jsonPath                               | errorImage                                               | greenOkButtonImage                                               | lat       | lng        |
      | 10      | RFC Data | src\main\resources\personInfoData.json | src\test\resources\ImageRepository\Errors\closeError.PNG | src\test\resources\ImageRepository\Errors\closeGreenMessage.PNG" | 19.633778 | -98.919861 |
