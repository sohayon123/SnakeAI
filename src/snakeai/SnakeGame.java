package snakeai;

import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;

public class SnakeGame extends Application {

    private Snake snake;
    private Food food;
    private Direction newDir = Direction.NONE;
    private int newX = 0, newY = 0, foodEaten = 0, highScore = 0;
    private final int SCREENWIDTH = 400, SCREENHEIGHT = 400, PIXELSIZE = 10;
    private Text scoreTxt, AItxt, highScoreTxt;
    private int [][] grid;
    private boolean AI = true, findFood = true;
    private Timeline timeline;
    private ArrayList<Direction> directions = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        Group root = new Group();
        primaryStage.setTitle("Snake");
        primaryStage.setScene(new Scene(root, SCREENWIDTH, SCREENHEIGHT, Color.BLACK));
        snake = new Snake();
        addSnakePart(root);
        addSnakePart(root);
        addSnakePart(root);
        food = new Food(PIXELSIZE);
        scoreTxt = new Text(10, 20, "Score: 0");
        scoreTxt.setFill(Color.YELLOW);
        scoreTxt.setFont(Font.font("Arial", 20));
        AItxt = new Text(340, 20, AI ? "AI On" : "AI Off");
        AItxt.setFill(Color.YELLOW);
        AItxt.setFont(Font.font("Arial", 20));
        highScoreTxt = new Text(10, 390, "high Score: N/A");
        highScoreTxt.setFill(Color.YELLOW);
        highScoreTxt.setFont(Font.font("Arial", 20));
        root.getChildren().addAll(scoreTxt, AItxt, highScoreTxt,food, snake.get(0));
        primaryStage.show();

        primaryStage.getScene().setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    if (snake.get(0).getDirection() != Direction.DOWN && !AI)
                        snake.changeDirection(Direction.UP);
                    break;
                case DOWN:
                    if (snake.get(0).getDirection() != Direction.UP && !AI)
                        snake.changeDirection(Direction.DOWN);
                    break;
                case LEFT:
                    if (snake.get(0).getDirection() != Direction.RIGHT && !AI)
                        snake.changeDirection(Direction.LEFT);
                    break;
                case RIGHT:
                    if (snake.get(0).getDirection() != Direction.LEFT && !AI)
                        snake.changeDirection(Direction.RIGHT);
                    break;
                case SPACE:
                    if (!AI) {
                        AI = true;
                        AItxt.setText("AI On");
                    } else if (AI) {
                        AI = false;
                        AItxt.setText("AI Off");
                    }

