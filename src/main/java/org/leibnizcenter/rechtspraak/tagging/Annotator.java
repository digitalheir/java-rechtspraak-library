package org.leibnizcenter.rechtspraak.tagging;

import com.google.common.base.Strings;
import org.leibnizcenter.rechtspraak.tagging.crf.features.textpatterns.TitlePatterns;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.text.IgnoreElement;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.SameKindOfNumbering;
import org.leibnizcenter.rechtspraak.tokens.tokentree.leibniztags.LeibnizTags;
import org.leibnizcenter.util.Collections3;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import static org.leibnizcenter.rechtspraak.tokens.TokenList.getEcliFromFileName;


public class Annotator extends JFrame {
    private final ListSelectionModel listSelectionModel;
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton loadNewButton;
    private MyTableCellRenderer renderer;
    private MyJTable table1;
    private JButton reloadButton;
    private File currentFile;
    private TokenList currentDoc;
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = factory.newDocumentBuilder();
    private File xmlFolder = new File(Xml.IN_FOLDER_MANUAL_TAGGING);
    private File out = new File(Const.IN_FOLDER_TESTING);
    //    private List<File> xmlFiles = Lists.newArrayList(Xml.getFile(new File(IN_FOLDER),
//            "ECLI:NL:OGEAA:2015:386"
//    ));
    private List<File> xmlFiles = Xml.listXmlFiles(xmlFolder, -1, true);
    private Iterator<File> iterator = xmlFiles.iterator();
    private List<Label> labels;


