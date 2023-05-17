package dku.edu.zj82;

import java.util.ArrayList;

public class OriginChecker extends BasicChecker{
    public OriginChecker(BasicChecker next){
        super(next);
    }
    @Override
    public String checkMyRule(Behavior myBehavior, ArrayList<Territory> globalMap){
        int behaviorID = myBehavior.getOwnID();
        int originID = myBehavior.getOrigin().getOwnID();
        Units useUnits = myBehavior.getUnits();
        Units hasUnits = myBehavior.getOrigin().getUnits();
        if(behaviorID!=originID&&(myBehavior.getType().equalsIgnoreCase("move")||myBehavior.getType().equalsIgnoreCase("attack"))){
            return "The behavior requires playerID equal to the ID of place of departure, playerID is "+behaviorID+", territory ID is "+originID;
        }
        if(!hasUnits.checkEnoughUnit(useUnits)){
            return "No enough units, it has "+ hasUnits +" but use "+ useUnits;
        }
        return null;
    }
}