                    break;
            }
        });

        timeline = new Timeline(new KeyFrame(Duration.millis(10), (ActionEvent t) -> {
                    if (foodEaten >= SCREENWIDTH*SCREENHEIGHT/PIXELSIZE/PIXELSIZE)
                        stop(root);

                    checkSelfCollisions(root);
                    checkBoundaryCollisions(root);
                    checkFood(root);
                    if (AI) {
                        findNextMove();
                    }

                    snake.move();
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void addSnakePart(Group root) {
        Snakepart finalPart  = snake.get(snake.size()-1);
        newDir = finalPart.getDirection();
        newX = (int)finalPart.getX() + -1*(int)newDir.getX()*(int)finalPart.getDim();
        newY = (int)finalPart.getY() + -1*(int)newDir.getY()*(int)finalPart.getDim();
        snake.add(newDir, newX, newY, PIXELSIZE);
        root.getChildren().add(snake.get(snake.size()-1));
    }
    
    private void stop(Group root) {
        snake.stop();

        for (int i = 0; i < snake.size(); i++)
            root.getChildren().remove(snake.get(i));

        if (foodEaten > highScore) {
            highScore = foodEaten;
            highScoreTxt.setText("High Score: " + Integer.toString(highScore));
        }

        foodEaten = 0;
        scoreTxt.setText("Score: " + Integer.toString(foodEaten));
        snake = new Snake();
        addSnakePart(root);
        addSnakePart(root);
        addSnakePart(root);
        food.randomize();
        root.getChildren().add(snake.get(0));
    }

    private void checkBoundaryCollisions(Group root) {
        Snakepart s = snake.get(0);
        if (s.getX() < 0 || s.getX() + s.getDim() > SCREENWIDTH || s.getY() < 0 || s.getY() + s.getDim() > SCREENHEIGHT) {
            System.out.println("BOUNDARY");
            stop(root);
        }

    }

    private void checkSelfCollisions(Group root) {
        Snakepart head = snake.get(0);
        for (Snakepart s : snake) {
            if (head.getX() == s.getX() && head.getY() == s.getY() && !s.isHead()) {
                stop(root);
                System.out.println("SELF");
                break;
            }
        }
}

    private void checkFood(Group root) {
        for (int i = 0; i < snake.size(); i++){
            if (snake.get(i).getX() == food.getX() && snake.get(i).getY() == food.getY()) {
                if (i == 0) {
                    addSnakePart(root);
                    foodEaten++;
                    scoreTxt.setText("Score: " + Integer.toString(foodEaten));
                    findFood = false;
                }
                food.randomize();
            }
        }
    }

    private void findNextMove() {
            grid = new int[(SCREENWIDTH / PIXELSIZE)][SCREENHEIGHT / PIXELSIZE];

            for (int i = 0; i < SCREENWIDTH / PIXELSIZE; i++) {
                for (int j = 0; j < SCREENHEIGHT / PIXELSIZE; j++) {
                    grid[i][j] = 1;
                }
            }

            for (Snakepart s : snake) {
                if (s.getX() > 0 && s.getX() < SCREENWIDTH && s.getY() > 0 && s.getY() < SCREENHEIGHT)
                    grid[(int) (s.getX()) / PIXELSIZE][(int) (s.getY()) / PIXELSIZE] = 0;
            }

            grid[(int) snake.get(0).getX() / PIXELSIZE][(int) snake.get(0).getY() / PIXELSIZE] = 1;

            grid[(int) food.getX() / PIXELSIZE][(int) food.getY() / PIXELSIZE] = 1;

            int headX = (int) (snake.get(0).getX()) / PIXELSIZE;
            int headY = (int) (snake.get(0).getY()) / PIXELSIZE;
            int foodX = (int) (food.getX()) / PIXELSIZE;
            int foodY = (int) (food.getY()) / PIXELSIZE;

                double dist = (Math.abs(foodX - headX) + Math.abs(foodY - headY));
                double[] fitness = {10000, 10000, 10000, 10000};

                fitness[0] = (Math.abs(foodX - (headX - 1)) + Math.abs(foodY - headY));
                fitness[1] = (Math.abs(foodX - (headX + 1)) + Math.abs(foodY - headY));
                fitness[2] = (Math.abs(foodX - headX) + Math.abs(foodY - (headY - 1)));
                fitness[3] = (Math.abs(foodX - headX) + Math.abs(foodY - (headY + 1)));

                double min = Double.MAX_VALUE;

                if (headX >= 1) {
                    if (grid[headX - 1][headY] != 1) {
                        fitness[0] = 50000;
                    }
                } else if (headX == 0) {
                    fitness[0] = 100000;
                }

                if (headX < (SCREENWIDTH / PIXELSIZE - 1)) {
                    if (grid[headX + 1][headY] != 1) {
                        fitness[1] = 50000;
                    }
                } else if (headX == (SCREENWIDTH / PIXELSIZE - 1)) {
                    fitness[1] = 100000;
                }

                if (headY >= 1) {
                    if (grid[headX][headY - 1] != 1) {
                        fitness[2] = 50000;
                    }
                } else if (headY == 0) {
                    fitness[2] = 100000;
                }

                if (headY < (SCREENHEIGHT / PIXELSIZE - 1)) {
                    if (grid[headX][headY + 1] != 1) {
                        fitness[3] = 50000;
                    }
                } else if (headY == (SCREENWIDTH / PIXELSIZE - 1)) {
                    fitness[3] = 100000;
                }


                if ((headX == 0 && headY == 0) || (headX == 0 && headY == (SCREENWIDTH / PIXELSIZE - 1)))
                    findFood = true;
                if ((headX == (SCREENWIDTH / PIXELSIZE - 1) && headY == 0) || (headX == (SCREENWIDTH / PIXELSIZE - 1) && headY == (SCREENWIDTH / PIXELSIZE - 1)))
                    findFood = true;

                for (double d : fitness) {
                    if (d < min)
                        min = d;
                }

                for (int i = 0; i < fitness.length; i++) {
                    if (fitness[i] == min) {
                        if (i == 0 && snake.get(0).getDirection() != Direction.RIGHT) {
                            snake.changeDirection(Direction.LEFT);
                            headX--;
                        } else if (i == 1 && snake.get(0).getDirection() != Direction.LEFT) {
                            snake.changeDirection(Direction.RIGHT);
                            headX++;
                        } else if (i == 2 && snake.get(0).getDirection() != Direction.DOWN) {
                            snake.changeDirection(Direction.UP);
                            headY--;
                        } else if (i == 3 && snake.get(0).getDirection() != Direction.UP) {
                            snake.changeDirection(Direction.DOWN);
                            headY++;
                        }
                        break;
                    }
                }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
