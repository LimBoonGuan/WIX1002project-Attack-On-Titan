
package Assignment_GRP6;

public class Color {
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_BLACK = "\u001B[30m";
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_YELLOW = "\u001B[33m";
    private static final String TEXT_BLUE = "\u001B[34m";
    private static final String TEXT_PURPLE = "\u001B[35m";
    private static final String TEXT_CYAN = "\u001B[36m";
    private static final String TEXT_WHITE = "\u001B[37m";
    
    public static String colorize(String s, String color) {
        String str = "";
        switch (color) {
            case "black":
                str += TEXT_BLACK;
                break;
            case "red":
                str += TEXT_RED;
                break;
            case "green":
                str += TEXT_GREEN;
                break;
            case "yellow":
                str += TEXT_YELLOW;
                break;
            case "blue":
                str += TEXT_BLUE;
                break;
            case "purple":
                str += TEXT_PURPLE;
                break;
            case "cyan":
                str += TEXT_CYAN;
                break;
            case "white":
                str += TEXT_WHITE;
                break;
            default:
                str += TEXT_RESET;
                break;
        }
        return str += s + TEXT_RESET;
    }
    
    public static String showColorAvailable() {
        return "Black, Red, Green, Yellow, Blue, Purple, Cyan, White";
    }
}
