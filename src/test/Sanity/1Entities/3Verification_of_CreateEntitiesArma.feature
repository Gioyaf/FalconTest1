Feature: 3Verification_of_CreateEntitiesArma sanity scenario for entities feature Weapon


#####################   Test Weapon entity ####################################
  @sanity @weapon
  Scenario Outline: Add Weapon entity with just mandatory fields and save it
          ############# LOG - IN start #################
    Given navigate to "http://10.32.5.73/ui/" on browser
    When type into "login_userNameField" the text "wstu5"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
         ############# LOG - IN end #################
    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityWeaponButton"
    And wait for "Weapon_armaScreen" is displaying on screen with timeout of "<timeout>"
    And Hold for "2" seconds
    When Insert data from "<jsonPath>" to "Weapon_weaponSeries"
    When Insert data from "<jsonPath>" to "Weapon_weaponType"
    And press on ENTER
    When Insert data from "<jsonPath>" to "Weapon_weaponMark"
    And press on ENTER
    And press on button "Weapon_model"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And press on button "Weapon_caliber"
    And Hold for "1" seconds
    And press on ENTER
    And Hold for "1" seconds
    And type into "Weapon_description" the text "description"

                ########################Add Link - START ##################################
    And press on button "Common_RelatedPeople"
    And Hold for "2" seconds
   # And Insert "person" ID to "Common_LinkPerson"
    And Insert "person" ID to "Common_LinkPerson"
    And press on ENTER
    And press on button "Common_LinkFirstResultSearch"
    And press on button "Common_LinkPersonAccept"
    And press on button "Common_LinkPersonOKButton"
    Then check if item is visible "common_LinkIcon" with timeout of "<timeout>"
                ########################Add Link - END ##################################
                ########################Add TAG - START  ########################
    And press on button "common_addTag"
    And Hold for "1" seconds
    And Insert data from "<jsonPath>" to "common_tagName"
    And press on ENTER
    And wait for "common_saveTag" is displaying on screen with timeout of "<timeout>"
    And press on button "common_saveTag"
                 ######################## Add TAG - END ########################
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And Hold for "3" seconds
    And press on button "common_saveButton"
    And Hold for "5" seconds
    Then get Element ID from "Weapon_weaponId"
    # check display on screen
    Then The expected  "Weapon_seriesView" is Equal to the actual "76541223"
    Then The expected  "Weapon_typeView" is Equal to the actual "Ametralladora"
    Then The expected  "Weapon_markView" is Equal to the actual "Adi"
    Then The expected  "Weapon_modelView" is Equal to the actual "(1315) gas gun"
    Then The expected  "Weapon_caliberView" is Equal to the actual "22###222 o 223 = 5.56 mm"
    Then The expected  "Weapon_descriptionView" is Equal to the actual "description"

    Examples:
      | timeout |  | jsonPath                               |
      | 10      |  | src\main\resources\personInfoData.json |
