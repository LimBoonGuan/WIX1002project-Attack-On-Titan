package Assignment_GRP6;

import java.util.Random;

public class ColossusTitan {
    private static int ColossusTitanSpawnCounter;
    private static int ColossusTitanKilledCounter;
    
    private int ColossusTitanHP;
    private final int ColossusTitanAttack = 10;
    
    private int ColossusTitanCurrentRow = 9;
    
    private int temporaryColossusTitanColumn;
    private int ColossusTitanCurrentColumn;
    
    private int ColossusTitanCurrentSpace;
    
    private int TestMoveCounter;
    Random r = new Random();
    
    public ColossusTitan() {
        ColossusTitanHP = 50;
        GenerateColossusTitan();
    }
    
    // generate new ColossusTitan
    public void GenerateColossusTitan() {
        ColossusTitanCurrentColumn = r.nextInt(10);
        temporaryColossusTitanColumn = ColossusTitanCurrentColumn;
        ColossusTitanSpawnCounter++;
    }
    // generate again ColossusTitan
    public void GenerateAgainColossusTitan() {
        ColossusTitanCurrentColumn = r.nextInt(10);
        temporaryColossusTitanColumn = ColossusTitanCurrentColumn;
    }
    
    // set ColossusTitanCurrentSpace after placed in a coordinate by Ground
    // remember to set ColossusTitanCurrentSpace after put when generate new or move
    public void setColossusTitanCurrentSpace(int spaceAssignedToTitan) {
        ColossusTitanCurrentSpace = spaceAssignedToTitan;
    }
    
    // ColossusTitanHP reduced by weapon
    public void setDamageOnColossusTitanHP(int damage) {
        ColossusTitanHP -= damage;
    }
    
    // ColossusTitan is killed HP <= 0, move to row 10
    public void setIsKilledColossusTitan() {
        ColossusTitanCurrentRow = 10;
    }
    
    // Move
    public void MoveColossusTitan() {
        temporaryColossusTitanColumn = ColossusTitanCurrentColumn;
        // movement (column 0 or 9)
        if (ColossusTitanCurrentColumn == 0) {
            temporaryColossusTitanColumn++;
        }
        else if (ColossusTitanCurrentColumn == 9) {
            temporaryColossusTitanColumn--;
        }
        else {
            // movement (column 1 to 8)
            TestMoveCounter = 0;
            int randomColumn = r.nextInt(2);
            // 0-left, 1-right
            if (randomColumn == 0) {
                temporaryColossusTitanColumn--;
            }
            else if (randomColumn == 1) {
                temporaryColossusTitanColumn++;
            }
            TestMoveCounter++;
        }
    }
    // if Move is permitted, currentColumn = temporaryColumn, titan moves, remember to get currentColumn
    // remember to check PermitToMove again after RestrictedMove
    public void setPermitToMoveColossusTitan() {
        ColossusTitanCurrentColumn = temporaryColossusTitanColumn;
    }
    // if previous Move is restricted (if not checkCoordinateForTitanIsEmpty)
    // remember to check PermitToMove again after RestrictedMove
    public void MoveIsRestrictedColossusTitan() {
        // move again column
        if (TestMoveCounter <= 2) {
            int randomColumn = r.nextInt(2);
            // 0-left, 1-right
            if (randomColumn == 0) {
                temporaryColossusTitanColumn--;
            }
            else if (randomColumn == 1) {
                temporaryColossusTitanColumn++;
            }
            TestMoveCounter++;
        }
    }
    
    // getter
    public static int getColossusTitanSpawnCounter() {
        return ColossusTitanSpawnCounter;
    }
    public static int getColossusTitanKilledCounter() {
        return ColossusTitanKilledCounter;
    }
    public int getColossusTitanHP() {
        return ColossusTitanHP;
    }
    public int getColossusTitanAttack() {
        return ColossusTitanAttack;
    }
    public int getColossusTitanCurrentRow() {
        return ColossusTitanCurrentRow;
    }
    // get temporaryColumn to check with Ground
    public int getTemporaryColossusTitanColumn() {
        return temporaryColossusTitanColumn;
    }
    // get currentColumn if Move is permitted
    public int getColossusTitanCurrentColumn() {
        return ColossusTitanCurrentColumn;
    }
    public int getColossusTitanCurrentSpace() {
        return ColossusTitanCurrentSpace;
    }
    public int getTestMoveCounter() {
        return TestMoveCounter;
    }
    
    // setter, only used for testing
    // delete when complie to avoid confusion
    public static void setColossusTitanSpawnCounter(int ColossusTitanSpawnCounter) {
        ColossusTitan.ColossusTitanSpawnCounter = ColossusTitanSpawnCounter;
    }
    public static void setColossusTitanKilledCounter(int ColossusTitanKilledCounter) {
        ColossusTitan.ColossusTitanKilledCounter = ColossusTitanKilledCounter;
    }
    public void setColossusTitanHP(int ColossusTitanHP) {
        this.ColossusTitanHP = ColossusTitanHP;
    }
    public void setTemporaryColossusTitanColumn(int temporaryColossusTitanColumn) {
        this.temporaryColossusTitanColumn = temporaryColossusTitanColumn;
    }
    public void setColossusTitanCurrentColumn(int ColossusTitanCurrentColumn) {
        this.ColossusTitanCurrentColumn = ColossusTitanCurrentColumn;
    }
}
