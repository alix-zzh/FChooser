package filepanel;

import filechooser.FileChooser;
import filechooser.AddComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 23/04/17.
 */
public class FilePanel extends JComponent {


    private static final String ALL_FILE = "All file";
    private FileChooser myFileChooser;
    private JTextField patch;
    private FileViewPanel main;
    private JTextField choose;
    private List<String> extentions;
    private String extensionNow = ALL_FILE;

    public FilePanel(FileChooser myFileChooser) {
        this.myFileChooser = myFileChooser;
        this.extentions = myFileChooser.getExtentions();
        patch = new JTextField();
        patch.setEditable(false);
        String homePatch = System.getProperty("user.home");
        patch.setText(homePatch);
        choose = new JTextField();
        setPreferredSize(new Dimension(400, 350));
        setLayout(new BorderLayout());
        add(createHeaderPanel(), BorderLayout.NORTH);
        main = new FileTablePanel(this);
        add((JComponent) main);
        add(createChooserPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        AddComponent.makeButton(panel, "BACK.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                upClick();
            }
        });
        AddComponent.makeButton(panel, "HOME.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                homeClick();
            }
        });
        panel.add(patch);
        AddComponent.makeButton(panel, "TABLE.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                useTable();
            }
        });
        AddComponent.makeButton(panel, "LIST.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                useList();
            }
        });
        AddComponent.makeButton(panel, "TITLE.png", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                useTitle();
            }
        });
        return panel;
    }

    private void useTable() {
        remove((JComponent) main);
        main = new FileTablePanel(this);
        add((JComponent) main);
        main.updatePanel();
    }

    private void useList() {
        remove((JComponent) main);
        main = new FileListPanel(this);
        add((JComponent) main);
        main.updatePanel();
    }

    private void useTitle() {
        remove((JComponent) main);
        main = new FileTitlePanel(this);
        add((JComponent) main);
        main.updatePanel();
    }

    private void upClick() {
        String patch = getPatchText();
        if (patch.length() > 1) {
            if (!(patch.lastIndexOf(File.separator) == 0)) {
                setPatchText(patch.substring(0, patch.lastIndexOf(File.separator)));
                updatePanel();
            } else {
                setPatchText(File.separator);
                updatePanel();
            }
        }
    }

    private void homeClick() {
        setPatchText(System.getProperty("user.home"));
        updatePanel();
    }

    private JPanel createChooserPanel() {
        JPanel panel = new JPanel();
        JLabel fileName=new JLabel(" File name : ");
        panel.add(fileName);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(choose);
        if (extentions != null) {
            extentions.add(0, ALL_FILE);
            JComboBox<String> extentionBox = new JComboBox<String>();
            extentionBox.setModel(new DefaultComboBoxModel(extentions.toArray()));
            extentionBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    changeExtention(e);
                }
            });
            panel.add(extentionBox);
        }
        JButton okButton = new JButton("OK");
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                okClick();
            }
        });
        panel.add(okButton);
        return panel;
    }

    private void changeExtention(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        extensionNow = (String) cb.getSelectedItem();
        updatePanel();
    }

    private void okClick() {
        myFileChooser.setChooserFile(patch.getText() + File.separator+choose.getText());
        myFileChooser.closeDialog();
    }

    public void setPatchText(String text) {
        this.patch.setText(text);
    }

    public String getPatchText() {
        return patch.getText();
    }

    public void updatePanel() {
        main.updatePanel();
    }


    public void setChooseText(String choose) {
        this.choose.setText(choose);
    }

    public boolean compliesExtentions(File file) {
        if (extensionNow.equals(ALL_FILE)) {
            return true;
        } else {
            String fileExtention = getExtension(file.getName());
            return extensionNow.equals(fileExtention);
        }
    }

    private static String getExtension(String fileName) {
        String extension = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        return extension;
    }

    public List<String> getExtentions() {
        return extentions;
    }

    public List<File> getFiles(File folder) {
        List<File> listOfFiles = new ArrayList<File>();
        if (getExtentions() == null) {
            listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
        } else {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    listOfFiles.add(file);
                } else {
                    if (compliesExtentions(file)) {
                        listOfFiles.add(file);
                    }
                }
            }
        }
        return listOfFiles;
    }

}
