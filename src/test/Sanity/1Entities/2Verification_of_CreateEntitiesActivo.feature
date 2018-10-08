Feature: 2Verification_of_CreateEntitiesActivo sanity scenario for entities feature Activo

#####################   Test Activo entity ####################################
  @sanity @activo

  Scenario Outline: Add Activo entity with just mandatory fields and save it
          ############# LOG - IN start #################
    Given navigate to "http://10.32.5.73/ui/" on browser
    When type into "login_userNameField" the text "wstu5"
    And type into "login_passwordField" the text "Pass123"
    And press on button "login_startSessionButton"
    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
#         ############# LOG - IN end #################
    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
    When press on button "topbar_addButton"
    And Hover on Element "topbar_addEntityEntidadMenu" and click on "addentityDropdown_addEntityActiveButton"
    And wait for "Activo_ActivoTitle" is displaying on screen with timeout of "<timeout>"
    And Hold for "5" seconds
    When Insert data from "<jsonPath>" to "Activo_Nombre"
    When Insert data from "<jsonPath>" to "Activo_Tipo"
    And press on ENTER
    And type into "Activo_area" the text "10"
    And type into "Activo_description" the text "description"
                ########################Add Link##################################
    And press on button "Common_RelatedPeople"
    And Hold for "2" seconds
  #  And Insert "person" ID to "Common_LinkPerson"
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
                 ######################## Add TAG - END ##########################

        ########################## Test MAP Output - START ###########################################
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
    And press on button "Common_saveButtonOK"

    ####### SAVE MAP FORM - End
    ####### SAVE FORM - Start
    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
    And Hold for "3" seconds
    And press on button "common_saveButton"
    And Hold for "5" seconds
    And get Element ID from "Activo_ActivoId"
    ####### SAVE FORM - End
    # check display on screen
    Then The expected  "Activo_nameView" is Equal to the actual "Alejandro"
    Then The expected  "Activo_typeView" is Equal to the actual "Casa"
    Then The expected  "Activo_descriptionView" is Equal to the actual "description"

    Examples:
      | timeout | jsonPath                               | country | State  |
      | 5       | src\main\resources\personInfoData.json | Mexico  | México |
