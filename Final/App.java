package Final;

import Final.levels.LevelController;
import engine.Application;
import engine.Screen;
import engine.game.Resource;
import engine.game.Sound;
import engine.game.collision.AABShape;
import engine.game.collision.CircleShape;
import engine.game.collision.PolygonShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;
import engine.uikit.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;


/**
 * This is your Wiz top-level, App class.
 * This class will contain every other object in your game.
 */
public class App extends Application {
    public App(String title) {
        super(title);
    }

    public App(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
        super(title, windowSize, debugMode, fullscreen);
    }

    protected Game game = new Game();

    Vec2d worldSize = new Vec2d(960, 540);
    Vec2i mapGridNum = new Vec2i(32, 18);  // Total grids number

    int currentLevel = 0;
    int maxLevel = 1;

    // Ui elements in the game screen
    ViewPort viewPort;
    RectangleButton restartButton;
    Text restartText;
    Text resultText;
    Text pressRestartText;

    private void loadImage() {
        Resource.loadImage("Final/sprites/circle.png", "characterStand", new Vec2d(849, 892), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/circle.png", "characterRunX", new Vec2d(849, 892), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/circle.png", "characterRun-X", new Vec2d(849, 892), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/circle.png", "characterRunY", new Vec2d(849, 892), new Vec2i(1, 1));

        Resource.loadImage("Final/sprites/Tiles1.png", "tile1", new Vec2d(78, 70), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/Tiles2.png", "tile2", new Vec2d(64, 35), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/rocks.png", "rocks", new Vec2d(55, 160), new Vec2i(1, 2));
        Resource.loadImage("Final/sprites/Projectiles.png", "Projectiles", new Vec2d(64, 193), new Vec2i(4, 12));
        Resource.loadImage("Final/sprites/upwardSpike.png", "upwardSpike", new Vec2d(32, 32), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/downwardSpike.png", "downwardSpike", new Vec2d(32, 32), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall2.png", "wall2", new Vec2d(16, 16), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall1.png", "wall1", new Vec2d(16, 16), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall3.png", "wall3", new Vec2d(16, 16), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/flag.png", "flag", new Vec2d(181, 281), new Vec2i(1, 1));
    }

    /**
     * Load map for the XMLProcessor
     */
    private void loadMap() {
        XMLProcessor.tag2shape.put("AABShape", AABShape.class);
        XMLProcessor.tag2shape.put("CircleShape", CircleShape.class);
        XMLProcessor.tag2shape.put("PolygonShape", PolygonShape.class);
        XMLProcessor.tag2component.put("AnimationComponent", AnimationComponent.class);
        XMLProcessor.tag2component.put("AttackComponent", AttackComponent.class);
        XMLProcessor.tag2component.put("CollisionComponent", CollisionComponent.class);
        XMLProcessor.tag2component.put("FireComponent", FireComponent.class);
        XMLProcessor.tag2component.put("FireRayComponent", FireRayComponent.class);
        XMLProcessor.tag2component.put("GravityComponent", GravityComponent.class);
        XMLProcessor.tag2component.put("HealthComponent", HealthComponent.class);
        XMLProcessor.tag2component.put("JumpComponent", JumpComponent.class);
        XMLProcessor.tag2component.put("KeyEventsComponent", KeyEventsComponent.class);
        XMLProcessor.tag2component.put("MoveComponent", MoveComponent.class);
        XMLProcessor.tag2component.put("MovingComponent", MovingComponent.class);
        XMLProcessor.tag2component.put("PhysicsComponent", PhysicsComponent.class);
        XMLProcessor.tag2component.put("SpriteComponent", SpriteComponent.class);
        XMLProcessor.tag2component.put("TransformComponent", TransformComponent.class);
        XMLProcessor.tag2component.put("StandAnimationComponent", Character.StandAnimationComponent.class);
        XMLProcessor.tag2component.put("RunLeftAnimationComponent", Character.RunLeftAnimationComponent.class);
        XMLProcessor.tag2component.put("RunRightAnimationComponent", Character.RunRightAnimationComponent.class);
        XMLProcessor.tag2component.put("CharacterMoveComponent", Character.CharacterMoveComponent.class);
    }

    private void loadAudios() {
        Sound.loadAudio("Final/resources/BGM.wav", "BackGround");
        Sound.loadAudio("Final/resources/jump.wav", "Jump");
        Sound.loadAudio("Final/resources/projectile.wav", "projectile");
    }

    @Override
    protected void onStartup() {
        loadImage();
        loadMap();
        loadAudios();
        LevelController.load();
        addScreen(createTitleScreen());
        addScreen(createInstructionScreen());
        activateScreen("title");
        super.onStartup();
    }

    /**
     * Create a title screen with a title and an instruction button
     * @return return the title screen
     */
    protected Screen createTitleScreen() {
        Screen titleScreen = new Screen("title",
                DEFAULT_STAGE_SIZE,
                Color.rgb(45, 45, 50));

        RectangleButton startButton = new RectangleButton(new Vec2d(400, 300),
                new Vec2d(155, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            @Override
            public void onMouseClicked(MouseEvent e) {
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                currentLevel = 0;
                restartVideo("Final/resources/re.mp4");
                super.onMouseClicked(e);
            }
        };

        Text startText = new Text("Start",
                Font.font(28),
                new Vec2d(445, 335),
                Color.rgb(0, 0, 0));

        RectangleButton loadButton = new RectangleButton(new Vec2d(400, 370),
                new Vec2d(155, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            @Override
            public void onMouseClicked(MouseEvent e) {
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                game.load();
                load();
            }
        };

        Text loadText = new Text("Load",
                Font.font(28),
                new Vec2d(445, 405),
                Color.rgb(0, 0, 0));

        RectangleButton instructionButton = new RectangleButton(new Vec2d(400, 440),
                new Vec2d(155, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            @Override
            public void onMouseClicked(MouseEvent e) {
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                activateScreen("instruction");  // Jump to instruction screen
                super.onMouseClicked(e);
            }
        };

        Text instructionText = new Text("instruction",
                Font.font(28),
                new Vec2d(405, 475),
                Color.rgb(0, 0, 0));

        Text titleText = new Text("Final",
                Font.font(96),
                new Vec2d(365, 200),
                Color.rgb(255, 255, 255));

        startButton.addUIElement(startText);  // Add text to the button
        titleScreen.addUIElement(startButton);  // Add button to the screen
        loadButton.addUIElement(loadText);
        titleScreen.addUIElement(loadButton);
        instructionButton.addUIElement(instructionText);
        titleScreen.addUIElement(instructionButton);
        titleScreen.addUIElement(titleText);  // Add text to the screen

        titleScreen.onResize(currentStageSize);

        titleScreen.setActive(false);

        return titleScreen;
    }

    /**
     * Create an instruction screen. Player can choose 3 seeds in this screen.
     * @return return the instruction screen.
     */
    protected Screen createInstructionScreen() {
        Screen instructionScreen = new Screen("instruction",
                DEFAULT_STAGE_SIZE,
                Color.rgb(45, 45, 50));

        Text titleText = new Text("Instruction",
                new Font(56),
                new Vec2d(300, 100),
                Color.rgb(255, 255, 255));

        Text instructionText = new Text("Move: arrow keys\n" +
                "Jump: shift (the character can double jump)\n" +
                "Fire: z\n",
                new Font(24),
                new Vec2d(230, 150),
                Color.rgb(255, 255, 255));

        RectangleButton backButton = new RectangleButton(new Vec2d(400, 370),
                new Vec2d(155, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            public void onMouseClicked(MouseEvent e) {
                if(!active) return;
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                activateScreen("title");
            }
        };
        Text backText = new Text("Back",
                Font.font(28),
                new Vec2d(445, 405),
                Color.rgb(0, 0, 0));


        instructionScreen.addUIElement(titleText);
        instructionScreen.addUIElement(instructionText);
        backButton.addUIElement(backText);
        instructionScreen.addUIElement(backButton);

        instructionScreen.onResize(currentStageSize);

        instructionScreen.setActive(false);

        return instructionScreen;
    }

    protected Screen createVideoScreen(String videoPath) {

        class VideoScreen extends Screen {
            protected Video video;

            public void setVideo(Video video) {
                this.video = video;
            }

            public VideoScreen(String name, Vec2d size, Color color) {
                super(name, size, color);
            }

            @Override
            public void onKeyPressed(KeyEvent e) {
                super.onKeyPressed(e);
                restartGameScreen();
            }

            @Override
            public void onTick(long nanosSincePreviousTick) {
                super.onTick(nanosSincePreviousTick);
                if(video != null && video.isFinished()) {
                    video.dispose();
                    restartGameScreen();
                }
            }
        }
        VideoScreen videoScreen = new VideoScreen("video",
                DEFAULT_STAGE_SIZE,
                Color.rgb(0, 0, 0, 0));

        Text text = new Text("Press any key to continue...",
                Font.font(28),
                new Vec2d(10, 30),
                Color.rgb(128, 209, 200, 0.75));

        videoScreen.addUIElement(text);

        Video video = new Video(videoPath, new Vec2d(0, 0), DEFAULT_STAGE_SIZE);
        video.setApp(this);
        videoScreen.addUIElement(video);
        videoScreen.setVideo(video);

        return videoScreen;
    }

    /**
     * Create a game screen with a view port, multiple buttons and texts.
     * @return return the game screen.
     */
    protected Screen createGameScreen(boolean createNewWorld) {
        // Create all UIElement and game related objects
        Screen gameScreen = new Screen("game",
                DEFAULT_STAGE_SIZE,
                Color.rgb(0, 0, 0));
        viewPort = new ViewPort(new Vec2d(0, 0), DEFAULT_STAGE_SIZE, new Vec2d(0, 0), new Scale(1, 1)) {
            @Override
            public void onKeyPressed(KeyEvent e) {
                if(e.getCode() == KeyCode.R) restartGame();
                super.onKeyPressed(e);
            }
        };

        // Create a new game world
        if(createNewWorld) {
            game.createGameWorld(currentLevel, worldSize, mapGridNum);
        }

        viewPort.setGameWorld(game.getGameWorld());
        gameScreen.addUIElement(viewPort);

        RectangleButton backButton = new RectangleButton(new Vec2d(850, 20),
                new Vec2d(100, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            public void onMouseClicked(MouseEvent e) {
                if(!active) return;
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                activateScreen("title");
            }
        };
        Text backText = new Text("Back",
                Font.font(28),
                new Vec2d(868, 55),
                Color.rgb(0, 0, 0));

        RectangleButton saveButton = new RectangleButton(new Vec2d(850, 90),
                new Vec2d(100, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            public void onMouseClicked(MouseEvent e) {
                if(!active) return;
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                game.save();
            }
        };
        Text saveText = new Text("Save",
                Font.font(28),
                new Vec2d(868, 125),
                Color.rgb(0, 0, 0));

        restartButton = new RectangleButton(new Vec2d(400, 325),
                new Vec2d(150, 50),
                Color.rgb(196, 189, 145),
                Color.rgb(204, 208, 132))
        {
            public void onMouseClicked(MouseEvent e) {
                if(!active) return;
                if(!inBound(new Vec2d(e.getX(), e.getY()))) return;
                restartGame();  // If the button is clicked, restart.
                super.onMouseClicked(e);
            }

            @Override
            public void onTick(long nanosSincePreviousTick) {
                if(game.gameWorld == null) return;
                if(game.gameWorld.isHasResult()) {
                    if(!game.gameWorld.isWin()) restartShow("YOU DIED!");
                    else if(currentLevel >= maxLevel) restartShow("YOU WIN!");
                    else {
                        currentLevel++;
                        restartGame();
                    }
                }
            }
        };
        restartText = new Text("RESTART",
                Font.font(28),
                new Vec2d(415, 360),
                Color.rgb(0, 0, 0));
        pressRestartText = new Text("Press 'R' to restart",
                Font.font(20),
                new Vec2d(390, 400),
                Color.rgb(255, 255, 255));
        resultText = new Text("YOU WIN!",
                Font.font(96),
                new Vec2d(200, 200),
                Color.rgb(255, 255, 255));
        restartButton.setActive(false);
        restartText.setActive(false);
        pressRestartText.setActive(false);
        resultText.setActive(false);

        gameScreen.addUIElement(backButton);
        gameScreen.addUIElement(backText);
        gameScreen.addUIElement(saveButton);
        gameScreen.addUIElement(saveText);

        gameScreen.addUIElement(restartButton);
        gameScreen.addUIElement(restartText);
        gameScreen.addUIElement(pressRestartText);
        gameScreen.addUIElement(resultText);

        gameScreen.onResize(currentStageSize);

        gameScreen.setActive(false);

        return gameScreen;
    }

    protected void restartGame() {
        game.disposeCurrentGameWorld();

        game.createGameWorld(currentLevel, worldSize, mapGridNum);
        game.getGameWorld().setViewPort(viewPort);
        viewPort.setGameWorld(game.getGameWorld());

        restartButton.setActive(false);
        restartText.setActive(false);
        pressRestartText.setActive(false);
        resultText.setActive(false);
        viewPort.setActive(true);
    }

    /**
     * Restart the game.
     */
    protected void restartGameScreen() {
        if(screensName2Index.containsKey("game"))
            screens.set(screensName2Index.get("game"), createGameScreen(true));  // Create new game screen to reset everything
        else addScreen(createGameScreen(true));
        activateScreen("game");
    }

    protected void restartVideo(String videoPath) {
        if(screensName2Index.containsKey("video"))
            screens.set(screensName2Index.get("video"), createVideoScreen(videoPath));  // Create new game screen to reset everything
        else addScreen(createVideoScreen(videoPath));
        activateScreen("video");
    }

    /**
     * Load the previously saved game.
     */
    protected void load() {
        if(screensName2Index.containsKey("game"))
            screens.set(screensName2Index.get("game"), createGameScreen(false));  // Create new game screen to reset everything
        else addScreen(createGameScreen(false));
        activateScreen("game");
    }

    /**
     * Game over, show result.
     * @param result result of the game
     */
    protected void restartShow(String result) {
        restartButton.setActive(true);
        restartText.setActive(true);
        pressRestartText.setActive(true);
        resultText.setContent(result);
        resultText.setActive(true);
        viewPort.setActive(false);
    }
}
