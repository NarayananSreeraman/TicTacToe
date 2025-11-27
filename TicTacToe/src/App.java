public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("[App] starting");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // CHANGE THIS LINE:
                new NeonTicTacToe(); 
                System.out.println("[App] creating NeonTicTacToe on EDT");
            }
        });
        System.out.println("[App] after invokeLater");
    }
}