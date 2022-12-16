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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


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
    int maxLevel = 4;

    // Ui elements in the game screen
    ViewPort viewPort;
    Picture restartPicture;
    Text resultText;

    private void loadImage() {
        Resource.loadImage("Final/sprites/circle.png", "circle", new Vec2d(1052, 1052), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/tiredCircle.png", "tiredCircle", new Vec2d(1052, 1052), new Vec2i(1, 1));

        Resource.loadImage("Final/sprites/wall2.png", "tile1", new Vec2d(1080, 1080), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/Tiles2.png", "tile2", new Vec2d(64, 35), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/rocks.png", "rocks", new Vec2d(55, 160), new Vec2i(1, 2));
        Resource.loadImage("Final/sprites/Projectiles.png", "Projectiles", new Vec2d(64, 193), new Vec2i(4, 12));
        Resource.loadImage("Final/sprites/upwardSpike.png", "upwardSpike", new Vec2d(915, 1075), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/downwardSpike.png", "downwardSpike", new Vec2d(915, 1075), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall2.png", "wall2", new Vec2d(1080, 1080), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall1.png", "wall1", new Vec2d(1080, 1080), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/wall3.png", "wall3", new Vec2d(1080, 1080), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/flag.png", "flag", new Vec2d(1080, 1080), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/save.png", "save", new Vec2d(541, 548), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/saved.png", "saved", new Vec2d(541, 548), new Vec2i(1, 1));
        Resource.loadImage("Final/sprites/guide.png", "guide", new Vec2d(1080, 1080), new Vec2i(1, 1));

        Resource.loadImage("Final/sprites/refresh.png", "refresh", new Vec2d(1080, 1080), new Vec2i(1, 1));
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
        XMLProcessor.tag2component.put("CharacterMoveComponent", Character.CharacterMoveComponent.class);
        XMLProcessor.tag2component.put("CharacterDashComponent", Character.CharacterDashComponent.class);
    }

    private void loadAudios() {
        Sound.loadAudio("Final/resources/BGM2.wav", "BackGround");
        Sound.loadAudio("Final/resources/jump3.wav", "Jump");
        Sound.loadAudio("Final/resources/projectile.wav", "projectile");
    }

    @Override
    protected void onStartup() {
        loadImage();
        loadMap();
        loadAudios();
        LevelController.load();
        addScreen(createTitleScreen());
        activateScreen("title");
        super.onStartup();
    }

    /**
     * Create a title screen with a title and an instruction button
     * @return return the title screen
     */
    protected Screen createTitleScreen() {
        class VideoScreen extends Screen {
            protected Video video;

            public void setVideo(Video video) {
                this.video = video;
            }

            public VideoScreen(String name, Vec2d size, Color color) {
                super(name, size, color);
            }
        }

        VideoScreen titleScreen = new VideoScreen("title",
                DEFAULT_STAGE_SIZE,
                Color.rgb(45, 45, 50, 0));

        Video video = new Video("Final/resources/start.mp4", new Vec2d(0, 0), DEFAULT_STAGE_SIZE);
        video.setApp(this);
        video.setAutoPlay(true);
        titleScreen.addUIElement(video);
        titleScreen.setVideo(video);

        MenuList menuList = new MenuList(new Vec2d(400, 300),
                                        new Vec2d(155, 200));
        RectangleKeyButton startButton = new RectangleKeyButton(new Vec2d(375, 325),
                                                                new Vec2d(155, 50),
                                                                Color.rgb(0, 0, 0),
                                                                Color.rgb(0, 0, 0, 0.75))
        {
            @Override
            protected void action() {
                currentLevel = 0;
                restartVideo("Final/resources/instruction.mp4");
            }
        };

        Text startText = new Text("Start",
                Font.font(28),
                new Vec2d(420, 360),
                Color.rgb(255, 255, 255));

        RectangleKeyButton loadButton = new RectangleKeyButton(new Vec2d(375, 395),
                new Vec2d(155, 50),
                Color.rgb(0, 0, 0),
                Color.rgb(0, 0, 0, 0.75))
        {
            @Override
            public void action() {
                load();
            }
        };

        Text loadText = new Text("Load",
                Font.font(28),
                new Vec2d(420, 430),
                Color.rgb(255, 255, 255));

        Text titleText = new Text("Circle World",
                Font.font(96),
                new Vec2d(200, 150),
                Color.rgb(0, 0, 0));

        startButton.addUIElement(startText);  // Add text to the button
        menuList.addButton(startButton);
        loadButton.addUIElement(loadText);
        menuList.addButton(loadButton);
        titleScreen.addUIElement(menuList);
        titleScreen.addUIElement(titleText);  // Add text to the screen

        titleScreen.onResize(currentStageSize);

        titleScreen.setActive(false);

        return titleScreen;
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
        }
        VideoScreen videoScreen = new VideoScreen("video",
                DEFAULT_STAGE_SIZE,
                Color.rgb(0, 0, 0, 0));

        Text text = new Text("Press any key to continue...",
                Font.font(28),
                new Vec2d(10, 500),
                Color.rgb(0, 0, 0));

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
    protected Screen createGameScreen(int level) {
        // Create all UIElement and game related objects
        Screen gameScreen = new Screen("game",
                DEFAULT_STAGE_SIZE,
                Color.rgb(0, 0, 0)) {
            @Override
            public void onKeyPressed(KeyEvent e) {
                if(e.getCode() == KeyCode.B) activateScreen("title");
                super.onKeyPressed(e);
            }
        };
        viewPort = new ViewPort(new Vec2d(0, 0), DEFAULT_STAGE_SIZE, new Vec2d(0, 0), new Scale(1, 1)) {
            @Override
            public void onKeyPressed(KeyEvent e) {
                if(e.getCode() == KeyCode.R) restartGame();
                super.onKeyPressed(e);
            }

            @Override
            public void onStartup() {
                super.onStartup();
                game.onStartup();
            }

            @Override
            public void onShutdown() {
                super.onShutdown();
                game.onShutdown();
            }
        };

        // Create a new game world
        currentLevel = level;
        game.createGameWorld(level, worldSize, mapGridNum);

        viewPort.setGameWorld(game.getGameWorld());
        gameScreen.addUIElement(viewPort);

        Image image = new Image("Final/sprites/gameOver.png", 765, 170, true, true);
        restartPicture = new Picture(new Vec2d(100, 150), new Vec2d(765, 170),image)
        {
            @Override
            public void onTick(long nanosSincePreviousTick) {
                if(game.gameWorld == null) return;
                if(game.gameWorld.isHasResult()) {
                    if(!game.gameWorld.isWin()) restartShow();
                    else if(currentLevel >= maxLevel) {
                        restartCurtainCall("Final/resources/curtainCall.mp4");
                    }
                    else {
                        currentLevel++;
                        restartGame();
                    }
                }
            }
        };

        resultText = new Text("YOU WIN!",
                Font.font(96),
                new Vec2d(200, 200),
                Color.rgb(255, 255, 255));
        restartPicture.setActive(false);
        resultText.setActive(false);

        gameScreen.addUIElement(restartPicture);
        gameScreen.addUIElement(resultText);

        gameScreen.onResize(currentStageSize);

        gameScreen.setActive(false);

        return gameScreen;
    }

    protected Screen createCurtainCallScreen(String videoPath) {
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
                if(e.getCode() == KeyCode.B) activateScreen("title");
            }
        }
        VideoScreen videoScreen = new VideoScreen("curtainCall",
                DEFAULT_STAGE_SIZE,
                Color.rgb(0, 0, 0, 0));


        Video video = new Video(videoPath, new Vec2d(0, 0), DEFAULT_STAGE_SIZE);
        video.setApp(this);
        videoScreen.addUIElement(video);
        videoScreen.setVideo(video);

        return videoScreen;
    }

    protected void restartGame() {
        game.disposeCurrentGameWorld();

        game.createGameWorld(currentLevel, worldSize, mapGridNum);
        game.getGameWorld().setViewPort(viewPort);
        viewPort.setGameWorld(game.getGameWorld());

        restartPicture.setActive(false);
        resultText.setActive(false);
        viewPort.setActive(true);
    }

    /**
     * Restart the game.
     */
    protected void restartGameScreen() {
        if(screensName2Index.containsKey("game"))
            screens.set(screensName2Index.get("game"), createGameScreen(currentLevel));  // Create new game screen to reset everything
        else addScreen(createGameScreen(currentLevel));
        activateScreen("game");
    }

    protected void restartVideo(String videoPath) {
        if(screensName2Index.containsKey("video"))
            screens.set(screensName2Index.get("video"), createVideoScreen(videoPath));  // Create new game screen to reset everything
        else addScreen(createVideoScreen(videoPath));
        activateScreen("video");
    }

    protected void restartCurtainCall(String videoPath) {
        if(screensName2Index.containsKey("curtainCall"))
            screens.set(screensName2Index.get("curtainCall"), createCurtainCallScreen(videoPath));  // Create new game screen to reset everything
        else addScreen(createCurtainCallScreen(videoPath));
        activateScreen("curtainCall");
    }

    /**
     * Load the previously saved game.
     */
    protected void load() {
        int level = 0;

        try {
            BufferedReader in = null;
            in = new BufferedReader(new FileReader("Final/savings/saving.txt"));
            StringBuffer sb;
            if (in.ready()) {
                sb = (new StringBuffer(in.readLine()));
                level = Integer.parseInt(String.valueOf(sb));
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(screensName2Index.containsKey("game"))
            screens.set(screensName2Index.get("game"), createGameScreen(level));  // Create new game screen to reset everything
        else addScreen(createGameScreen(level));
        activateScreen("game");
    }

    /**
     * Game over, show result.
     */
    protected void restartShow() {
        restartPicture.setActive(true);
        viewPort.setActive(false);
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        super.onKeyPressed(e);
        if(e.getCode() == KeyCode.ESCAPE) super.shutdown();
    }
}
