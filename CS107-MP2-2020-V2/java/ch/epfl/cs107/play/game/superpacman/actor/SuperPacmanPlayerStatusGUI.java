package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;


class SuperPacmanPlayerStatusGUI implements Graphics {

    private final int MAX_HEALTH = 5;

    private final SuperPacmanPlayer player;
    private Vector coordinates = new Vector(1.f,13.2f);
    private String lifeDisplayString = "superpacman/lifeDisplay";
    private Color COLOR = Color.YELLOW;

    SuperPacmanPlayerStatusGUI(SuperPacmanPlayer player){
        this.player = player;
    }

    protected void adaptGUI(){
        lifeDisplayString = "superpacman/lifeDisplayRed";
        coordinates = new Vector(1.f, 12.2f);
        COLOR = Color.RED;
    }

   private void manageHealth(Canvas canvas){

        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2,height/2));

        for (int i = 0; i < MAX_HEALTH; i++) {
            ImageGraphics health;
            if(i<player.getHealth()){
                health = new ImageGraphics(ResourcePath.getSprite(lifeDisplayString), 1.f, 1.f, new RegionOfInterest(0, 0, 64, 64) , anchor.add(new Vector(coordinates.x+i, coordinates.y)), 1, 10);
            } else {
                health = new ImageGraphics(ResourcePath.getSprite(lifeDisplayString), 1.f, 1.f, new RegionOfInterest(64, 0, 64, 64) , anchor.add(new Vector(coordinates.x+i, coordinates.y)), 1, 10);
            }
            health.draw(canvas);
        }
    }

    private void manageScore(Canvas canvas){
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2,height/2));

        TextGraphics scoreText = new TextGraphics("Score : "+player.getScore(), 1.f, COLOR);
        scoreText.setOutlineColor(COLOR==Color.RED? Color.RED : Color.ORANGE);
        scoreText.setThickness(0.05f);
        scoreText.setBold(true);
        scoreText.setAnchor(anchor.add(new Vector(width/2, coordinates.y)));
        scoreText.draw(canvas);
    }


    @Override
    public void draw(Canvas canvas) {
        manageScore(canvas);
        manageHealth(canvas);
    }

}
