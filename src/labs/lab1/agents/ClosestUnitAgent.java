package src.labs.lab1.agents;


// SYSTEM IMPORTS
import edu.bu.labs.lab1.Coordinate;
import edu.bu.labs.lab1.Direction;
import edu.bu.labs.lab1.Tile;
import edu.bu.labs.lab1.Unit.UnitView;
import edu.bu.labs.lab1.State.StateView;
import edu.bu.labs.lab1.agents.Agent;
 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// JAVA PROJECT IMPORTS


public class ClosestUnitAgent
    extends Agent
{

    // put your fields here! You will probably want to remember the following information:
    //      - all friendly unit ids (there may be more than one!)
    //      - the location(s) of COIN(s) on the map
    private Set<Integer> myUnitIds;
    private Coordinate finishLocation;

    /**
     * The constructor for this type. Each agent has a unique ID that you will need to use to request info from the
     * state about units it controls, etc.
     */
	public ClosestUnitAgent(final int agentId)
	{
		super(agentId); // make sure to call parent type (Agent)'s constructor!

        // initialize your fields here!
        this.myUnitIds = new HashSet<>();
        this.finishLocation = null;

        // helpful printout just to help debug
		System.out.println("Constructed ClosestUnitAgent");
	}

    /////////////////////////////// GETTERS AND SETTERS (this is Java after all) ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final Set<Integer> getMyUnitIds(){
        return this.myUnitIds;
    }
    public final Coordinate getFinishLocation(){
        return this.finishLocation;
    }

    /**
     * This method is called by our game engine once: before any moves are made. You are provided with the state of
     * the game before any actions have been taken. This is in case you have some fields you need to set but are
     * unable to in the constructor of this class (like keeping track of units on the map, etc.).
     */
	@Override
	public void initializeFromState(final StateView stateView)
	{
        // TODO: identify units, set fields that couldn't be initialized in the constructor because
        // of a lack of game data in the constructor.
        for(Integer unitID : stateView.getUnitIds(this.getAgentId())){
            this.myUnitIds.add(unitID);
        }
 
        Coordinate finishLocation = null;

        for(int row = 0; row < stateView.getNumRows(); row++){
            for(int col = 0; col < stateView.getNumCols(); col++){
                Coordinate c = new Coordinate(row, col);
                if(stateView.getTileState(c) == Tile.State.FINISH){
                    finishLocation = c;
                }
            }
        }
 
        if(finishLocation == null){
            System.err.println("No finish tile");
            System.exit(-1);
        }
 
        this.finishLocation = finishLocation;
	}

    /**
     * This method is called every turn (or "frame") of the game. Your agent is responsible for assigning
     * actions to each of the unit(s) your agent controls. The return type of this method is a mapping
     * from unit ID (that your agent controls) to the Direction you want that unit to move in.
     *
     * If you are trying to collect COIN(s), you do so by walking into the same square as a COIN. Your agent
     * will pick it up automatically (and the COIN will dissapear from the map).
     */
	@Override
	public Map<Integer, Direction> assignActions(final StateView state)
    {
        Map<Integer, Direction> actions = new HashMap<>();

        // TODO: your code to give your unit(s) actions for this turn goes here!
        Integer closestUnitId = null;
        Coordinate closestPosition = null;
        double closestDistance = Double.POSITIVE_INFINITY;
 
        for(Integer unitId : this.getMyUnitIds()){
            UnitView unitView = state.getUnitView(this.getAgentId(), unitId);
            Coordinate position = unitView.currentPosition();
 
            double dx = position.col() - this.getFinishLocation().col();
            double dy = position.row() - this.getFinishLocation().row();
            double distance = Math.sqrt(dx * dx + dy * dy);
 
            if(distance < closestDistance){
                closestDistance = distance;
                closestUnitId = unitId;
                closestPosition = position;
            }
        }
 
        if(closestUnitId != null){
            Direction direction;
            if(closestPosition.col() < this.getFinishLocation().col()){
                direction = Direction.RIGHT;
            }
            else if(closestPosition.col() > this.getFinishLocation().col()){
                direction = Direction.LEFT;
            }
            else if(closestPosition.row() > this.getFinishLocation().row()){
                direction = Direction.UP;
            }
            else if(closestPosition.row() < this.getFinishLocation().row()){
                direction = Direction.DOWN;
            }
            else{
                direction = Direction.UP;
            }
 
            actions.put(closestUnitId, direction);
        } 
        return actions;
	}
}

