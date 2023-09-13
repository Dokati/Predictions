package Coordinate;

import Entity.instance.EntityInstance;

import java.util.Random;

public class Coordinate {
    Integer x;
    Integer y;

    public Coordinate(EntityInstance[][] grid){
        boolean found = false;
        Random random = new Random();

        while(!found){
            this.x = random.nextInt(grid.length);
            this.y = random.nextInt(grid[0].length);

            if(grid[x][y] == null){
                found = true;
            }
        }

    }
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void chooseRandomNeighbor(EntityInstance[][] grid) {
        boolean found = false;
        Random random = new Random();

        while(!found) {
            int choice = random.nextInt(5); // Generate a random number between 0 and 4

            switch (choice) {
                case 0:
                    if (x == 0) {
                        x = grid.length - 1;
                    } else {
                        x = x - 1;
                    }
                    break;
                case 1:
                    if (y == 0) {
                        y = grid[0].length - 1;
                    } else {
                        y = y - 1;
                    }
                    break;
                case 2:
                    if (x == (grid.length - 1)) {
                        x = 0;
                    } else {
                        x = x + 1;
                    }
                    break;
                case 3:
                    if (y == (grid[0].length - 1)) {
                        y = 0;
                    } else {
                        y = y + 1;
                    }
                    break;
                case 4:
                    break;
            }

            if(grid[x][y] == null){
                found = true;
            }
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
