package frames;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AutoCompletion extends PlainDocument {
    JComboBox comboBox;
    ComboBoxModel model;
    JTextComponent editor;

    // flag to indicate if setSelectedItem has been called
    // subsequent calls to remove/insertString should be ignored
    boolean selecting=false;
    boolean hitBackspace=false;
    boolean hitBackspaceOnSelection;

    KeyListener editorKeyListener;
    FocusListener editorFocusListener;

    Object currentItem;
    boolean searchOnOff = true;

    public AutoCompletion(JComboBox comboBox) {

        this.comboBox = comboBox;
        model = comboBox.getModel();

        comboBox.addActionListener(e -> {

            if (!selecting) highlightCompletedText(0);

        });

        comboBox.addPropertyChangeListener(e -> {

            if (e.getPropertyName().equals("editor")) configureEditor((ComboBoxEditor) e.getNewValue());
            if (e.getPropertyName().equals("model")) model = (ComboBoxModel) e.getNewValue();

        });

        editorKeyListener = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {

                if (comboBox.isDisplayable()) comboBox.setPopupVisible(true);

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_ENTER :
                        comboBox.setFocusable(false);
                        comboBox.setFocusable(true);
                        break;

                    case KeyEvent.VK_BACK_SPACE :
                        searchOnOff = true;
                        break;
                }
            }
        };

        configureEditor(comboBox.getEditor());
    }

    void configureEditor(ComboBoxEditor newEditor) {

        if (editor != null) {

            editor.removeKeyListener(editorKeyListener);
            editor.removeFocusListener(editorFocusListener);

        }

        if (newEditor != null) {

            editor = (JTextComponent) newEditor.getEditorComponent();
            editor.addKeyListener(editorKeyListener);
            editor.addFocusListener(editorFocusListener);
            editor.setDocument(this);

        }
    }

    public void remove(int offs, int len) throws BadLocationException {

        // return immediately when selecting an item
        if (selecting) return;
        if (hitBackspace) {
            // user hit backspace => move the selection backwards
            // old item keeps being selected
            if (offs>0) {
                if (hitBackspaceOnSelection) offs--;
            }

            highlightCompletedText(offs);

        } else {

            super.remove(offs, len);
        }
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        // return immediately when selecting an item
        if (selecting) {

            return;
        }

        // insert the string into the document
        super.insertString(offs, str, a);

        // lookup and select a matching item
        String text = getText(0, getLength());

        if (searchOnOff){

            Object item = lookupItem(text);
            setSelectedItem(item);
            setText(item.toString());

        }else{

            setSelectedItem(text);
            setText(text);
            comboBox.setPopupVisible(false);
        }

        // select the completed part
        highlightCompletedText(offs+str.length());
    }

    private void setText(String text) {

        try {
            // remove all text and insert the completed string
            super.remove(0, getLength());
            super.insertString(0, text, null);

        } catch (BadLocationException e) {

            throw new RuntimeException(e.toString());

        }
    }

    private void highlightCompletedText(int start) {

        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(start);
    }

    private void setSelectedItem(Object item) {

        selecting = true;
        model.setSelectedItem(item);
        selecting = false;

    }

    private Object lookupItem(String pattern) {

            // iterate over all items
            for (int i=0; i < model.getSize(); i++) {

                currentItem = model.getElementAt(i);

                // current item starts with the pattern?
                if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {

                    return currentItem;

                }
            }

        searchOnOff = false;
        return pattern;
    }

    // checks if str1 starts with str2 - ignores case
    private boolean startsWithIgnoreCase(String str1, String str2) {

        return str1.toUpperCase().startsWith(str2.toUpperCase());

    }
}