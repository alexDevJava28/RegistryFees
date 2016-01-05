
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by khodackovskiy on 17.12.2015.
 */
public class CompaniesField extends JComboBox{

    JLabel labelCompanyName;
    Connection conn;
    Statement sta;
    public int caretPos=0;
    public JTextField inputField=null;

    public CompaniesField(Connection conn) {

        this.conn = conn;

        fillComboBox();
        setSelectedItem(null);

        setEditor(new BasicComboBoxEditor());
        setEditable(true);

    }

    public JLabel getLabel (){

        labelCompanyName = new JLabel("Company");
        labelCompanyName.setBounds(20, 200, 250, 15);

        return labelCompanyName;

    }

    public void fillComboBox(){

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        try {

            String select = "SELECT name FROM Companies ORDER BY name";

            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                while (result.next()){

                    model.addElement(result.getString(1));
                }

            }

            this.setModel(model);

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get Company name Error in Company name list");
            e.printStackTrace();
        }finally {

            try{

                if (sta != null){

                    sta.close();
                }
            }catch (SQLException e){

                e.printStackTrace();
            }
        }
    }

    public void setSelectedIndex(int index) {

        super.setSelectedIndex(index);

        inputField.setText(getItemAt(index).toString());
        inputField.setSelectionEnd(caretPos + inputField.getText().length());
        inputField.moveCaretPosition(caretPos);
    }

    public void setEditor(ComboBoxEditor editor) {

        super.setEditor(editor);

        if (editor.getEditorComponent() instanceof JTextField) {

            inputField = (JTextField) editor.getEditorComponent();

            inputField.addKeyListener(new KeyAdapter() {

                public void keyReleased( KeyEvent ev ) {

                    char key = ev.getKeyChar();

                    if (! (Character.isLetterOrDigit(key) || Character.isSpaceChar(key) )) return;

                    caretPos=inputField.getCaretPosition();
                    String text="";

                    try {

                        text=inputField.getText(0, caretPos);

                    }
                    catch (javax.swing.text.BadLocationException e) {

                        e.printStackTrace();
                    }

                    for (int i=0; i<getItemCount(); i++) {

                        String element = getItemAt(i).toString();

                        if (element.startsWith(text)) {

                            setSelectedIndex(i);
                            return;
                        }
                    }
                }
            });
        }
    }
}
