package com.company;

import javax.swing.*;

/**
 * This class just run our code or "NotePad" class.
 * @author Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since May.14.2020
 */
public class Run {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        NotePad notePad=new NotePad();
        notePad.show();
    }

}
