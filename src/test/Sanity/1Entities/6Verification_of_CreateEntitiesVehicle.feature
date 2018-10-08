Feature: 6Verification_of_CreateEntitiesVehicle sanity scenario for entities feature Vehicle

#####################   Test Vehicle entity ####################################
  @sanity @vehicle
  Scenario Outline: Add Vehicle entity with just mandatory fields and save it
              ############# LOG - IN start #################
    Given navigate to "http://10.32.5.73/ui/" on browser
    When type into "login_userNameField" the text "wstu5"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
         ############# LOG - IN end #################
    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityVehicleButton"
    And wait for "vehicle_vehicleTitle" is displaying on screen with timeout of "<timeout>"
    When type into "vehicle_Registration" the text "6766676"
    When type into "vehicle_Tipo" the text "Autobus"
    And press on ENTER
    And press on button "vehicle_marker"
     
    And press on ENTER
     
    And press on button "vehicle_model"
     
    And press on ENTER
     
    And press on button "vehicle_color"
     
    And press on ENTER
     
    And type into "vehicle_capacity" the text "10"
    And type into "vehicle_manufacturingYear" the text "1950"
    And type into "vehicle_engineNumber" the text "1111"
    And type into "vehicle_chassisNumber" the text "2222"
    And type into "vehicle_description" the text "description"
     
    # set registration date to 1/1/2018
    And press on button "vehicle_registrationDate"
    And press on button "vehicle_date"
    And press on button "vehicle_date"
    And press on button "vehicle_year2018"
    And press on button "vehicle_jan"
    And press on button "vehicle_first"
                ######################## Add Link - START ##################################
    And press on button "Common_RelatedPeople"
    And Hold for "2" seconds
    And Insert "person" ID to "Common_LinkPerson"
    And press on ENTER
    And press on button "Common_LinkFirstResultSearch"
    And press on button "Common_LinkPersonAccept"
    And press on button "Common_LinkPersonOKButton"
    Then check if item is visible "common_LinkIcon" with timeout of "<timeout>"
                ########################Add Link - END ##################################
                ########################Add TAG - START  ##################################
    And press on button "common_addTag"
     
    And Insert data from "<jsonPath>" to "common_tagName"
    And press on ENTER
    And wait for "common_saveTag" is displaying on screen with timeout of "<timeout>"
    And press on button "common_saveTag"
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And Hold for "5" seconds
    And press on button "common_saveButton"
    And Hold for "3" seconds
    Then get Element ID from "vehicle_vehicleID"
    Then set PID to "*"
                 ######################## Add TAG - END ##################################
                ########################Create Report - START ############################
    ####CLEAN DOWNLOAD FOLDER
    When Delete all files from folder
    ####PDF
     
    And wait for "common_hamburgerButton" is displaying on screen with timeout of "<timeout>"
   And wait for "common_hamburgerButton" to be clickable with timeout of "<timeout>"
     
    And press on button "common_hamburgerButton"
    And Hover on Element "common_createReport" and click on "common_pdf"
    And accept chrome alert
    And Hold for "5" seconds
    Then check if file exists "<pdf>"
    ####WORD
    And wait for "common_hamburgerButton" is displaying on screen with timeout of "<timeout>"
     
    And press on button "common_hamburgerButton"
    And Hover on Element "common_createReport" and click on "common_word"
    And accept chrome alert
    And Hold for "5" seconds
    Then check if file exists "<word>"
    ####Power Point
    And wait for "common_hamburgerButton" is displaying on screen with timeout of "<timeout>"
     
    And press on button "common_hamburgerButton"
    And Hover on Element "common_createReport" and click on "common_powerpoint"
    And accept chrome alert
    And Hold for "5" seconds
    ####Check if file created
    Then check if file exists "<Power Point>"
                ########################Create Report - END############################
    And Hold for "5" seconds

    # check display on screen
    Then The expected  "vehicle_registrationView" is Equal to the actual "6766676"
    Then The expected  "vehicle_typeView" is Equal to the actual "Autobus"
    Then The expected  "vehicle_markerView" is Equal to the actual "A canc"
    Then The expected  "vehicle_modelView" is Equal to the actual "175 cm3"
    Then The expected  "vehicle_colorView" is Equal to the actual "Azul claro"
    Then The expected  "vehicle_capacityView" is Equal to the actual "10"
    Then The expected  "vehicle_manufacturingYearView" is Equal to the actual "1950"
    Then The expected  "vehicle_engineNumberView" is Equal to the actual "1111"
    Then The expected  "vehicle_chassisNumberView" is Equal to the actual "2222"
    Then The expected  "vehicle_registrationDateView" is Equal to the actual "01/01/2018"
    Then The expected  "vehicle_descriptionView" is Equal to the actual "description"

    Examples:
      | timeout | jsonPath                               | word                                           | pdf                                           | Power Point                                    |
      | 10      | src\main\resources\personInfoData.json | Autobus A canc 175 cm3 Azul claro 6766676.word | Autobus A canc 175 cm3 Azul claro 6766676.pdf | Autobus A canc 175 cm3 Azul claro 6766676.pptx |
