package bricker.main;

import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.gameobjects.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManger extends GameManager {
    public static final int WALL_WIDTH = 10;

    private static final String WINDOW_TITLE = "bricker";
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int TARGET_FRAMERATE = 80;
    private static final float WINDOW_CENTER_FACTOR = 0.5f;
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_TURBO_MODE_IMAGE_PATH = "assets/redball.png";
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String BALL_COLLISION_SOUND_PATH = "assets/blop.wav";
    private static final int BALL_RADIUS = 25;
    private static final float BALL_VELOCITY = 250;
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final int PADDLE_WIDTH = 200;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_INITIAL_DISTANCE_FROM_CENTER = 30;
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final int BRICK_HEIGHT = 15;
    private static final String LOSE_MESSAGE = "You Lose!";
    private static final String PLAY_AGAIN_MESSAGE = " Play again";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final int MAX_LOSE = 3;
    private static final int HEART_WIDTH = 30;
    private static final int HEART_HEIGHT = 30;
    private static final int DISTANCE_BETWEEN_HEARTS = 3;
    private static final int DEFAULT_BRICKS_ROWS = 7;
    private static final int DEFAULT_BRICKS_COLS = 8;
    private static final int BRICKS_COLS_INDEX = 0;
    private static final int BRICKS_ROWS_INDEX = 1;
    private static final int DISTANCE_BETWEEN_BRICKS = 1;
    private static final String WIN_MESSAGE = "You Win!";
    private static final int GREEN_COLOR_COUNTER = 1;
    private static final int YELLOW_COLOR_COUNTER = 2;
    private static final int STRATEGIES_INDEX_BOUND = 10;
    private static final float TURBO_VELOCITY_FACTOR = 1.4f;
    private static final int TURBO_MODE_MAX_COLLISIONS = 6;
    private static final int PUCKS_PER_BRICK = 2;
    private static final int FALLEN_HEART_VELOCITY = 100;

    private ImageReader imageReader;
    private SoundReader soundReader;
    private final int brickRows;
    private final int brickCols;
    private final Vector2 paddleSizes;
    private Renderable heartImage;
    private Ball ball;
    private int turboModeInitialCounter;
    private GameObject[] hearsList;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter loseCounter;
    private TextRenderable livesCounterRenderable;
    private Counter bricksCounter;
    private UserInputListener inputListener;
    private final Random random;
    private ImageRenderable paddleImage;
    private boolean turboMode;
    private Paddle mainPaddle;

    public BrickerGameManger(String windowTitle , Vector2 windowDimensions, int brickRows, int brickCols) {
        super(windowTitle, windowDimensions);
        this.paddleSizes = new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT);
        this.random = new Random();
        this.brickRows = brickRows;
        this.brickCols = brickCols;
        this.loseCounter = new Counter(MAX_LOSE);
    }

    @Override
    public void initializeGame(
            ImageReader imageReader,
            SoundReader soundReader, UserInputListener inputListener,
            WindowController windowController) {
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        windowController.setTargetFramerate(TARGET_FRAMERATE);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;

        //create ball
        this.turboMode = false;
        createBall();

        //create paddles
        createPaddle();

        //create walls
        createWalls();

        //create object background
        createBackground();

        //create bricks
        this.bricksCounter = new Counter(brickRows * brickCols);
        createBricks();

        //create hearts
        this.hearsList = new GameObject[MAX_LOSE + 1];
        this.heartImage = imageReader.readImage(HEART_IMAGE_PATH,true);
        createHearts();

        //create lives counter
        livesCounterRenderable = new TextRenderable(Integer.toString(MAX_LOSE));
        createLivesCounter();
    }

    public void removeBrick(Brick brick) {
        boolean removedBrick = this.gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS);
        if(removedBrick) {
            this.bricksCounter.decrement();
        }
    }

    public Vector2 getWindowDimensions() {
        return this.windowDimensions;
    }

    public void removeObject(GameObject object) {
        this.gameObjects().removeGameObject(object);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTurboMode();
        checkForGameEnd();
    }

    public void createPucks(Vector2 pucksLocation) {
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Puck[] pucks = new Puck[PUCKS_PER_BRICK];
        for (int index = 0; index < pucks.length; index++) {
            pucks[index] = new Puck(pucksLocation, new Vector2(BALL_RADIUS, BALL_RADIUS).mult(0.75f),
                    puckImage, collisionSound, this);
            double angle = this.random.nextDouble() * Math.PI;
            float velocityX = (float)Math.cos(angle) * BALL_VELOCITY;
            float velocityY = (float)Math.sin(angle) * BALL_VELOCITY;
            pucks[index].setVelocity(new Vector2(velocityX, velocityY));
            this.gameObjects().addGameObject(pucks[index]);
        }
    }

    public void createFallenHearts(Vector2 heartLocation) {
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Heart heart = new Heart(heartLocation, new Vector2(HEART_WIDTH, HEART_HEIGHT), this.heartImage,
                collisionSound, this);
        heart.setVelocity(new Vector2(0, FALLEN_HEART_VELOCITY));
        this.gameObjects().addGameObject(heart);
    }

    public void createPaddle() {
        this.paddleImage = this.imageReader.readImage(PADDLE_IMAGE_PATH,false);
        this.mainPaddle = new Paddle(Vector2.ZERO, this.paddleSizes,
                paddleImage, inputListener, windowDimensions);
        this.mainPaddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_INITIAL_DISTANCE_FROM_CENTER));
        gameObjects().addGameObject(this.mainPaddle);
    }

    public Renderable getPaddleImage() { return this.paddleImage; }

    public float getMainPaddleHeight() { return this.mainPaddle.getCenter().y(); }

    public void createExtraPaddle(Vector2 location, Counter counter) {
        ExtraPaddle extraPaddle = new ExtraPaddle(location, this.paddleSizes, paddleImage, inputListener,
                this, counter);
        gameObjects().addGameObject(extraPaddle);
    }

    public void removePaddle(Paddle paddle) {
        gameObjects().removeGameObject(paddle);
    }

    public void transferBallIntoTurboMode() {
        this.turboMode = true;
        this.turboModeInitialCounter = this.ball.getCollisionCounter();
        ImageRenderable ballImage = this.imageReader.readImage(BALL_TURBO_MODE_IMAGE_PATH,
                true);
        this.ball.renderer().setRenderable(ballImage);
        this.ball.setVelocity(this.ball.getVelocity().mult(TURBO_VELOCITY_FACTOR));
    }

    public void updateTurboMode() {
        int turboModeCounter = ball.getCollisionCounter() - this.turboModeInitialCounter;
        if(turboModeCounter == TURBO_MODE_MAX_COLLISIONS && this.turboMode) {
            this.turboMode = false;
            ImageRenderable ballImage = this.imageReader.readImage(BALL_IMAGE_PATH,
                    true);
            this.ball.renderer().setRenderable(ballImage);
            this.ball.setVelocity(this.ball.getVelocity().mult((float) 1 / TURBO_VELOCITY_FACTOR));
        }
    }

    public void incrementLivesCounter() {
        livesCounterRenderable.setString(Integer.toString(loseCounter.value()));
        gameObjects().addGameObject(hearsList[loseCounter.value()]);
    }

    public boolean getTurboMode() {
        return this.turboMode;
    }

    public Counter getLivesCounter() {
        return this.loseCounter;
    }

    private void createBall() {
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        ImageRenderable ballImage = this.imageReader.readImage(BALL_IMAGE_PATH,
                true);
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        float ballVelX = BALL_VELOCITY;
        float ballVelY = BALL_VELOCITY;
        if(random.nextBoolean()) {
            ballVelX *= -1;
        }
        if(random.nextBoolean()) {
            ballVelY *= -1;
        }
        this.ball.setVelocity(new Vector2(ballVelX, ballVelY));
        windowDimensions = windowController.getWindowDimensions();
        this.ball.setCenter(windowDimensions.mult(WINDOW_CENTER_FACTOR));
        this.gameObjects().addGameObject(this.ball);
    }

    private void createWalls() {
        Vector2[] wallPositions = {Vector2.ZERO, new Vector2(windowDimensions.x() - WALL_WIDTH,0),
                Vector2.ZERO};
        Vector2[] dimensions = {new Vector2(WALL_WIDTH,windowDimensions.y()), new Vector2(WALL_WIDTH,
                windowDimensions.y()), new Vector2(windowDimensions.x(), WALL_WIDTH)};
        for(int index = 0; index < wallPositions.length; index++) {
            GameObject wall = new Wall(Vector2.ZERO, dimensions[index], null);
            wall.setTopLeftCorner(wallPositions[index]);
            gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        }
    }

    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,
                false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()), backgroundImage);
        //TODO check later...
        CoordinateSpace CoordinateSpace = null;
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks() {
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory();
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH,false);
        //TODO check the problem of cutting bricks in the right edge.
        int brickWidth = (int) (windowDimensions.x() / brickCols);
        for (int row = 0; row < this.brickRows; row++) {
            for (int col = 0; col < this.brickCols; col++) {
                CollisionStrategy collisionStrategy = collisionStrategyFactory.buildCollisionStrategy(
                        this.random.nextInt(1, STRATEGIES_INDEX_BOUND + 1),
                        this);
                GameObject brick = new Brick(new Vector2(col * (brickWidth + DISTANCE_BETWEEN_BRICKS),
                        WALL_WIDTH + (row * BRICK_HEIGHT)), new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, collisionStrategy);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createLivesCounter() {
        GameObject livesCounter = new GameObject(new Vector2(0, windowDimensions.y() - HEART_HEIGHT),
                new Vector2(HEART_WIDTH,HEART_HEIGHT), livesCounterRenderable);
        gameObjects().addGameObject(livesCounter, Layer.UI);
        livesCounterRenderable.setColor(Color.GREEN);
    }

    private void createHearts() {
        for(int index = 0; index < MAX_LOSE; index++) {
            hearsList[index] = new GameObject(new Vector2((index + 1) * (HEART_WIDTH +
                    DISTANCE_BETWEEN_HEARTS), windowDimensions.y() - HEART_HEIGHT),
                    new Vector2(HEART_WIDTH, HEART_HEIGHT), heartImage);
            gameObjects().addGameObject(hearsList[index], Layer.UI);
        }
        hearsList[MAX_LOSE - 1] = new GameObject(new Vector2((MAX_LOSE) * (HEART_WIDTH +
                DISTANCE_BETWEEN_HEARTS), windowDimensions.y() - HEART_HEIGHT),
                new Vector2(HEART_WIDTH, HEART_HEIGHT), heartImage);
    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        prompt = checkLose(ballHeight);
        if(prompt.equals("")) {
            prompt = checkWin();
        }
        checkPlayAgain(prompt);
    }

    private String checkWin() {
        if ((bricksCounter.value() == 0) || (this.inputListener.isKeyPressed(KeyEvent.VK_W))) {
            return WIN_MESSAGE;
        }
        return "";
    }

    private String checkLose(double ballHeight) {
        if(ballHeight > windowDimensions.y()) {
            ball.setCenter(windowDimensions.mult(WINDOW_CENTER_FACTOR));
            loseCounter.decrement();
            livesCounterRenderable.setString(Integer.toString(loseCounter.value()));
            switch (loseCounter.value()) {
                case GREEN_COLOR_COUNTER:
                    livesCounterRenderable.setColor(Color.RED);
                    break;
                case YELLOW_COLOR_COUNTER:
                    livesCounterRenderable.setColor(Color.YELLOW);
                    break;
                default:
                    livesCounterRenderable.setColor(Color.GREEN);
            }
            gameObjects().removeGameObject(hearsList[loseCounter.value()], Layer.UI);
        }
        if (loseCounter.value() == 0){
            return LOSE_MESSAGE;
        }
        return "";
    }

    private void checkPlayAgain(String prompt) {
        if(!prompt.isEmpty()){
            prompt += PLAY_AGAIN_MESSAGE;
            if(windowController.openYesNoDialog(prompt)) {

                this.loseCounter.increaseBy(MAX_LOSE - this.loseCounter.value());
                livesCounterRenderable.setString(Integer.toString(loseCounter.value()));
                bricksCounter = new Counter(this.brickCols * this.brickRows);
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    public static void main(String[] args) {
        GameManager gameManager;
        if(args.length == 0) {
            gameManager = new BrickerGameManger(WINDOW_TITLE, new Vector2(WINDOW_WIDTH,
                    WINDOW_HEIGHT), DEFAULT_BRICKS_ROWS, DEFAULT_BRICKS_COLS);
        } else {
            gameManager = new BrickerGameManger(WINDOW_TITLE, new Vector2(WINDOW_WIDTH,
                    WINDOW_HEIGHT), Integer.parseInt(args[BRICKS_ROWS_INDEX]),
                    Integer.parseInt(args[BRICKS_COLS_INDEX]));
        }
        gameManager.run();
    }
}
