package newaot;

import java.util.Random;

public class ArmouredTitan {
    private static int ArmouredTitanSpawnCounter;
    private static int ArmouredTitanKilledCounter;
    
    private int ArmouredTitanHP;
    private final int ArmouredTitanAttack = 5;
    
    private int temporaryArmouredTitanRow = 0;
    private int ArmouredTitanCurrentRow = 0;
    
    private int temporaryArmouredTitanColumn;
    private int ArmouredTitanCurrentColumn;
    
    private int ArmouredTitanCurrentSpace;
    
    private int ClosestWeaponColumn;
    private int TestMoveCounter;
    Random r = new Random();
    
    public ArmouredTitan() {
        ArmouredTitanHP = 100;
        GenerateArmouredTitan();
        presetClosestWeaponColumn();
    }
    
    // generate new ArmouredTitan
    public void GenerateArmouredTitan() {
        ArmouredTitanCurrentColumn = r.nextInt(10);
        temporaryArmouredTitanColumn = ArmouredTitanCurrentColumn;
        ArmouredTitanSpawnCounter++;
    }
    // generate again ArmouredTitan
    public void GenerateAgainArmouredTitan() {
        ArmouredTitanCurrentColumn = r.nextInt(10);
        temporaryArmouredTitanColumn = ArmouredTitanCurrentColumn;
    }
    private void presetClosestWeaponColumn() {
        ClosestWeaponColumn = r.nextInt(10);
    }
    
    // set ArmouredTitanCurrentSpace after placed in a coordinate by Ground
    // remember to set ArmouredTitanCurrentSpace after put when generate new or move
    public void setArmouredTitanCurrentSpace(int spaceAssignedToTitan) {
        ArmouredTitanCurrentSpace = spaceAssignedToTitan;
    }
    
    // ArmouredTitanHP reduced by weapon
    public void setDamageOnArmouredTitanHP(int damage) {
        ArmouredTitanHP -= damage;
    }
    
    // ArmouredTitan is killed HP <= 0, move to row 10
    public void setIsKilledArmouredTitan() {
        temporaryArmouredTitanRow = 10;
        ArmouredTitanCurrentRow = 10;
    }
    
    // ArmouredTitan priotise movement towards weapon
    /*(delete this comment before submission)
    (tester class) default closestWeaponColumn = 0 or 9,
        always attack column 0 or 9 easier to code,
        but report just write random movement
    */
    public void setClosestWeaponColumn(int closestWeaponColumn) {
        this.ClosestWeaponColumn = closestWeaponColumn;
    }
    
    // Move
    public void MoveArmouredTitan() {
        TestMoveCounter = 0;
        temporaryArmouredTitanRow = ArmouredTitanCurrentRow;
        temporaryArmouredTitanColumn = ArmouredTitanCurrentColumn;
        // movement (row 9)
        if (ArmouredTitanCurrentRow == 9) {
            if (ClosestWeaponColumn > ArmouredTitanCurrentColumn) {
                temporaryArmouredTitanColumn++;
            }
            else if (ClosestWeaponColumn < ArmouredTitanCurrentColumn) {
                temporaryArmouredTitanColumn--;
            }
        }
        // movement (row 0 to 8)
        else {
            temporaryArmouredTitanRow++;
            if (ClosestWeaponColumn > ArmouredTitanCurrentColumn) {
                temporaryArmouredTitanColumn++;
            }
            else if (ClosestWeaponColumn < ArmouredTitanCurrentColumn) {
                temporaryArmouredTitanColumn--;
            }
            TestMoveCounter++; // counter=1
        }
    }
    // if Move is permitted, currentColumn = temporaryColumn, titan moves, remember to get currentColumn
    // remember to check PermitToMove again after RestrictedMove
    public void setPermitToMoveArmouredTitan() {
        ArmouredTitanCurrentRow = temporaryArmouredTitanRow;
        ArmouredTitanCurrentColumn = temporaryArmouredTitanColumn;
    }
    // if previous Move is restricted (if not checkCoordinateForTitanIsEmpty)
    // remember to check PermitToMove again after RestrictedMove
    public void MoveIsRestrictedArmouredTitan() {
        // if row 9 and Move is restricted, stay and attack
        if (ArmouredTitanCurrentRow != 9) {
            // back to previous column
            temporaryArmouredTitanColumn = ArmouredTitanCurrentColumn;
            // move again column, previous row
            if (TestMoveCounter >= 2) {
                temporaryArmouredTitanRow = ArmouredTitanCurrentRow;
                if (TestMoveCounter < 3) {
                    if (ClosestWeaponColumn > ArmouredTitanCurrentColumn) {
                        temporaryArmouredTitanColumn++;
                    }
                    if (ClosestWeaponColumn < ArmouredTitanCurrentColumn) {
                        temporaryArmouredTitanColumn--;
                    }
                }
            }
            TestMoveCounter++; // counter=2, 3
            // if counter=4, no movement
        }
    }
    // if no Move and row 9, ArmouredTitan will attack
    public boolean checkPermitToAttackArmouredTitan() {
        boolean attack = false;
        if (ArmouredTitanCurrentRow == 9 && ArmouredTitanCurrentColumn == temporaryArmouredTitanColumn) {
            attack = true;
        }
        return attack;
    }
    
    // getter
    public static int getArmouredTitanSpawnCounter() {
        return ArmouredTitanSpawnCounter;
    }
    public static int getArmouredTitanKilledCounter() {
        return ArmouredTitanKilledCounter;
    }
    public int getArmouredTitanHP() {
        return ArmouredTitanHP;
    }
    public int getArmouredTitanAttack() {
        return ArmouredTitanAttack;
    }
    // get temporary to check with Ground
    public int getTemporaryArmouredTitanRow() {
        return temporaryArmouredTitanRow;
    }
    public int getTemporaryArmouredTitanColumn() {
        return temporaryArmouredTitanColumn;
    }
    // get current if Move is permitted
    public int getArmouredTitanCurrentRow() {
        return ArmouredTitanCurrentRow;
    }
    public int getArmouredTitanCurrentColumn() {
        return ArmouredTitanCurrentColumn;
    }
    public int getArmouredTitanCurrentSpace() {
        return ArmouredTitanCurrentSpace;
    }
    public int getTestMoveCounter() {
        return TestMoveCounter;
    }
    
    // setter, only used for testing
    public static void setArmouredTitanSpawnCounter(int ArmouredTitanSpawnCounter) {
        ArmouredTitan.ArmouredTitanSpawnCounter = ArmouredTitanSpawnCounter;
    }
    public static void setArmouredTitanKilledCounter(int ArmouredTitanKilledCounter) {
        ArmouredTitan.ArmouredTitanKilledCounter = ArmouredTitanKilledCounter;
    }
    public void setTemporaryArmouredTitanRow(int temporaryArmouredTitanRow) {
        this.temporaryArmouredTitanRow = temporaryArmouredTitanRow;
    }
    public void setArmouredTitanCurrentRow(int ArmouredTitanCurrentRow) {
        this.ArmouredTitanCurrentRow = ArmouredTitanCurrentRow;
    }
    public void setTemporaryArmouredTitanColumn(int temporaryArmouredTitanColumn) {
        this.temporaryArmouredTitanColumn = temporaryArmouredTitanColumn;
    }
    public void setArmouredTitanCurrentColumn(int ArmouredTitanCurrentColumn) {
        this.ArmouredTitanCurrentColumn = ArmouredTitanCurrentColumn;
    }
    
}
