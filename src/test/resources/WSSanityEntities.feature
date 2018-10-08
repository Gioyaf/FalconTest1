#Feature: sanity scenario for entities feature
#
######################   Define for all scenarios what is the object repository JSON file #######################
#  Background: Definition of object repository for all scenarios
#    Given Object repository for all GUI items is "WsCnsObjectRepository.json"
#
######################   Login to the system ####################################
#@gil
#  Scenario: Log in to WS system
#    Given navigate to "http://10.32.5.23/ui/" on browser
#    When type into "login_userNameField" the text "User10"
#    And type into "login_passwordField" the text "Pass123"
#    And press on button "login_startSessionButton"
#    Then wait for "topbar_addButton" is displaying on screen with timeout of "10"
#
######################   Test Person entitie ####################################
#  @gil
#  Scenario Outline: Add person entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And press on button "topbar_addEntityPersonButton"
#    And type into "person_FirstNameField" the text "<Name>"
#    And type into "person_LastNameField" the text "Ron"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common.conformationNOButton"
#    And wait for "person_FirstNameField" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Avi"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "Avi"
#
#    Examples:
#     |timeout | Name |
#     | 10     | Avi  |
#
#
#
#  Scenario Outline: Add "person" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addentityDropdown_addEntityPersonButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addentityDropdown_addEntityPersonButton"
#    And wait for "person_FirstNameField" is displaying on screen with timeout of "<timeout>"
#    And type into "person_FirstNameField" the text "Avi"
#    And type into "person_LastNameField" the text "Ron"
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Avi"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "Avi"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  Scenario Outline: Edit "person" entity, fill all possible fields with data and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on tab number "2" of "topbar_tabsOnMainToolbar"
#    And press on button "common_EditButton"
#    And wait for "addentityDropdown_addEntityPersonButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addentityDropdown_addEntityPersonButton"
#    And wait for "person_FirstNameField" is displaying on screen with timeout of "<timeout>"
#    And type into "person_FirstNameField" the text "Avi"
#    And type into "person_LastNameField" the text "Ron"
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Avi"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "Avi"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#
######################   Test weapon entity ####################################
#  Scenario Outline: Add "Weapon" entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityWeaponButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityWeaponButton"
#    And wait for "weapon_weaponType" is displaying on screen with timeout of "<timeout>"
#    And type into "weapon_weaponType" the text "Carabina"
#    And press on ENTER
#    And type into "weaponMark" the text "Adi"
#    And press on ENTER
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationNOButton"
#    And wait for "weapon_weaponType" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Carabina Adi"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "Carabina Adi"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  @sanity
#  Scenario Outline: Add "Weapon" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityWeaponButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityWeaponButton"
#    And wait for "weapon_weaponType" is displaying on screen with timeout of "<timeout>"
#    And type into "weapon_weaponType" the text "Carabina"
#    And press on ENTER
#    And type into "weaponMark" the text "Adi"
#    And press on ENTER
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Carabina Adi"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "Carabina Adi"
#
#    Examples:
#      |timeout |
#      | 10     |
#
######################   Test Active entity ####################################
#  @sanity
#  Scenario Outline: Add "Active" entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityActiveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityActiveButton"
#    And wait for "activeType" is displaying on screen with timeout of "<timeout>"
#    And type into "activeType" the text "Edificio comercial"
#    And press on ENTER
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationNOButton"
#    And wait for "activeType" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Edificio"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "Edificio"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  @sanity
#  Scenario Outline: Add "Active" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityActiveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityActiveButton"
#    And wait for "activeType" is displaying on screen with timeout of "<timeout>"
#    And type into "activeType" the text "Edificio comercial"
#    And press on ENTER
#    And type into "activeArea" the text "123"
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Edificio"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "Edificio"
#
#    Examples:
#      |timeout |
#      | 10     |
#
######################   Test Telephone entity ####################################
#  @sanity
#  Scenario Outline: Add "Telephone" entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addentityDropdown_addEntityTelephoneButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityTelephoneButton"
#    And wait for "telephoneNumber" is displaying on screen with timeout of "<timeout>"
#    And type into "telephoneNumber" the text "0501234567"
#    And press on ENTER
#    And Hold for "1" seconds
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationNOButton"
#    And wait for "telephoneNumber" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "0501234567"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "0501234567"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  @sanity
#  Scenario Outline: Add "Telephone" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addentityDropdown_addEntityTelephoneButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addentityDropdown_addEntityTelephoneButton"
#    And wait for "telephoneNumber" is displaying on screen with timeout of "<timeout>"
#    And type into "telephoneNumber" the text "0501234567"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "0501234567"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "0501234567"
#
#    Examples:
#      |timeout |
#      | 10     |
######################   Test Vehicle entity ####################################
#  @sanity
#  Scenario Outline: Add "Vehicle" entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityVehicleButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityVehicleButton"
#    And wait for "vehicleRegistration" is displaying on screen with timeout of "<timeout>"
#    And type into "vehicleRegistration" the text "AA123BB456"
#    And type into "vehicleType" the text "Anfibio"
#    And press on ENTER
#    And Hold for "1" seconds
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationNOButton"
#    And wait for "vehicleRegistration" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "AA123BB456"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "AA123BB456"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  @sanity
#  Scenario Outline: Add "Vehicle" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityVehicleButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityVehicleButton"
#    And wait for "vehicleRegistration" is displaying on screen with timeout of "<timeout>"
#    And type into "vehicleRegistration" the text "AA123BB456"
#    And type into "vehicleType" the text "Anfibio"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "AA123BB456"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "AA123BB456"
#
#    Examples:
#      |timeout |
#      | 10     |
#
######################   Test Organization entity ####################################
#  @sanity
#  Scenario Outline: Add "Organization" entity with just mandatory fields and cancel it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addEntityOrgButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addEntityOrgButton"
#    And wait for "orgName" is displaying on screen with timeout of "<timeout>"
#    And type into "orgName" the text "Evil corp"
#    And type into "orgType" the text "Cartel"
#    And press on ENTER
#    And Hold for "1" seconds
#    And press on button "common_cancelButton"
#    And wait for "common_conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "conformationNOButton"
#    And wait for "orgName" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_cancelButton"
#    And wait for "conformationNOButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_conformationYESButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Evil corp"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" not containing the item "Evil corp"
#
#    Examples:
#      |timeout |
#      | 10     |
#
#  @sanity
#  Scenario Outline: Add "Organization" entity with just mandatory fields and save it
#    Given wait for "topbar_addButton" is displaying on screen with timeout of "<timeout>"
#    When press on button "topbar_addButton"
#    And select "topbar_addEntityEntidadMenu" item from "topbar_addEntityDropdown" dropdown menu
#    And wait for "addentityDropdown_addEntityOrgButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "addentityDropdown_addEntityOrgButton"
#    And wait for "orgName" is displaying on screen with timeout of "<timeout>"
#    And type into "orgName" the text "Evil corp"
#    And type into "orgType" the text "Cartel"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_saveButton" is displaying on screen with timeout of "<timeout>"
#    And press on button "common_saveButton"
#    And Hold for "2" seconds
#    And type into "topbar_mainSearchField" the text "Evil corp"
#    And press on ENTER
#    And Hold for "1" seconds
#    And wait for "common_loadingImage" is not displaying on screen with timeout of "<timeout>"
#    Then list of results "topbar_mainSearchList" containing the item "Evil corp"
#    And Close all open tabs on main toolbar
#
#    Examples:
#      |timeout |
#      | 10     |