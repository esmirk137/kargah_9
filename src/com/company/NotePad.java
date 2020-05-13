package com.company;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represent a notepad.
 * @author Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since May.14.2020
 */
public class NotePad {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ArrayList<Note> notes;
    private DefaultListModel demoList;

    /**
     * This is constructor of this class and implement GUI of our notePad.
     * @throws ClassNotFoundException thrown as a result of three different method calls, all of which handling loading classes by name.
     * @throws UnsupportedLookAndFeelException An exception that indicates the requested look & feel management classes are not present on the user's system.
     * @throws InstantiationException Thrown when an application tries to create an instance of a class using the newInstance method in class Class, but the specified class object cannot be instantiated.
     * @throws IllegalAccessException thrown when an application tries to reflectively create an instance (other than an array), set or get a field, or invoke a method, but the currently executing method does not have access to the definition of the specified class, field, method or constructor.
     */
    public NotePad() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // inti[
        notes=new ArrayList<>();
        demoList=new DefaultListModel();
        // inti]
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        // Frame[
        frame=new JFrame("notepad");
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Frame]
        JMenuBar menuBar=new JMenuBar();
        JMenu application=new JMenu("Application");
        application.setMnemonic('p');
        menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JMenuItem newNote=new JMenuItem(new DefaultEditorKit.CopyAction());
        newNote.setText("New note");
        newNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newNote.addActionListener(newNoteE->{
            actionOfNewNote();
        });
        JMenuItem copy=new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copy.addActionListener(copyE->{
            JTextArea text=(JTextArea)tabbedPane.getSelectedComponent();
            System.out.println(text.getText());
            StringSelection stringSelection=new StringSelection(text.getText());
            Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection,null);
        });
        JMenuItem save=new JMenuItem(new DefaultEditorKit.CopyAction());
        save.setText("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        save.addActionListener(saveE->{
            notes.get(tabbedPane.getSelectedIndex()).setText(((JTextArea)tabbedPane.getSelectedComponent()).getText());
            try (FileOutputStream fileOutputStream=new FileOutputStream("notes\\"+notes.get(tabbedPane.getSelectedIndex()).getTitle()+".txt")){
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(notes.get(tabbedPane.getSelectedIndex()));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            demoList.addElement(notes.get(tabbedPane.getSelectedIndex()).getTitle()+" |"+formatter.format(new Date())+" | "+((JTextArea)tabbedPane.getSelectedComponent()).getText());
        });
        JMenuItem close=new JMenuItem("Close");
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
        close.addActionListener(e ->{
            tabbedPane.remove(tabbedPane.getSelectedIndex());
        });
        JMenuItem exit=new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        exit.addActionListener(e ->{
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        application.add(newNote); application.add(copy); application.add(save); application.add(close); application.add(exit);
        menuBar.add(application);
        frame.setJMenuBar(menuBar);

        // main panel[
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        tabbedPane=new JTabbedPane();
        JPanel listPanel=new JPanel();
        listPanel.setLayout(new BorderLayout());
        JLabel label=new JLabel("List of notes");
        Dimension labelDimension=new Dimension();
        labelDimension.height=30;
        label.setPreferredSize(labelDimension);
        listPanel.add(label,BorderLayout.NORTH);
        Dimension dimension=new Dimension();
        dimension.width=200;
        listPanel.setPreferredSize(dimension);
        File[] file=textFileFinder();
        for(File i: file){
            try(FileInputStream fileInputStream=new FileInputStream("notes\\"+i.getName())){
                ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
                Note note= (Note) objectInputStream.readObject();
                notes.add(note);
                demoList.addElement(note.getTitle()+" |"+formatter.format(note.getDate())+" | "+note.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JList listOfNotes=new JList(demoList);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTextArea textArea=new JTextArea();
                    textArea.setText(notes.get(listOfNotes.getSelectedIndex()).getText());
                    tabbedPane.add(notes.get(listOfNotes.getSelectedIndex()).getTitle(),textArea);
                    frame.setVisible(true);
                }
            }
        };
        listOfNotes.addMouseListener(mouseListener);
        listPanel.add(listOfNotes,BorderLayout.CENTER);
        mainPanel.add(listPanel,BorderLayout.WEST);
        mainPanel.add(tabbedPane,BorderLayout.CENTER);
        frame.add(mainPanel);
        // main panel]
    }

    /**
     * This method do action of new note in menubar.
     */
    private void actionOfNewNote(){
        JFrame jFrame=new JFrame("New note");
        jFrame.setSize(300, 120);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel labelPanel=new JPanel();
        JLabel label=new JLabel("Enter title of your note");
        labelPanel.add(label);
        JTextField textField=new JTextField();
        JButton button=new JButton("Create");
        button.addActionListener(createE->{
            Note newNote=new Note(textField.getText(),"",new Date());
            notes.add(newNote);
            JTextArea jTextArea=new JTextArea();
            tabbedPane.add(newNote.getTitle(),jTextArea);
            frame.setVisible(true);
            jFrame.dispatchEvent(new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING));
        });
        panel.add(labelPanel,BorderLayout.NORTH);
        panel.add(textField,BorderLayout.CENTER);
        panel.add(button,BorderLayout.SOUTH);
        jFrame.add(panel);
        jFrame.setVisible(true);
    }

    /**
     * This method find text file in folder "notes".
     * @return these file as array
     */
    private File[] textFileFinder(){
        File dir = new File("notes");
        return dir.listFiles((dir1, filename) -> filename.endsWith(".txt"));
    }

    /**
     * This method show frame of our notepad.
     */
    public void show(){
        frame.setVisible(true);
    }

}
