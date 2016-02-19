package frames;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.util.Properties;

/**
 * Created by Пользователь on 27.12.2015.
 */
public class DatePicker {

    public JDatePickerImpl getjDatePicker() {

        UtilDateModel jmodel = new UtilDateModel();
        jmodel.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(jmodel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(680, 250, 200, 30);
        datePicker.setTextEditable(true);
        return datePicker;
    }
}
