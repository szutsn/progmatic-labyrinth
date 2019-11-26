package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {

    private CellType[][] labyrinth;
    Coordinate coordinate;

    public LabyrinthImpl() {
        
    }

    @Override
    public int getWidth() {

        if (labyrinth == null){
            return -1;
        } else {
            return labyrinth.length;
        }
    }

    @Override
    public int getHeight() {

        if (labyrinth == null){
            return -1;
        } else {
            return labyrinth[0].length;
        }
    }

    @Override
    public void loadLabyrinthFile(String fileName) {

        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());

            setSize(width,height);

            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            setCellType(new Coordinate(ww,hh),CellType.WALL);
                            break;
                        case 'E':
                            setCellType(new Coordinate(ww,hh),CellType.END);
                            break;
                        case 'S':
                            setCellType(new Coordinate(ww,hh),CellType.START);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException | CellException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() < 0 || c.getRow() >= getHeight()
                || c.getCol() < 0 || c.getCol() >= getWidth()) {
            throw new CellException(c, "Nincs tovább a labirintus.");
        }
        return labyrinth[c.getCol()][c.getRow()];
    }

    @Override
    public void setSize(int width, int height) {
        labyrinth = new CellType[width][height];

        for (int i = 0; i < labyrinth.length; i++) {
            for (int j = 0; j < labyrinth[i].length; j++) {
                labyrinth[i][j] = CellType.EMPTY;
            }
        }
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {

        getCellType(c);
        labyrinth[c.getCol()][c.getRow()] = type;

        if (type == CellType.START) {
            coordinate = new Coordinate(c.getCol(), c.getRow());
        }
    }

    @Override
    public Coordinate getCoordinate() {

        return coordinate;
    }

    @Override
    public boolean hasPlayerFinished() {

        try {
            return getCellType(coordinate) == CellType.END;
        } catch (CellException ex) {
        }
        return false;
    }

    @Override
    public List<Direction> possibleMoves() {

        List<Direction> direction = new ArrayList<>();
        int col = coordinate.getCol();
        int row = coordinate.getRow();

        try {
            if (row > 0 && getCellType(new Coordinate(col, row - 1)) != CellType.WALL) {
                direction.add(Direction.NORTH);
            }
            if (col > 0 && getCellType(new Coordinate(col - 1, row)) != CellType.WALL) {
                direction.add(Direction.WEST);
            }
            if (row < getHeight() - 1 && getCellType(new Coordinate(col, row + 1)) != CellType.WALL) {
                direction.add(Direction.SOUTH);
            }
            if (col < getWidth() - 1 && getCellType(new Coordinate(col + 1, row)) != CellType.WALL) {
                direction.add(Direction.EAST);
            }
        } catch (CellException ex) {
            
        }
        return direction;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {

        int col = coordinate.getCol();
        int row = coordinate.getRow();
        List<Direction> pMoves = possibleMoves();

        if (pMoves.contains(direction)) {
            switch (direction) {
                case NORTH:
                    coordinate = new Coordinate(col, row - 1);
                    break;
                case WEST:
                    coordinate = new Coordinate(col - 1, row);
                    break;
                case SOUTH:
                    coordinate = new Coordinate(col, row + 1);
                    break;
                case EAST:
                    coordinate = new Coordinate(col + 1, row);
                    break;
            }
        } else {
            throw new InvalidMoveException();
        }
    }
}
