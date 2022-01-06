package Assignment_GRP6;

public class Ground {
    private final int GroundRow = 11;
    private final int GroundColumn = 11;
    private final int GroundSpace = 3;
    
    private String[][][] Ground = new String[GroundRow][GroundColumn][GroundSpace];
    private String[][][] temporaryGround = new String[GroundRow][GroundColumn][GroundSpace];
    
    private boolean[][][] SpaceForTitanIsEmpty = new boolean[GroundRow][GroundColumn][GroundSpace];
    private int SpaceAssignedToTitan;
    
    private int[][] CoordinateForTitanCounter = new int[GroundRow][GroundColumn];
    private boolean[][] CoordinateForTitanIsEmpty = new boolean[GroundRow][GroundColumn];
    
    private boolean[][] CoordinateForShieldIsEmpty = new boolean[GroundRow][GroundColumn];
    
    private final String ArmouredTitan = "A";
    private final String ColossusTitan = "C";
    private final String Shield = "#";
    private final String EmptySpace = " ";
    
    public Ground() {
        generateGround();
        refreshTemporaryGround();
    }
    
    // generate new Ground
    public void generateGround() {
        for (int row=0 ; row<GroundRow-1 ; row++) {
            for (int column=0 ; column<GroundColumn-1 ; column++) {
                for (int space=0 ; space<GroundSpace ; space++) {
                    Ground[row][column][space] = EmptySpace;
                    SpaceForTitanIsEmpty[row][column][space] = true;
                }
                CoordinateForTitanCounter[row][column] = 0;
                CoordinateForTitanIsEmpty[row][column] = true;
                CoordinateForShieldIsEmpty[row][column] = true;
            }
        }
    }
    // Ground[11][0][0] can be set as graveyard if necessary, not displayed
    // Ground[0-10][0-10][0-2] used as ground to be displayed
    // Ground[][][0] can be set as shield for titan
    // Ground[][][1-2] used as space for titan
    
    
    // --------------------------------------------------
    // Ground used as original(previous) reference
    // changes made to temporaryGround - add or remove - titan or shield
    // update Ground to be displayed
    // refresh temporaryGround when Ground is displayed (before changes)
    
    
    // check Maximum ArmouredTitan can be summoned at row 0
    public int checkMaxArmouredTitanCanBeSummoned() {
        int reply = 0;
        for (int column=0 ; column<CoordinateForTitanIsEmpty[0].length-1 ; column++) {
            if (CoordinateForTitanIsEmpty[0][column]) {
                reply++;
            }
        }
        return reply;
    }
    // check Maximum ColossusTitan can be summoned at row 0
    public int checkMaxColossusTitanCanBeSummoned() {
        int reply = 0;
        for (int column=0 ; column<CoordinateForTitanIsEmpty[9].length-1 ; column++) {
            if (CoordinateForTitanIsEmpty[9][column]) {
                reply++;
            }
        }
        return reply;
    }
    // check Maximum Shield can be summoned at row 0
    public int checkMaxShieldCanBeSummoned() {
        int reply = 0;
        for (int row=0 ; row<CoordinateForShieldIsEmpty.length-1 ; row++ ) {
            for (int column=0 ; column<CoordinateForShieldIsEmpty[row].length-1 ; column++) {
                if (CoordinateForTitanIsEmpty[row][column]) {
                    reply++;
                }
            }
        }
        return reply;
    }
    
    // check whether coordinate is available or not to putTitan
    public boolean checkCoordinateForTitanIsEmpty(ColossusTitan c) {
        int row = c.getColossusTitanCurrentRow();
        int column = c.getTemporaryColossusTitanColumn();
        boolean reply = CoordinateForTitanIsEmpty[row][column];
        return reply;
    }
    
    public boolean checkCoordinateForTitanIsEmpty(ArmouredTitan a) {
        int row = a.getTemporaryArmouredTitanRow();
        int column = a.getTemporaryArmouredTitanColumn();
        boolean reply = CoordinateForTitanIsEmpty[row][column];
        return reply;
    }
    
