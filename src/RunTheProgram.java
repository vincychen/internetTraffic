import javax.swing.JFrame;

import javax.swing.SwingUtilities;

public class RunTheProgram implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Window w = new Window();
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setSize(1000,500);
		w.setVisible(true);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RunTheProgram());
		}

}
