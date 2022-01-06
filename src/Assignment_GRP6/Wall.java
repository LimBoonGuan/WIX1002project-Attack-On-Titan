package Assignment_GRP6;

public class Wall {
    private int hp = 50;

    // constructor, default hp is 50
    public Wall() {
        hp = 50;
    }
    
    public Wall(int hp) {
        this.hp = hp;
    }
    
    public int getHP() {
        return hp;
    }
    
    public void setHP(int hp) {
        this.hp = hp;
    }
    
    public void upgrade(int addHP) {
        hp += addHP;
    } 
    
    // display wall edge --- --- ---
    public static String printEdge(Wall[] w) {
        String str = "   ";
        for (int i = 0; i < w.length; i++) {
            String pattern = "---";
            if (w[i].getHP() <= 0) {
                pattern = "";
            }
            str += String.format("%4s", pattern);
        }
        return str;
    }
    
    // display the wall index 
    public static String printIndex(Wall[] w) {
        String str = "  ";
        for (int i = 0; i < w.length; i++) {
            str += String.format("%4d", i);
        }
        str += String.format("%5s %-10s\n", "", "Index");
        return str;
    }
    
    // display the wall HP
    public static String printHP(Wall[] w) {
        String str = "  ";
        for (int i = 0; i < w.length; i++) {
            str += String.format("%4d", w[i].getHP());
        }
        str += String.format("%5s %-10s\n", "", "HP");
        return str;
    }
    
    // display the wall pattern in front of ground
    public static String toString(Wall[] w) {    
        String str = "";
        str += printEdge(w);
        str += String.format("%5s%-10s\n", "", "The Wall");
        str += printIndex(w);
        str += printHP(w);
        str += printEdge(w);
        return str + "\n";
    }

    //reduce the wallHP due to the attack by Titans
    public void reduceHP(int attackPoint){
        hp -= attackPoint;     
    }
}
