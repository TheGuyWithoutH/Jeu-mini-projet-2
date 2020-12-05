package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;


public class SuperPacmanBehavior extends AreaBehavior {

    public enum SuperPacmanCellType {

        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NONE(0), // never used as real content
        WALL ( -16777216),       //black
        FREE_WITH_DIAMOND(-1),         //white
        FREE_WITH_BLINKY (-65536),     //red
        FREE_WITH_PINKY ( -157237),    //pink
        FREE_WITH_INKY ( -16724737),   //cyan
        FREE_WITH_CHERRY (-36752),      //light red
        FREE_WITH_BONUS ( -16478723), //light blue
        FREE_EMPTY ( -6118750);         // sort of gray
        ;

        final int type;



        SuperPacmanCellType(int type) {
            this.type = type;
        }

        public static SuperPacmanCellType toType(int type) {
            for (SuperPacmanCellType ict : SuperPacmanCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;

        }
    }

    ArrayList<Ghost> ghostActors = new ArrayList<Ghost>();

    protected void registerActors(SuperPacmanArea area){
        DiscreteCoordinates coordinates;
        int height = getHeight();
        int width = getWidth();
        for(int x = 0; x < width; ++x ) {
            for(int y = 0; y < height; ++y){
                SuperPacmanCellType color = SuperPacmanCellType.toType(getRGB(height-1-y, x));
                SuperPacmanCell cell = (SuperPacmanCell)getCell(x,y);
                coordinates = new DiscreteCoordinates(x,y);
                switch(color){
                    case WALL:
                        Wall wallActor = new Wall(area,coordinates,cell.getWallNeighborhood(x,y));
                        area.registerActor(wallActor);
                        break;
                    case FREE_WITH_BONUS:
                        Bonus bonus = new Bonus(area, coordinates);
                        area.registerActor(bonus);
                        break;
                    case FREE_WITH_CHERRY:
                        Cherry cherry = new Cherry(area, coordinates);
                        area.registerActor(cherry);
                        break;
                    case FREE_WITH_DIAMOND:
                        Diamond diamond = new Diamond(area, coordinates);
                        area.registerActor(diamond);
                        break;
                    case FREE_WITH_BLINKY:
                        Vector positionRefuge = new Vector((float)(x),(float)(y));
                        Blinky blinky = new Blinky(area, Orientation.UP, coordinates, positionRefuge);
                        area.registerActor(blinky);
                        ghostActors.add(blinky);
                    case FREE_WITH_INKY:
                        Vector positionRefuge2 = new Vector((float)(x),(float)(y));
                        Inky inky = new Inky(area, Orientation.UP, coordinates, positionRefuge2);
                        area.registerActor(inky);
                        ghostActors.add(inky);
                    case FREE_WITH_PINKY:
                        Vector positionRefuge3 = new Vector((float)(x),(float)(y));
                        Pinky pinky = new Pinky(area, Orientation.UP, coordinates, positionRefuge3);
                        area.registerActor(pinky);
                        ghostActors.add(pinky);

                }
            }
        }
    }
    AreaGraph areaGraph;
    public ArrayList<Ghost> getGhostActors(){
        return ghostActors;
    }

    public static boolean ghostActorsExist(ArrayList<Ghost> arraylist){
        if(arraylist.size() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Default AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public SuperPacmanBehavior(Window window, String name) {
        super(window, name);
        areaGraph = new AreaGraph();

        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                SuperPacmanCellType color = SuperPacmanCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new SuperPacmanCell(x,y,color));
                
                SuperPacmanCell cell = (SuperPacmanCell)getCell(x,y);
                DiscreteCoordinates coordinates = new DiscreteCoordinates(x,y);
                if(color != SuperPacmanCellType.WALL){
                    areaGraph.addNode(coordinates , cell.hasSideEdge("LEFT",coordinates,height), cell.hasSideEdge("UP",coordinates,height), cell.hasSideEdge("RIGHT", coordinates,height),cell.hasSideEdge("DOWN", coordinates,height));
                }
            }

        }
    }
    public AreaGraph getAreaGraph(){return areaGraph;}

    public class SuperPacmanCell extends AreaBehavior.Cell{

        private final SuperPacmanCellType type;

        /**
         * Default Cell constructor
         *  @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         * @param type
         */
        protected SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {   //If cell already has an entity occupying all the cell space, it's not traversable
            if(this.hasNonTraversableContent()){
                return false;
            }
            else{
                return true;
            }
        }

        @Override
        public boolean isCellInteractable() {
            return false;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
        }

        private boolean cellExists(int x, int y){
            try {
                getCell(x, y);
            } catch (ArrayIndexOutOfBoundsException e){
                return false;
            }
            return true;
        }

        private boolean[][] getWallNeighborhood(int x, int y){
            boolean[][] neigborhood = new boolean[3][3];
            for(int xcord = x-1, i = 0; xcord <= x+1; ++xcord , ++i){
                for(int ycord = y+1, j = 0; ycord >= y-1; --ycord, ++j){
                    if(cellExists(xcord, ycord) &&  SuperPacmanCellType.toType(getRGB(getHeight()-1-ycord, xcord)) == SuperPacmanCellType.WALL){
                        neigborhood[i][j] = true;
                    }
                    else{
                        neigborhood[i][j] = false;
                    }
                }
            }
            return neigborhood;
        }
        
        protected boolean hasSideEdge(String Side, DiscreteCoordinates coordinates, int height){
           
            SuperPacmanCellType color = null;
            if(Side == "LEFT") {
                int x = coordinates.x - 1;
                int y = coordinates.y;
                if(cellExists(x,y)){
                    color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                }
            }
            else if(Side == "RIGHT") {
                int x = coordinates.x + 1;
                int y = coordinates.y;
                if(cellExists(x,y)){
                    color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                }

            }
            else if(Side == "DOWN") {
                int x = coordinates.x;
                int y = coordinates.y - 1;
                if(cellExists(x,y)){
                    color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                }
            }
            else if(Side == "UP") {
                int x = coordinates.x;
                int y = coordinates.y + 1;
                if(cellExists(x,y)){
                    color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                }

            }
            
            if (color == SuperPacmanCellType.WALL) {
                return true;
            } else {
                return false; 
            }
        }
    }


}