    // put Titan (Colossus or Armoured)
    // always remove Titan before moving, then only put Titan!!! important!!!
    public void putColossusTitan(ColossusTitan c) {
        int row = c.getColossusTitanCurrentRow();
        int column = c.getTemporaryColossusTitanColumn();
        if (CoordinateForTitanIsEmpty[row][column]) {
            for (int space=1 ; space<GroundSpace ; space++) {
                if (SpaceForTitanIsEmpty[row][column][space]) {
                    temporaryGround[row][column][space] = ColossusTitan;
                    SpaceAssignedToTitan = space;
                    SpaceForTitanIsEmpty[row][column][space] = false;
                    CoordinateForTitanCounter[row][column]++;
                    if (CoordinateForTitanCounter[row][column] >= 2) {
                        CoordinateForTitanIsEmpty[row][column] = false;
                    }
                    break;
                }
            }
        }
    }
    public void putArmouredTitan(ArmouredTitan a) {
        int row = a.getTemporaryArmouredTitanRow();
        int column = a.getTemporaryArmouredTitanColumn();
        if (CoordinateForTitanIsEmpty[row][column]) {
            for (int space=1 ; space<GroundSpace ; space++) {
                if (SpaceForTitanIsEmpty[row][column][space]) {
                    temporaryGround[row][column][space] = ArmouredTitan;
                    SpaceAssignedToTitan = space;
                    SpaceForTitanIsEmpty[row][column][space] = false;
                    CoordinateForTitanCounter[row][column]++;
                    if (CoordinateForTitanCounter[row][column] >= 2) {
                        CoordinateForTitanIsEmpty[row][column] = false;
                    }
                    break;
                }
            }
        }
    }
    public int getSpaceAssignedToTitan() {
        return SpaceAssignedToTitan;
    }
    
    // remove Titan (Colossus or Armoured)
    // always remove Titan before moving, then only put Titan!!! important!!!
    public void removeColossusTitan(ColossusTitan c) {
        int row = c.getColossusTitanCurrentRow();
        int column = c.getColossusTitanCurrentColumn();
        int space = c.getColossusTitanCurrentSpace();
        temporaryGround[row][column][space] = EmptySpace;
        SpaceForTitanIsEmpty[row][column][space] = true;
        CoordinateForTitanCounter[row][column]--;
        CoordinateForTitanIsEmpty[row][column] = true;
    }
    public void removeArmouredTitan(ArmouredTitan a) {
        int row = a.getArmouredTitanCurrentRow();
        int column = a.getArmouredTitanCurrentColumn();
        int space = a.getArmouredTitanCurrentSpace();
        temporaryGround[row][column][space] = EmptySpace;
        SpaceForTitanIsEmpty[row][column][space] = true;
        CoordinateForTitanCounter[row][column]--;
        CoordinateForTitanIsEmpty[row][column] = true;
    }
    
    // check whether coordinate is available or not to putShield
    public boolean checkCoordinateForShieldIsEmpty(Shield s) {
        int row = s.getShieldCurrentRow();
        int column = s.getShieldCurrentColumn();
        boolean reply = CoordinateForShieldIsEmpty[row][column];
        return reply;
    }
    // put or remove shield
    public void putShield(Shield s) {
        int row = s.getShieldCurrentRow();
        int column = s.getShieldCurrentColumn();
        if (CoordinateForShieldIsEmpty[row][column]) {
            temporaryGround[row][column][0] = Shield;
            CoordinateForShieldIsEmpty[row][column] = false;
        }
    }
    public void removeShield(Shield s) {
        int row = s.getShieldCurrentRow();
        int column = s.getShieldCurrentColumn();
        temporaryGround[row][column][0] = EmptySpace;
        CoordinateForShieldIsEmpty[row][column] = true;
    }
    
    // create or refresh a temporaryGround, as template to be changed
    public void refreshTemporaryGround() {
        temporaryGround = Ground;
    }
    
    // update Ground
    public void updateGround() {
        Ground = temporaryGround;
    }
    
    public String toString(int gameHour, Coin c) {
        updateGround();
        String str = "On The Ground\n";
        str += "Row\n";
        for (int row=0 ; row<GroundRow-1 ; row++) {
            str += String.format("%-3s ", row);
            for (int column=0 ; column<GroundColumn-1 ; column++) {
                for (int space=0 ; space<GroundSpace ; space++) {
                    str += Ground[row][column][space];
                }
                str += " ";
            }
            if (row == 0) {
                // append current game hour to row 0
                str += String.format("%5s HOUR %d", "", gameHour);
            } else if (row == 1) {
                // append current coin number to the row 1
                str += String.format("%5s Coin: %d", "", c.getBalance());
            }
            str += "\n";
        }
        refreshTemporaryGround();
        return str;
    }
}
