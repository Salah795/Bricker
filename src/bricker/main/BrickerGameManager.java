
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

/**
 * The BrickerGameManager class extends GameManager to implement the classic brick-breaking game.
 * It handles game initialization, object creation, user input, game updates, and end-of-game logic.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class BrickerGameManager extends GameManager {
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
    private static final int FIRST_POSSIBLE_RANDOM_INDEX = 6;

    private final int brickRows;
    private final int brickCols;
    private final Vector2 paddleSizes;
    private final Counter loseCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Renderable heartImage;
    private Ball ball;
    private int turboModeInitialCounter;
    private GameObject[] hearsList;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private TextRenderable livesCounterRenderable;
    private Counter bricksCounter;
    private UserInputListener inputListener;
    private final Random random;
    private ImageRenderable paddleImage;
    private boolean turboMode;
    private Paddle mainPaddle;
    private int extraPaddleCounter;

    /**
     * Constructs a new BrickerGameManager with the specified window title, dimensions,
     * and brick configuration.
     *
     * @param windowTitle      the title of the game window
     * @param windowDimensions the size of the game window as a Vector2 (width, height)
     * @param brickRows        the number of brick rows to generate
     * @param brickCols        the number of brick columns to generate
     */
    public BrickerGameManager(String windowTitle , Vector2 windowDimensions, int brickRows, int brickCols) {
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
        this.extraPaddleCounter = 0;
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

    /**
     * Removes the specified brick from the game world and decrements the brick counter.
     *
     * @param brick the Brick object to remove
     */
    public void removeBrick(Brick brick) {
        boolean removedBrick = this.gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS);
        if(removedBrick) {
            this.bricksCounter.decrement();
        }
    }

    /**
     * Returns the dimensions of the game window.
     *
     * @return a Vector2 containing the window width (x) and height (y)
     */
    public Vector2 getWindowDimensions() {
        return this.windowDimensions;
    }

    /**
     * Removes the specified game object from the game world.
     *
     * @param object the GameObject to remove
     */
    public void removeObject(GameObject object) {
        this.gameObjects().removeGameObject(object);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTurboMode();
        checkForGameEnd();
    }

    /**
     * Creates a burst of pucks at the given location when a brick is hit.
     * Each puck is given a random direction and added to the game.
     *
     * @param pucksLocation the spawn location for the pucks as a Vector2
     */
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

    /**
     * Creates a falling heart object at the specified location when the player gains a life.
     *
     * @param heartLocation the spawn location for the fallen heart as a Vector2
     */
    public void createFallenHearts(Vector2 heartLocation) {
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Heart heart = new Heart(heartLocation, new Vector2(HEART_WIDTH, HEART_HEIGHT), this.heartImage,
                collisionSound, this);
        heart.setVelocity(new Vector2(0, FALLEN_HEART_VELOCITY));
        this.gameObjects().addGameObject(heart);
    }

    /**
     * Creates the main paddle at its starting position centered horizontally near the bottom of the window.
     */
    public void createPaddle() {
        this.paddleImage = this.imageReader.readImage(PADDLE_IMAGE_PATH,false);
        this.mainPaddle = new Paddle(Vector2.ZERO, this.paddleSizes,
                paddleImage, inputListener, windowDimensions);
        this.mainPaddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_INITIAL_DISTANCE_FROM_CENTER));
        gameObjects().addGameObject(this.mainPaddle);
    }

    /**
     * Returns the image used to render the paddle.
     *
     * @return the Renderable representing the paddle texture
     */
    public Renderable getPaddleImage() { return this.paddleImage; }

    /**
     * Returns the vertical position of the main paddle's center (its height above the bottom).
     *
     * @return the Y-coordinate of the paddle center
     */
    public float getMainPaddleHeight() { return this.mainPaddle.getCenter().y(); }

    /**
     * Creates an extra paddle the first time it's triggered, positioned at the given location.
     *
     * @param location the spawn location for the extra paddle as a Vector2
     */
    public void createExtraPaddle(Vector2 location) {
        if(this.extraPaddleCounter == 0) {
            extraPaddleCounter++;
            ExtraPaddle extraPaddle = new ExtraPaddle(location, this.paddleSizes, paddleImage, inputListener,
                    this);
            gameObjects().addGameObject(extraPaddle);
        }
    }

    /**
     * Activates turbo mode on the ball: changes its texture, increases velocity, and tracks collision count.
     */
    public void transferBallIntoTurboMode() {
        this.turboMode = true;
        this.turboModeInitialCounter = this.ball.getCollisionCounter();
        ImageRenderable ballImage = this.imageReader.readImage(BALL_TURBO_MODE_IMAGE_PATH,
                true);
        this.ball.renderer().setRenderable(ballImage);
        this.ball.setVelocity(this.ball.getVelocity().mult(TURBO_VELOCITY_FACTOR));
    }

    /**
     * Checks if the ball has reached the maximum number of turbo collisions and resets normal mode if so.
     */
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

    /**
     * Increments the displayed lives counter and updates the heart UI and counter color accordingly.
     */
    public void incrementLivesCounter() {
        livesCounterRenderable.setString(Integer.toString(loseCounter.value()));
        gameObjects().addGameObject(hearsList[loseCounter.value() - 1], Layer.UI);
        updateLivesCounterColor();
    }

    /**
     * Indicates whether the ball is currently in turbo mode.
     *
     * @return true if turbo mode is active; false otherwise.
     */
    public boolean getTurboMode() {
        return this.turboMode;
    }

    /**
     * Returns the Counter tracking the remaining lives (allowed misses).
     *
     * @return a Counter representing the current lives remaining
     */
    public Counter getLivesCounter() {
        return this.loseCounter;
    }

    /**
     * Creates the ball object, sets a random initial direction and position at screen center,
     * and adds it to the game.
     */
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

    /**
     * Creates the boundary walls on the left, right, and top edges to contain the ball.
     */
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

    /**
     * Creates the game background image covering the entire window area.
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,
                false);
        GameObject background = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                windowDimensions.y()), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Generates the brick grid based on the configured rows and columns,
     * applying random collision strategies to each brick.
     */
    private void createBricks() {
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory();
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH,false);
        int brickWidth = (int) (windowDimensions.x() / brickCols);
        for (int row = 0; row < this.brickRows; row++) {
            for (int column = 0; column < this.brickCols; column++) {
                CollisionStrategy collisionStrategy = collisionStrategyFactory.buildCollisionStrategy(
                        this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX, STRATEGIES_INDEX_BOUND + 1),
                        this);
                GameObject brick = new Brick(new Vector2(column * (brickWidth + DISTANCE_BETWEEN_BRICKS),
                        WALL_WIDTH + (row * BRICK_HEIGHT)), new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, collisionStrategy);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Creates the on-screen lives counter text and positions it near the bottom-left corner.
     */
    private void createLivesCounter() {
        GameObject livesCounter = new GameObject(new Vector2(0, windowDimensions.y() - HEART_HEIGHT),
                new Vector2(HEART_WIDTH,HEART_HEIGHT), livesCounterRenderable);
        gameObjects().addGameObject(livesCounter, Layer.UI);
        livesCounterRenderable.setColor(Color.GREEN);
    }

    /**
     * Creates the heart icons used to visually represent remaining lives.
     */
    private void createHearts() {
        for(int index = 0; index < this.hearsList.length; index++) {
            hearsList[index] = new GameObject(new Vector2((index + 1) * (HEART_WIDTH +
                    DISTANCE_BETWEEN_HEARTS), windowDimensions.y() - HEART_HEIGHT),
                    new Vector2(HEART_WIDTH, HEART_HEIGHT), heartImage);
            if(index < MAX_LOSE) {
                gameObjects().addGameObject(hearsList[index], Layer.UI);
            }
        }
    }

    /**
     * Checks for win or lose conditions each frame and triggers the end-of-game dialog if needed.
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt;
        prompt = checkLose(ballHeight);
        if(prompt.isEmpty()) {
            prompt = checkWin();
        }
        checkPlayAgain(prompt);
    }

    /**
     * Checks if the player has cleared all bricks or pressed the win key.
     *
     * @return the win prompt string if game is won; empty string otherwise
     */
    private String checkWin() {
        if ((bricksCounter.value() == 0) || (this.inputListener.isKeyPressed(KeyEvent.VK_W))) {
            return WIN_MESSAGE;
        }
        return "";
    }

    /**
     * Updates the color of the lives counter text based on remaining lives:
     * green for many, yellow for moderate, red for low.
     */
    private void updateLivesCounterColor() {
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
    }

    /**
     * Checks if the ball has fallen below the bottom of the screen, decrements lives,
     * and resets ball position.
     *
     * @param ballHeight the Y-coordinate of the ball center
     * @return the loose prompt string if no lives remain; empty string otherwise
     */
    private String checkLose(double ballHeight) {
        if(ballHeight > windowDimensions.y()) {
            ball.setCenter(windowDimensions.mult(WINDOW_CENTER_FACTOR));
            loseCounter.decrement();
            livesCounterRenderable.setString(Integer.toString(loseCounter.value()));
            updateLivesCounterColor();
            gameObjects().removeGameObject(hearsList[loseCounter.value()], Layer.UI);
        }
        if (loseCounter.value() == 0){
            return LOSE_MESSAGE;
        }
        return "";
    }

    /**
     * Presents a Yes/No dialog prompting the player to play again or exit when game ends.
     *
     * @param prompt the message to display, including win/lose text
     */
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

    /**
     * The main entry point: parses optional rows/cols arguments and starts the game.
     *
     * @param args optional command-line arguments for brick rows and columns
     */
    public static void main(String[] args) {
        GameManager gameManager;
        if(args.length == 0) {
            gameManager = new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH,
                    WINDOW_HEIGHT), DEFAULT_BRICKS_ROWS, DEFAULT_BRICKS_COLS);
        } else {
            gameManager = new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_WIDTH,
                    WINDOW_HEIGHT), Integer.parseInt(args[BRICKS_ROWS_INDEX]),
                    Integer.parseInt(args[BRICKS_COLS_INDEX]));
        }
        gameManager.run();
    }
}
