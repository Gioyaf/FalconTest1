Feature: 5Verification_of_CreateEntitiesTelephone sanity scenario for entities feature Telephone

#####################   Test Telephone entity ####################################
  @sanity @telephone
  Scenario Outline: Add Telephone entity with just mandatory fields and save it
              ############# LOG - IN start #################
    Given navigate to "http://10.32.5.73/ui/" on browser
    When type into "login_userNameField" the text "wstu5"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
         ############# LOG - IN end #################
    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityTelephoneButton"
    And wait for "Telephone_telephoneNumber" is displaying on screen with timeout of "<timeout>"
    When Insert data from "<jsonPath>" to "Telephone_telephoneNumber"
    When Insert data from "<jsonPath>" to "Telephone_teleType"
    And press on ENTER
    And type into "Telephone_IMEI" the text "IMEI"
    And type into "Telephone_description" the text "description"

                ########################Add Link##################################
    And press on button "Common_RelatedPeople"
    And Hold for "2" seconds
    And Insert "person" ID to "Common_LinkPerson"
    And press on ENTER
    And press on button "Common_LinkFirstResultSearch"
    And press on button "Common_LinkPersonAccept"
    And press on button "Common_LinkPersonOKButton"
    Then check if item is visible "common_LinkIcon" with timeout of "<timeout>"
                ########################Add Link##################################
                ########################Add TAG - START  #########################
    And press on button "common_addTag"
    And Hold for "1" seconds
    And Insert data from "<jsonPath>" to "common_tagName"
    And press on ENTER
    And wait for "common_saveTag" is displaying on screen with timeout of "<timeout>"
    And press on button "common_saveTag"
                 ######################## Add TAG - END ##################################
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And Hold for "5" seconds
    And press on button "common_saveButton"
    And Hold for "3" seconds
    Then get Element ID from "Phone_phoneId"
    # check display on screen
    Then The expected  "Telephone_numberView" is Equal to the actual "97250558877"
    Then The expected  "Telephone_typeView" is Equal to the actual "Casa"
    Then The expected  "Telephone_imeiView" is Equal to the actual "IMEI"
    Then The expected  "Telephone_descriptionView" is Equal to the actual "description"

    Examples:
      | timeout |  | jsonPath                               |
      | 10      |  | src\main\resources\personInfoData.json |