    public Annotator(String name) throws ParserConfigurationException, IOException, SAXException {
        super(name);
        setContentPane(contentPane);
        //setModal(true);
//        getRootPane().setDefaultButton(buttonOK);
        reloadButton.addActionListener(e -> reloadCurrentDoc());
        buttonCancel.addActionListener(e -> onCancel());
        loadNewButton.addActionListener(e -> saveAndLoadNewDoc());

        contentPane.setPreferredSize(new Dimension(1100, 900));

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        TableColumnModel columnModel = new DefaultTableColumnModel();
        columnModel.addColumn(new TableColumn(0, 50));
        columnModel.addColumn(new TableColumn(1, 100));
        columnModel.addColumn(new TableColumn(2, 50));

        renderer = new MyTableCellRenderer();
        table1.setDefaultRenderer(Object.class, renderer);

        table1.setColumnModel(columnModel);

        table1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int column = table.columnAtPoint(p);
                if (column == MyTableModel.COLUMN_TEXT) {
                    if (row > 0) {
                        if (me.getClickCount() == 2) {
                            TokenTreeLeaf e = ((MyTableModel) table1.getModel()).getToken(row);
                            System.out.println(e.toString());
                        }
                    }
                }
            }
        });
        //
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_Q), KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_W), KeyStroke.getKeyStroke(KeyEvent.VK_W, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_L), KeyStroke.getKeyStroke(KeyEvent.VK_L, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_SEMICOLON), KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_E), KeyStroke.getKeyStroke(KeyEvent.VK_E, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_R), KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_T), KeyStroke.getKeyStroke(KeyEvent.VK_T, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        table1.registerKeyboardAction((e) -> saveAndLoadNewDoc(), KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction((e) -> saveAndLoadNewDoc(), KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction((e) -> saveAndLoadNewDoc(), KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction((e) -> saveAndLoadNewDoc(), KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_I), KeyStroke.getKeyStroke(KeyEvent.VK_I, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_O), KeyStroke.getKeyStroke(KeyEvent.VK_O, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_S), KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        table1.registerKeyboardAction(setRowsTo(KeyEvent.VK_A), KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        table1.setRowSelectionAllowed(true);
        table1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        listSelectionModel = table1.getSelectionModel();
        listSelectionModel.addListSelectionListener(
                new SharedListSelectionHandler());

        loadNewDoc();
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        Annotator dialog = new Annotator("Annotator");

        if (args.length > 0 && !Strings.isNullOrEmpty(args[0])) dialog.xmlFolder = new File(args[0]);
        if (args.length > 1 && !Strings.isNullOrEmpty(args[1])) dialog.out = new File(args[1]);


        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
//        System.exit(0);

    }

    public static void printToFile(Document root, File file) {
        try {
            file.getParentFile().mkdir();
                Xml.writeToStream(root, new FileWriter(file));
        } catch (IOException | TransformerException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            file.delete();
        }
    }

    private void reloadCurrentDoc() {
        try {
            loadDoc(currentFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private ActionListener setRowsTo(int k) {
        return (e) -> setSelectedRowsTo(getLabel(k));
    }

    private Label getLabel(int keystroke) {
        switch (keystroke) {
//            case KeyEvent.VK_E:
//                return Label.QUOTE_IN;
//            case KeyEvent.VK_Q:
//                return Label.NR_BLOCK;
//            case KeyEvent.VK_W:
//                return Label.QUOTE_IN;
            case KeyEvent.VK_O:
                return Label.TEXT_BLOCK;
//            case KeyEvent.VK_I:
//                return Label.INFO;
            case KeyEvent.VK_R:
                return Label.NR;
            case KeyEvent.VK_T:
                return Label.SECTION_TITLE;
            case KeyEvent.VK_A:
                return Label.TEXT_BLOCK;
//            case KeyEvent.VK_SEMICOLON:
//                return Label.LIST_ITEM_IN;
//            case KeyEvent.VK_A:
//                return Label.SUBSECTION_NR;
//            case KeyEvent.VK_S:
//                return Label.SUBSECTION_TEXT;
            default:
                return null;
        }
    }

    private void setSelectedRowsTo(Label l) {
        if (l == null) return;
        MyTableModel m = (MyTableModel) table1.getModel();
        int[] rows = table1.getSelectedRows();

        for (int i : rows) {
            m.setLabel(i, l);
//            if (Label.QUOTE_START.equals(l)) l = Label.QUOTE_IN; // For all following quote tags
        }
        table1.repaint();
    }

    private void saveAndLoadNewDoc() {
        if (currentFile != null) {
            String fileName = currentFile.getName();
            File outFile = new File(out, fileName);
            Document annotatedDoc = createDoc();
            printToFile(annotatedDoc, outFile);
        }
        try {
            loadNewDoc();
        } catch (IOException | SAXException e) {
            throw new Error(e);
        }
    }

    private Document createDoc() {
        MyTableModel model = (MyTableModel) table1.getModel();
        for (int i = 0; i < currentDoc.size(); i++) {
            Label label = model.getLabel(i);
            TokenTreeLeaf leaf = currentDoc.get(i);
            if (leaf instanceof RechtspraakElement) {
                ((RechtspraakElement) leaf).setAttribute(LeibnizTags.Attr.manualAnnotation, label.name());
            } else if (leaf instanceof Newline) {
                // It should be CLEAR that this is a newline
            } else {
                throw new IllegalStateException();
            }
        }
        return currentDoc.getSource();
    }

    private void loadNewDoc() throws IOException, SAXException {
        assert out.exists();
        assert out.isDirectory();

        do {
            File inFile = iterator.next();
            String fileName = inFile.getName();
            File outFile = new File(out, fileName);
            if (!outFile.exists()) {
                loadDoc(inFile);
                return;
            }
        } while (iterator.hasNext());
        throw new IllegalStateException();
    }

    private void loadDoc(File inFile) throws SAXException, IOException {
        currentFile = inFile;
        String ecli = getEcliFromFileName(inFile);
        Document doc = TokenList.getDoc(builder, inFile);

        TokenList from = TokenList.parse(ecli, doc, Xml.getContentRoot(doc));

        labels = DeterministicTagger.tag(from);

        System.out.println("http://uitspraken.rechtspraak.nl/inziendocument?id=" + from.getEcli());
        setListModel(labels, from);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table1.getColumnModel().getColumn(0).setPreferredWidth(30);
        table1.getColumnModel().getColumn(MyTableModel.COLUMN_NOTE).setPreferredWidth(100);
        table1.getColumnModel().getColumn(MyTableModel.COLUMN_TEXT).setPreferredWidth(600);
        table1.getColumnModel().getColumn(MyTableModel.COLUMN_TAG).setPreferredWidth(100);
    }

    private void setListModel(List<Label> labeledTokens, TokenList tokens) {
        currentDoc = tokens;
        DefaultTableModel lm = new MyTableModel(labeledTokens, tokens);

        JComboBox<Label> comboBox = new JComboBox<>();
        for (Label l : Label.values()) {
            comboBox.addItem(l);
        }
        comboBox.addActionListener((e) -> table1.repaint());

        table1.setModel(lm);

        DefaultCellEditor cellEditor = new DefaultCellEditor(comboBox);
        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });


        table1.getColumnModel()
                .getColumn(MyTableModel.COLUMN_TAG)
                .setCellEditor(cellEditor);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    static class MyTableModel extends DefaultTableModel {
        private static final int COLUMN_NOTE = 1;
        public static final int COLUMN_TEXT = 2;
        private static final int COLUMN_TAG = 3;
        private static final Color SUBSECTION_NR = Color.decode("#89e861");
        private static final Color SUBSECTION_TEXT = Color.decode("#cefcba");
        private static final Color SECTION_TEXT = Color.decode("#cefeff");
        private static final Color SECTION_NR = Color.decode("#97FDFF");
        private static final Color GRY = Color.decode("#EEEEEE");
        private static final Color DARK_RED = new Color(214, 84, 84);
        private static final Color LIME = Color.decode("#76D654");
        private static final Color RED = new Color(234, 184, 184);
        private static final Pattern LIST_START = Pattern.compile("^(?:(?:[a-h][ \\.:\\)])" +
                "|[0-9] ? ?[:\\.]|[\\*-])");
        private final List<Label> labels;
        private final TokenList tokens;

        public MyTableModel(List<Label> labels, TokenList tokens) {
            super(getRows(labels, tokens), new Object[]{"Nr", "Note", "Text", "Label"});
            this.labels = labels;
            this.tokens = tokens;
        }

        private static Object[][] getRows(List<Label> labels, TokenList tokens) {
            Object[][] ret = new Object[labels.size()][];
            for (int i = 0; i < labels.size(); i++) {
                TokenTreeLeaf token = tokens.get(i);

                labels.set(i, getBestGuessForTag(labels, tokens, i));
                List<String> notes = new LinkedList<>();

                if (IgnoreElement.passThrough(token.getNodeName())
                        || token.getNodeName().endsWith("nr")
                        || labels.get(i).equals(Label.NEWLINE)
                        ) {
                    notes.add(token.getNodeName());
                }

                Set<String> empasis = tokens.get(i).getEmphasis();
                if (null != empasis) {
                    String note;
                    if (empasis.size() != 0) note = String.join(",", empasis);
                    else note = "emph";
                    notes.add(note);
                }


                if (token instanceof Numbering
                        && !Collections3.isNullOrEmpty(((Numbering) token).getSequence(SameKindOfNumbering.ALPHABETIC))) {
                    notes.add("α-seq");
                }

                if (token instanceof Numbering
                        && !Collections3.isNullOrEmpty(((Numbering) token).getSequence(SameKindOfNumbering.ALPHABETIC))) {
                    notes.add("σ-seq");
                }
//                if (labels.get(i).equals(Label.SECTION_TITLE)) {
//                    notes.add("||");
//                }
//
//                if (token.followsNonEmptyText && token.precedesNonEmptyText) {
//                    notes.add("][");
//                } else if (token.followsNonEmptyText) {
//                    notes.add("]");
//                } else if (token.precedesNonEmptyText) {
//                    notes.add(0, "[");
//                }

                String notesStr = String.join(" ", notes);
                ret[i] = new Object[]{
                        i,
                        notesStr,
                        token.getTextContent(),
                        labels.get(i)
                };
                if (labels.get(i).equals(Label.SECTION_TITLE)
                        && !TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(tokens, i)
                        && !TitlePatterns.TitlesNormalizedMatchesLowConf.matchesAny(tokens, i)) {
                    System.err.println("http://uitspraken.rechtspraak.nl/" +
                            "inziendocument?id="
                            + tokens.getEcli()
                            + ": unexpected title \""
                            + token.getTextContent()
                            + "\"");
                }
            }
//            addInfoTagsIfNotPresent(ret);
            return ret;
        }

//        private static void addInfoTagsIfNotPresent(Object[][] rows) {
//            boolean infoFound = false;
//            for (Object[] row : rows) {
//                Label l = (Label) row[COLUMN_TAG];
//                if (Label.INFO.equals(l)) {
//                    infoFound = true;
//                    break;
//                }
//            }
//            if (!infoFound) {
//                // All labels before the first section title become INFO tag
//                for (Object[] row : rows) {
//                    row[COLUMN_TAG] = Label.INFO;
//                    Label l = (Label) row[COLUMN_TAG];
//                    if (Label.SECTION_TITLE.equals(l) || Label.SECTION_NR.equals(l)) {
//                        break;
//                    }
//                }
//            }
//        }

        private static Label getBestGuessForTag(List<Label> labels, List<TokenTreeLeaf> tokens, int i) {
            TokenTreeLeaf t = tokens.get(i);
            if (t instanceof Element) {
                Element e = (Element) t;
                if (e.hasAttribute("manualAnnotation")) {
                    System.out.println("Found pre-set " + e.getAttribute("manualAnnotation"));
                    return Label.fromString.get(e.getAttribute("manualAnnotation"));
                } else return labels.get(i);
            } else if (t instanceof Newline) return Label.NEWLINE;
            else throw new IllegalStateException();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
//            column == COLUMN_TAG;
        }

        public void fireRowChanged(int row) {
            fireTableRowsUpdated(row, row);
        }

        public Label getLabel(int row) {
            return (Label) ((Vector) dataVector.get(row)).get(COLUMN_TAG);
        }

        public Color getRowColor(int row) {
            switch (getLabel(row)) {
                case NEWLINE:
                    return GRY;
                case SECTION_TITLE:
                    return LIME;
//                case QUOTE:
//                    return RED;
//                case NR_FULL_INLINE:
//                    return SECTION_NR;
                case NR:
                    return SECTION_NR;
//                case NR_SUBSECTION_INLINE:
//                    return SECTION_NR;
//                case NR_SUBSECTION_BLOCK:
//                    return SECTION_NR;
//                case NR_BLOCK:
//                    return Color.YELLOW;
                case TEXT_BLOCK:
                    return Color.WHITE;
//                case SUBSECTION_NR:
//                    return SUBSECTION_NR;
//                case SUBSECTION_TEXT:
//                    return SUBSECTION_TEXT;
//                case QUOTE_IN:
//                    return RED;
//                case LIST_HEADING:
//                    return Color.ORANGE;
//                case LIST_ITEM_IN:
//                    return Color.YELLOW;
//                case QUOTE_START:
//                    return DARK_RED;
                default:
                    return Color.BLACK;
            }
        }

        public void setLabel(int row, Label l) {
            //noinspection unchecked
            ((Vector) dataVector.get(row)).set(COLUMN_TAG, l);
        }

        public TokenTreeLeaf getToken(int row) {
            return this.tokens.get(row);
        }
    }

    static class MyTableCellRenderer extends JTextArea implements TableCellRenderer {
        private final DefaultTableCellRenderer rrr;

        public MyTableCellRenderer() {
            rrr = new DefaultTableCellRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            MyTableModel model = (MyTableModel) table.getModel();

            Component c = rrr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JComponent jc = (JComponent) c;
            c.setBackground(model.getRowColor(row));

            ListSelectionModel selectionModel = table.getSelectionModel();
            int min = selectionModel.getMinSelectionIndex();
            int max = selectionModel.getMaxSelectionIndex();
            if (isRowSelected(min, max, row)) {
                jc.setBackground(new Color(0.6f, 0.6f, 0.8f, 0.2f));
                jc.setBorder(new MatteBorder(min == row ? 1 : 0, 0, max == row ? 1 : 0, 0, Color.BLUE));
            }

            if (column == MyTableModel.COLUMN_TEXT) {
                // Wrap
                this.setText(value.toString());
                this.setWrapStyleWord(true);
                this.setLineWrap(true);
                int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
                int textLength = this.getText().length();

                int cols = this.getColumns();
                int lines = (cols < 1 ? 0 : textLength / cols) + 1;//+1, cause we need at least 1 row.
                int height = fontHeight * lines;
                table.setRowHeight(row, height);
            }

            return c;
        }


        private boolean isRowSelected(int min, int max, int row) {
            return row >= min && row <= max;
        }
    }

    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            table1.repaint();
        }
    }

}
