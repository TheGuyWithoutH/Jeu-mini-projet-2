package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.userInterface.SuperPacmanAreaGUIEntity;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.Queue;

public abstract class SuperPacmanArea extends Area implements Logic {

    protected abstract void createArea();
    private SuperPacmanBehavior behavior;
    private ArrayList<Ghost> areaGhostActors;
    private AreaGraph areaGraph;
    public static float cameraScaleFactor = 15.f;

    private int diamondCount;

    private boolean pause;
    private Window window;

    private SuperPacmanAreaGUIEntity display;

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);
            behavior.registerActors(this);
            pause = false;
            this.window = window;

            createArea();
            display = new SuperPacmanAreaGUIEntity(this);
            registerActor(display);
            return true;
        }
        return false;
    }

    public boolean begin(Window window, FileSystem fileSystem, SuperPacmanAutoGeneratedMazeBehavior behavior) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            this.behavior = behavior;
            setBehavior(behavior);
            behavior.registerActors(this);
            pause = false;

            createArea();
            display = new SuperPacmanAreaGUIEntity(this);
            registerActor(display);
            return true;
        }
        return false;
    }

    public void deactivateNode(DiscreteCoordinates coordinates, Logic logic){
        behavior.deactivateNode(coordinates, logic);
    }

    public ArrayList<Ghost> getAreaGhostActors(){
        return areaGhostActors;
    }

    public Queue<Orientation> shortestPath(DiscreteCoordinates origine, DiscreteCoordinates arrivee){
        return(behavior.shortestPath(origine, arrivee));
    }

    public void scareCheck(SuperPacmanPlayer player){
        behavior.scareCheck(player);
    }

    public AreaGraph getAreaGraph(){return areaGraph;}

    public boolean isCellWalkable(DiscreteCoordinates coordinates, String SIDE){
        if(behavior.isCellWalkableBehavior(coordinates, SIDE)){
            return true;
        }
        else{
            return false;
        }
    }

    public void addDiamond(){ diamondCount++; }

    public void removeDiamond(){ diamondCount--; }

    public final float getCameraScaleFactor(){return cameraScaleFactor;}

    public abstract DiscreteCoordinates getSpawnPoint();

    @Override
    public boolean isOn() { return !isOff();}

    @Override
    public boolean isOff() { return diamondCount>0;}

    @Override
    public float getIntensity() {
        if(isOn()){
            return 1.f;
        } else {
            return 0.f;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard= this.getKeyboard();
        if(keyboard.get(Keyboard.P).isPressed()){
            suspend();
        }
    }

    @Override
    public void suspend(){
        super.suspend();
        pause = !pause;
        System.out.println("pause : "+pause);
    }

    public boolean isPaused(){ return pause; }

}
