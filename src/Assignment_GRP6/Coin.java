package Assignment_GRP6;

public class Coin {
    private int coinPerHour;
    private int balance;
    
    public Coin() {
        balance = 50;
        coinPerHour = 5;  // default coin generation per hour
    }
   
    public int getCoinPerHour() {
        return coinPerHour;
    }
    
    // get the current coin balance
    public int getBalance() {
        return balance;
    }

    // if not in normal mode, set the coin generated per hour
    public void setCoinPerHour(int rate) {
        coinPerHour = rate;
    }
    
    // set new coin balance
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String addCoinPerHour() {
        balance += coinPerHour;
        return "Coin + " + coinPerHour;
    }
    
    // pay for the cost to upgrade wall or weapon and reset total cost to 0
    public void pay(int cost) {
        balance -= cost;
    } 
}
