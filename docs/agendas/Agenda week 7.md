# Agenda week 7

---

~55 minutes

---
| Key | Value                |
| --- |----------------------|
| Date: | 16-03-2024           |
| Time: | 15:45                |
| Location: | Drebbelweg PC-hall 1 |
| Chair | Julian Overmars      |
| Minute Taker | Joris Timmermans     |
| Attendees: | Everyone             |

## Opening by chair (1 min)
- Is everyone present?
## Check-in (3 min)
- How is everyone doing?
- Are there any errors or problems someone came across?
## Announcements by the TA (3 min)
## Presentation of current app to TA (5 min)
- Show the features we implemented this week
  - Language
  - adding to the database
  - settling debt
  - websockets
  - config persistence
  - admin page
## Talk about the implementations we did this week and how they work (6 min)
- Everyone should say what they did and how to work with them e.g. websockets, participant saving etc...
## Talking about planning(8 min)
- Discuss when we are doing which requirements, how much time do we have?
- Take the exams into account
- Cleaning the code and front end, how long will this take?
- Has everyone seen the oral exam assignment details?
  - how are we making sure everyone understands it, when are we making the presentation?
## What to do for next week for everyone (20 min)
- Note the problems, front end? back end?
- Talk about the graded assignments 
  - Tasks and Planning, Technology 
  - did we improve? are we on track?
- Finish basic requirements, clean up already implemented ones
  - adding events and finishing the overview
  - invite codes, emails?
  - statistics
  - at least finish the extra's we have started working on!
- How are we dividing these tasks and in what groups? 
  - all together for fixing and cleaning code for maximum efficiency and no merge conflicts?
  - when are we meeting?
- Note error handling?

## Summary of meeting (4 min)
- Recap of key decisions and assigned tasks.
## Feedback for the meeting (2 min)
## Any questions? + Closure (3 min)

---

# Minute taker notes
## Announcements TA
- The presentation is friday 19th 13:55
- Thursday, March 28th:
  - deadline Draft product pitch (Formative)
  - deadline Implemented features (Formative)

### IMPORTANT MESSAGE:
ServerUtils has 2 constructors, don't use the one with the clientConfig parameter. 
See javadoc for more info

## Questions Duuk
1. The knockout criteria of week 9 is indeed on friday
2. The presentation is a product pitch, so we don't have to go technical. 
We can expect questions like: general process like what is spring, our design choices, the lecture stuff etc.
3. How literal is the backlog? "up to you reading comprehension" 
He did give us this:
   - edit participants = edit & remove
   - add: invite is fine just explain the choice in the presentation
   - delete: only host is fine, explain in presentation
#### EXPLAIN THOSE CHOICES
4. Issues, are the okay now?
   - Tasks/sub-issues are great
   - we should add weights to all issues
   like: easy 1, normal 3, big 5, bigger 7 (only prime numbers)
5. How low is the bar?
   - It is 5% of the grade and we need at least a 5.0 = 4/9 requirments
   - LOOKS ARENT GRADED, just color contrast etc
   - We need to add undo, MORE ICONS, and other forms of visual communication
6. JFX testing
  - use mockito and dependency injection
  - test whether methods are being called 
  - google for a helpful library

## Planning this week
  - focus on basic requirements
  - next week everything should be working
  - DISCORD CALL WEDNESDAY 11:30

## Tasks this week
- JORIS createEvent in CreateEventController
- JESSE File manager (to make sure that the errors that duuk had gets fixed)
- MACIEJ service class for grading
- STIJN Event new window (able to edit an Event title)
- YAVOR Join Event (invite code how are we going to do it???) -> hash name and append id at the end
- YAVOR you are only able to leave an event if you have balance 0 (everyone can edit everyone)
- MACIEJ add expenses to event
- JULIAN HCI requerementes and test coverage.
- STIJN test coverage in ServerUtils this is a lot so other people can do this too, but we need to make sure that we are writing same tests for the same file.
