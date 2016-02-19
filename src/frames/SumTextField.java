package frames;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Created by khodackovskiy on 18.12.2015.
 */
public class SumTextField extends JFormattedTextField {

    private JLabel sumLabel;
    private NumberFormatter defaultFormatter;
    private NumberFormatter displayFormatter;
    private NumberFormatter editFormatter;
    private DefaultFormatterFactory salaryFactory;
    private ResourceBundle resString;

    public SumTextField(ResourceBundle resString) {

        super();
        this.resString = resString;

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

        sumLabel = new JLabel(resString.getString("lblSum"));
        sumLabel.setBounds(290, 200, 150, 15);

        return sumLabel;

    }

}
