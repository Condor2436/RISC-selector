package dku.edu.zj82;

import java.util.ArrayList;

public abstract class BasicChecker {
    private final BasicChecker next;
    public BasicChecker(BasicChecker next){
        this.next = next;
    }
    public abstract String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap);
    public String processCheck(Behavior myBehavior, ArrayList<Territory> globalMap){
        String errMessage = checkMyRule(myBehavior,globalMap);
        if(errMessage!=null){
            return errMessage;
        }
        if(next!=null){
            return next.processCheck(myBehavior,globalMap);
        }
        return null;
    }
}
