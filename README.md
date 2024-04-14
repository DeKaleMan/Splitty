# OOPP Project team 31

## How to run the project
You can run the project using gradle see below to find the command for your setup:
1. Linux and Windows powershell: ./gradlew run (client) and ./gradlew bootRun (server)
2. Windows command line: ./gradlew.bat run (client) and ./gradlew.bat bootRun (server)
3. To build the project use ./gradlew build (Linux/powershell) and ./gradlew.bat build (windows command line)

## Important side notes
1. DO NOT delete the language files on the server side
2. We know that there is an enormous warning wall when entering some scenes, we have tried to fix it for 3 weeks but the only thing that we can conclude after research on the internet is that we are either stupid or that is it a JavaFX bug. It does not affect the way the app works so please ignore them.
3. If you want to run multiple clients in the same project, run one from your terminal using the gradlew command, and run one client from intellij. This way 2 config files will be created because intellij and the terminal use a different location to generate the clientData folder that we generate.

## **Ghost Participants**
We will briefly explain how the participant system works. When a user creates an event they are automatically the host. They can invite others through email and themselves with an invite code. Other users may join the event and can use all the functionality like creating expenses, settling debts and managing tags. There is ane thing only the host can do, which is creating/deleting/editing ghost participants. Ghost participants are participants created by the host, there are no users linked through the app to ghost participants. The idea is that if a user creates an event and wants to add a person without the app, they can simply add them as a ghost participant and fill in the details themselves. For every other functionality ghost participants work the same as other users. 

## **Long-polling/WebSockets**
Long-polling and WebSockets are implemented in overview (client/scenes/SplittyOverviewCtrl.java) for all updates of Participants and Expenses respectively.

## **The HCI features**
Upon selected or when hovering over a button or item in a list the item or button will change colour so the user knows what they have selected. 
Anything related to colour such as the red and green buttons, or the tags have appropriate text fill. When the background colour is darker the text is white, and the other way around. 
We added icons at various places so the user finds certain features faster such as settings, admin and statistics.
The entire app is filled with error messages which could pop up and inform the user what they are doing wrong, these are always red so the user knows its an error. The same goes for confirmation messages and these are always blue.
Regarding expenses, undoing changes is possible for as far as the user made changes during their visit to the event. 

Shortcuts
In general: ESC mean to go back or to cancel. In listviews you can press enter or dubble click to view or edit the selected item, this does not work for the expense tab however. Ctrl S is to save changes or add or create a participant/tag/event. Ctrl Shft N is creating a new tag/event/tag/participant in the approprate scenes. In listviews you can also delete tags/participants or events for the admin when pressing Ctrl delete.

