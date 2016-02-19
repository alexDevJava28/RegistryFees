import frames.RegistryWindow;

import javax.swing.*;

/**
 * Created by khodackovskiy on 24.12.2015.
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new RegistryWindow());
    }
}
