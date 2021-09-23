
## Agenda Meeting 06-03

---

Date:           06-03-2020\
Note taker:     Ethan Keller

# Opening
*Everybody is present.*

# Points of action

 - Change branch strategy
   - After careful consideration we saw that we have way too many branches and that it is not a clean environment to work in. We merged (during the meeting) every branch into develop. From now on when we want to create a new feature we create a new branch on top of develop. When its done we go to the issue we just fixed, create a merge request and merge that feature branch into develop. That way we have every finished feature in the develop branch and everybody can directly use it. (it's the branch strategy that we actually had to use). Also apparently branch names have to be in lower case letters (convention) Let's do that from now on as well!
 - Checkstyle
   - We should not forget that sometimes we have to run the gradle checkstyle file to see how we are doing with nice layout of our code.
     For now our checkstyle report is horrible so we will definitely have to arrange that.
 - Issue weights
   - When creating issues make sure we make it as 'full' as possible. So that means adding title, description, comments (if needed), labels, assignees (if possible), and weights! Weight 1 is least important and weight 10 is most important. That way the TA's can see how we work.
 - Merge request approvals
   - Lets have a rule that when someone creates a merge request, that person creates it from an issue (from the issue board) such that it is linked to that issue and the TA's can see why we are merging (based on what issue). Also when the GUI team creates a merge request, the back end team (at least 2 or 3 people) have to approve it and vice versa as well.
- Database normalization
   - Already done


# Closing
*Everybody starts working*
