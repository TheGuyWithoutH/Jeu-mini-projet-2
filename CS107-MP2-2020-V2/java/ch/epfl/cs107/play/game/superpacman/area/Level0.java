package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level0 extends SuperPacmanArea {

    private final static DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);

    @Override
    protected void createArea() {

        Door door = new Door("superpacman/Level1", new DiscreteCoordinates(15, 6), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(5,9), new DiscreteCoordinates(6,9));
        registerActor(door);

        Key key = new Key(this, new DiscreteCoordinates(3,4));
        registerActor(key);

        Gate[] gates = new Gate[2];
        gates[0] = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5,8), key);
        gates[1] = new Gate(this, Orientation.LEFT, new DiscreteCoordinates(6,8), key);
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
        return "superpacman/Level0";
    }

}
