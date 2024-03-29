package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level1 extends SuperPacmanArea {

    private final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);


    @Override
    protected void createArea() {

        Door door = new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(14,0), new DiscreteCoordinates(15,0));
        registerActor(door);

        Gate[] gates = new Gate[2];
        gates[0] = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14,3), this);
        gates[1] = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15,3), this);
        for (Gate gate : gates) {
            registerActor(gate);
        }
    }

    @Override
    public DiscreteCoordinates getSpawnPoint() {
        return PLAYER_SPAWN_POSITION;
    }

    @Override
    public String getTitle() {
        return "superpacman/Level1";
    }

}
