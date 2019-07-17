package com.codecool.quest.logic.actors;

import com.codecool.quest.logic.Cell;
import com.codecool.quest.logic.CellType;
import com.codecool.quest.logic.interfaces.Drawable;
import com.codecool.quest.logic.interfaces.Movable;

import java.util.Arrays;
import java.util.List;

public abstract class Actor implements Drawable, Movable {
    protected Cell cell;
    private int health = 10;
    private String[] cheatNames = {"Michał", "Piotrek", "Janek", "Olek", "Daniel"};
    private int attackPower = 3;
    private String direction = "up";

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);

        List<String> cheatNamesList = Arrays.asList(cheatNames);
        String actorName = cell.getActor().getName();

        boolean isNextCellWall = nextCell.getType().equals(CellType.WALL);
        boolean isNextCellActor = nextCell.getActor() != null;
        boolean isNextCellDoorClosed = false;
        if (nextCell.getInteractable() != null){
            isNextCellDoorClosed = !nextCell.getInteractable().isPassable();
        }

        if (!isNextCellWall && !isNextCellActor && !isNextCellDoorClosed) {
            changeCell(nextCell);
        //checks if player name equals any name on cheatList
        }else if(cheatNamesList.contains(actorName)){
            changeCell(nextCell);
        }

    }

    protected boolean isMoveValid(Cell nextCell) {
        boolean isNextCellWall = nextCell.getType().equals(CellType.WALL);
        boolean isNextCellActor = nextCell.getActor() != null;

        return !isNextCellWall && !isNextCellActor;
    }

    protected void changeCell(Cell nextCell) {
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    public void printHealth(String msg){
        System.out.println(health+msg);
    }

    public void changeAttackPower(int change){
        attackPower += change;
    }

    public int getHealth() {
        return health;
    }

    public void changeHealth(int change){
        health += change;
    }

    public int getAttackPower(){
        return attackPower;
    }

    public String getDirection(){
        return direction;
    }

    public void changeDirection(String newDirection){
        direction = newDirection;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public Cell getNextCell(){
        switch (getDirection()){
            case "down":
                return getCell().getNeighbor(0,1);
            case "up":
                return getCell().getNeighbor(0,-1);
            case "left":
                return getCell().getNeighbor(-1,0);
            case "right":
                return getCell().getNeighbor(1,0);
        }
        return getCell();
    }

    public abstract String getName();

    public void receiveAttack(int receivedDamage, Actor player){
        changeHealth(-receivedDamage);
        if (health<=0){
            death();
        } else{
            if( player != null)
                this.attackPlayer(player);
        }
    }

    public void attackPlayer(Actor player){
        player.receiveAttack(getAttackPower(),null);
    }

    public void death(){
        System.out.println("DEATH");
        getCell().setActor(null);
    }
}
