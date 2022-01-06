package Assignment_GRP6;

import java.util.Random;

public class Shield {
    private static int ShieldSpawnCounter;
    private static int ShieldKilledCounter;
    
    private int ShieldHP;
    private int ShieldCurrentRow;
    private int ShieldCurrentColumn;
    
    Random r = new Random();
    
    public Shield() {
        ShieldHP = 20;
        GenerateShield();
    }
    
    // generate new Shield
    public void GenerateShield() {
        ShieldCurrentRow = r.nextInt(10);
        ShieldCurrentColumn = r.nextInt(10);
        ShieldSpawnCounter++;
    }

    // generate again Shield
    public void GenerateAgainShield() {
        ShieldCurrentRow = r.nextInt(10);
        ShieldCurrentColumn = r.nextInt(10);
    }
    
    // ShieldHP reduced by weapon
    public void setDamageOnShieldHP(int damage) {
        ShieldHP -= damage;
    }
    
    // Shield is killed HP <= 0, move to row 10
    public void setIsKilledShield() {
        ShieldCurrentRow = 10;
    }
    
    // getter
    public static int getShieldSpawnCounter() {
        return ShieldSpawnCounter;
    }

    public static int getShieldKilledCounter() {
        return ShieldKilledCounter;
    }

    public int getShieldHP() {
        return ShieldHP;
    }

    public int getShieldCurrentRow() {
        return ShieldCurrentRow;
    }

    public int getShieldCurrentColumn() {
        return ShieldCurrentColumn;
    }
    
    // setter, only used for testing
    /*
    public void setShieldSpawnCounter(int ShieldSpawnCounter) {
        this.ShieldSpawnCounter = ShieldSpawnCounter;
    }
    public void setShieldKilledCounter(int ShieldKilledCounter) {
        this.ShieldKilledCounter = ShieldKilledCounter;
    }
    public void setShieldHP(int ShieldHP) {
        this.ShieldHP = ShieldHP;
    }
    public void setShieldCurrentRow(int ShieldCurrentRow) {
        this.ShieldCurrentRow = ShieldCurrentRow;
    }
    public void setShieldCurrentColumn(int ShieldCurrentColumn) {
        this.ShieldCurrentColumn = ShieldCurrentColumn;
    }  
    */
}
