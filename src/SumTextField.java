
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by khodackovskiy on 18.12.2015.
 */
public class SumTextField extends JFormattedTextField {

    JLabel sumLabel;

    public SumTextField() {

        super();

        DecimalFormat decimalFormat = new DecimalFormat("0,00");

        this.setBounds(290, 220, 150, 25);
        this.setText(null);

    }

    public JLabel getLabel (){

        sumLabel = new JLabel("Company");
        sumLabel.setBounds(290, 200, 150, 15);

        return sumLabel;

    }
}
