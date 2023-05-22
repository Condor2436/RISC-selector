# RISC-selector
## 2023/05/17
---
### Create: 

- BasicChecker.java

- Behavior.java

- BehaviorList.java

- Territory.java

- Units.java

- BasicCheckerTest.java

- BehaviorTest.java

- BehaviorListTest.java

- TerritoryTest.java

- UnitsTest.java

- OriginChecker.java

- OriginCheckerTest.java

- DestinationChecker.java

- DestinationCheckerTest.java: the private method will never return null but it has to have a final return at the end, this makes the code coverage is not 100%, there is 1 line cannot be covered.

- Player.java(currently in commit and test, still need work on)

- GameInfo.java(currently in commit and test, still need work on)


- TransInfo.java(currently in commit and test, still need work on)

- TransInfoTest.java(currently in commit and test, still need work on)

---

### Modify:

- Units.java: add feature to compare two Units objects

- UnitsTest.java: add test for the added feature

- UnitsTest.java: add test for the added feature

---
## 2023/05/19
- create: 

Game.java

---
- modify:
 
 Player.java: remove unnecessary status that I used want to use it as condition of server's shut down
 
 TransInfo.java: Now it can deliver multiple information
 
 TransInfoTest.java: unit test updated as the modification of source code
 
 Units.java: add function to modify the inside int
 
 UnitsTest.java: unit test updated as the modification of source code
 
---

## 2023/05/22
- create: 

    Server.java

- modify:

    Game.java: finish game logic, wait for client

    Player.java: add helper methods to switch game and disconnect from game
    
### TODO:

- client process related files
---
