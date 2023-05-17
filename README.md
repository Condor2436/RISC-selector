# RISC-selector
## 2023/05/17
- Create: 

BasicChecker.java

Behavior.java

BehaviorList.java

Territory.java

Units.java

BasicCheckerTest.java

BehaviorTest.java

BehaviorListTest.java

TerritoryTest.java

UnitsTest.java

OriginChecker.java

OriginCheckerTest.java

DestinationChecker.java

DestinationCheckerTest.java: the private method will never return null but it has to have a final return at the end, this makes the code coverage is not 100%, there is 1 line cannot be covered.

---

- Modify:

Units.java: add feature to compare two Units objects

UnitsTest.java: add test for the added feature
