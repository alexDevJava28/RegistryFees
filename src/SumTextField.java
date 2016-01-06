
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created by khodackovskiy on 18.12.2015.
 */
public class SumTextField extends JFormattedTextField {

    JLabel sumLabel;
    NumberFormatter defaultFormatter;
    NumberFormatter displayFormatter;
    NumberFormatter editFormatter;
    DefaultFormatterFactory salaryFactory;

    public SumTextField() {

        super();

        // create the formatters, default, display, edit
        defaultFormatter = new NumberFormatter(new DecimalFormat("#,###.##"));
        displayFormatter = new NumberFormatter(new DecimalFormat("#,###.##"));
        editFormatter = new NumberFormatter(new DecimalFormat("#,###.##"));

        // set their value classes
        defaultFormatter.setValueClass(Double.class);
        displayFormatter.setValueClass(Double.class);
        editFormatter.setValueClass(Double.class);

        // create and set the DefaultFormatterFactory
        salaryFactory = new DefaultFormatterFactory(defaultFormatter,displayFormatter,editFormatter);
        this.setFormatterFactory(salaryFactory);

        this.setBounds(290, 220, 150, 25);
        this.setText(null);

    }

    public JLabel getLabel (){

        sumLabel = new JLabel("Company");
        sumLabel.setBounds(290, 200, 150, 15);

        return sumLabel;

    }

}
