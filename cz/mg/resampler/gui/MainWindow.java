package cz.mg.resampler.gui;

import cz.mg.resampler.gui.dialogs.WindowClosingDialog;
import cz.mg.resampler.gui.dialogs.ImageGalleryDialog;
import cz.mg.resampler.gui.dialogs.OriginalPreviewDialog;
import cz.mg.resampler.gui.utilities.View;
import cz.mg.resampler.gui.components.ResamplerPreviewPanel;
import cz.mg.resampler.gui.components.StatusBar;
import cz.mg.resampler.gui.dialogs.ConfigurationDialog;
import cz.mg.resampler.gui.dialogs.ErrorDialog;
import cz.mg.resampler.gui.dialogs.ErrorLogDialog;
import cz.mg.resampler.gui.dialogs.ExportImageDialog;
import cz.mg.resampler.gui.dialogs.ImageFileDialog;
import cz.mg.resampler.gui.utilities.MenuUtilities;
import cz.mg.resampler.gui.utilities.ImageState;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;


public final class MainWindow extends javax.swing.JFrame {
    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final int DEFAULT_CONFIGURATION_PANEL_WIDTH = 320 + 24;
    
    private ImageState image = null;
    private final View view = new View();
    private StatusBar statusBar;
    private final ResamplerPreviewPanel[] previewPanels = new ResamplerPreviewPanel[]{
        new ResamplerPreviewPanel(this, "Top left"),
        new ResamplerPreviewPanel(this, "Top right"),
        new ResamplerPreviewPanel(this, "Bottom left"),
        new ResamplerPreviewPanel(this, "Bottom right"),
    };
    private ResamplerPreviewPanel selectedPreviewPanel;
    private boolean viewLock = false;
    private int cursorX = 0;
    private int cursorY = 0;

    
    public MainWindow() {
        initComponents();
        postInitComponent();
        postInitComponents();
        addListeners();
    }
    
    private void postInitComponent() {
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setLocationRelativeTo(null);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Main.png")).getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    private void addListeners(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                boolean close = WindowClosingDialog.show(MainWindow.this);
                if(close) dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                shutdown();
            }
        });
        MenuUtilities.addMenuAcceleratorListener(this, jPopupMenu);
        MenuUtilities.makeMenuConsumeClickEvent();
    }
    
    private void postInitComponents() {
        jPanelStatusContent.add(statusBar = new StatusBar(this));
        for(ResamplerPreviewPanel panel : previewPanels) jPanelPreview.add(panel);
        jButtonMenu.setBorderPainted(false);
        jButtonMenu.setFocusPainted(false);
        jButtonMenu.setContentAreaFilled(false);
        jSplitPane.setDividerLocation(DEFAULT_WIDTH - DEFAULT_CONFIGURATION_PANEL_WIDTH);
        floatValueSliderX.setMinValue(0.0f);
        floatValueSliderX.setMaxValue(1.0f);
        floatValueSliderY.setMinValue(1.0f); // switched min and max intentionally
        floatValueSliderY.setMaxValue(0.0f);
        floatValueSliderX.setValue(view.getRx());
        floatValueSliderY.setValue(view.getRy());
        setSelectedPreviewPanel(previewPanels[0]);
    }
    
    private boolean isReady(){
        return image != null;
    }
    
    public ImageState getImage() {
        return image;
    }
    
    private void setImage(ImageState image){
        this.image = image;
        zoomFit();
    }

    public View getView() {
        return view;
    }
    
    private void openGallery(){
        try {
            BufferedImage newImage = ImageGalleryDialog.show(this);
            if(newImage != null) setImage(new ImageState(newImage));
        } catch (Exception e) {
            ErrorDialog.show(this, "Could not open image", e);
        }
    }
    
    private void openFile(){
        try {
            BufferedImage newImage = ImageFileDialog.open(this);
            if(newImage != null) setImage(new ImageState(newImage));
        } catch (Exception e) {
            ErrorDialog.show(this, "Could not open image", e);
        }
    }
    
    private void saveFile(){
        if(!isReady()) return;
        ExportImageDialog.show(this, getSelectedPreviewPanel().getResampler(), image);
    }
    
    private void showMenu(){
        jPopupMenu.show(jButtonMenu, 0, jButtonMenu.getHeight());
    }
    
    private void showOriginalImage(){
        if(!isReady()) return;
        OriginalPreviewDialog.show(this, image.getSwingImage());
    }
    
    private void showConfiguration(){
        ConfigurationDialog.show(this);
        repaint();
    }
    
    private void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    private void updateViewX(){
        if(viewLock) return;
        view.setRx(floatValueSliderX.getValue());
        refresh();
    }
    
    private void updateViewY(){
        if(viewLock) return;
        view.setRy(floatValueSliderY.getValue());
        refresh();
    }
    
    public void updateSliderX(){
        viewLock = true;
        floatValueSliderX.setValue(view.getRx());
        viewLock = false;
    }
    
    public void updateSliderY(){
        viewLock = true;
        floatValueSliderY.setValue(view.getRy());
        viewLock = false;
    }
    
    private void zoomIn(){
        selectedPreviewPanel.updateView();
        view.zoomIn();
        refresh();
    }
    
    private void zoomOut(){
        selectedPreviewPanel.updateView();
        view.zoomOut();
        refresh();
    }
    
    private void zoomReset(){
        selectedPreviewPanel.updateView();
        view.zoomReset();
        refresh();
    }
    
    private void zoomFit(){
        selectedPreviewPanel.updateView();
        view.zoomFit();
        refresh();
    }
    
    private void centerView(){
        selectedPreviewPanel.updateView();
        view.setRx(0.5f);
        view.setRy(0.5f);
        updateSliderX();
        updateSliderY();
        refresh();
    }
    
    public void switchPanelMaximization(ResamplerPreviewPanel panel){
        boolean maximize = jPanelPreview.getComponentCount() > 1;
        jPanelPreview.removeAll();
        if(maximize){
            jPanelPreview.add(panel);
            ((GridLayout)jPanelPreview.getLayout()).setColumns(1);
            ((GridLayout)jPanelPreview.getLayout()).setRows(1);
        } else {
            for(ResamplerPreviewPanel p : previewPanels) jPanelPreview.add(p);
            ((GridLayout)jPanelPreview.getLayout()).setColumns(2);
            ((GridLayout)jPanelPreview.getLayout()).setRows(2);
        }
        jPanelPreview.revalidate();
        refresh();
    }

    public ResamplerPreviewPanel getSelectedPreviewPanel() {
        return selectedPreviewPanel;
    }
    
    public void setSelectedPreviewPanel(ResamplerPreviewPanel selectedPreviewPanel) {
        this.selectedPreviewPanel = selectedPreviewPanel;
        jScrollPaneConfiguration.setViewportView(selectedPreviewPanel.getConfigurationPanel());
        statusBar.update(null, null);
    }
    
    public void cancel(){
        for(ResamplerPreviewPanel panel : previewPanels) panel.cancel();
    }
    
    public void refresh(){
        for(ResamplerPreviewPanel panel : previewPanels) panel.refresh();
        statusBar.update(null, null);
    }

    public int getCursorX() {
        return cursorX;
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }
    
    private void switchShowCursor(){
        Configuration.getInstance().setShowCursorLocation(!Configuration.getInstance().isShowCursorLocation());
        repaint();
    }
    
    private void shutdown(){
        for(ResamplerPreviewPanel panel : previewPanels) panel.shutdown();
    }
    
    private void showErrorLog(){
        ErrorLogDialog.show(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemOpenGallery = new javax.swing.JMenuItem();
        jMenuItemOpenFile = new javax.swing.JMenuItem();
        jMenuItemSaveFile = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemShowOriginalImage = new javax.swing.JMenuItem();
        jMenuItemZoomIn = new javax.swing.JMenuItem();
        jMenuItemZoomOut = new javax.swing.JMenuItem();
        jMenuItemZoomReset = new javax.swing.JMenuItem();
        jMenuItemZoomFit = new javax.swing.JMenuItem();
        jMenuItemMaximizePreview = new javax.swing.JMenuItem();
        jMenuItemShowCursor = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemCancel = new javax.swing.JMenuItem();
        jMenuItemRefresh = new javax.swing.JMenuItem();
        jMenuItemConfiguration = new javax.swing.JMenuItem();
        jMenuItemErrorLog = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jToolBar = new javax.swing.JToolBar();
        jButtonMenu = new javax.swing.JButton();
        jButtonOpenGallery = new javax.swing.JButton();
        jButtonOpenFile = new javax.swing.JButton();
        jButtonSaveFile = new javax.swing.JButton();
        jButtonShowOriginalImage = new javax.swing.JButton();
        jButtonZoomIn = new javax.swing.JButton();
        jButtonZoomOut = new javax.swing.JButton();
        jButtonZoomReset = new javax.swing.JButton();
        jButtonZoomFit = new javax.swing.JButton();
        jButtonMaximizePreview = new javax.swing.JButton();
        jButtonShowCursor = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jButtonConfiguration = new javax.swing.JButton();
        jButtonErrorLog = new javax.swing.JButton();
        jPanelContent = new javax.swing.JPanel();
        jSplitPane = new javax.swing.JSplitPane();
        jPanelPreviewArea = new javax.swing.JPanel();
        floatValueSliderX = new cz.mg.resampler.gui.components.value.FloatValueSlider();
        floatValueSliderY = new cz.mg.resampler.gui.components.value.FloatValueSlider();
        jPanelPreview = new javax.swing.JPanel();
        jLabelCenter = new javax.swing.JLabel();
        jScrollPaneConfiguration = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jPanelStatus = new javax.swing.JPanel();
        jPanelStatusContent = new javax.swing.JPanel();
        jPanelStatusDummy = new javax.swing.JPanel();

        jMenuItemOpenGallery.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpenGallery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/OpenGallery.png"))); // NOI18N
        jMenuItemOpenGallery.setText("Open gallery");
        jMenuItemOpenGallery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenGalleryActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemOpenGallery);

        jMenuItemOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/OpenFile.png"))); // NOI18N
        jMenuItemOpenFile.setText("Open file");
        jMenuItemOpenFile.setToolTipText("");
        jMenuItemOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenFileActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemOpenFile);

        jMenuItemSaveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSaveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/SaveFile.png"))); // NOI18N
        jMenuItemSaveFile.setText("Save file as");
        jMenuItemSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveFileActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemSaveFile);
        jPopupMenu.add(jSeparator1);

        jMenuItemShowOriginalImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Original.png"))); // NOI18N
        jMenuItemShowOriginalImage.setText("Show original image");
        jMenuItemShowOriginalImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowOriginalImageActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemShowOriginalImage);

        jMenuItemZoomIn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomIn.png"))); // NOI18N
        jMenuItemZoomIn.setText("Zoom in");
        jMenuItemZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemZoomInActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemZoomIn);

        jMenuItemZoomOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomOut.png"))); // NOI18N
        jMenuItemZoomOut.setText("Zoom out");
        jMenuItemZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemZoomOutActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemZoomOut);

        jMenuItemZoomReset.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_NUMPAD1, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemZoomReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomReset.png"))); // NOI18N
        jMenuItemZoomReset.setText("Zoom reset");
        jMenuItemZoomReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemZoomResetActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemZoomReset);

        jMenuItemZoomFit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_NUMPAD0, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemZoomFit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomFit.png"))); // NOI18N
        jMenuItemZoomFit.setText("Zoom fit");
        jMenuItemZoomFit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemZoomFitActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemZoomFit);

        jMenuItemMaximizePreview.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemMaximizePreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Maximize.png"))); // NOI18N
        jMenuItemMaximizePreview.setText("Maximize preview");
        jMenuItemMaximizePreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMaximizePreviewActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemMaximizePreview);

        jMenuItemShowCursor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemShowCursor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Cursor.png"))); // NOI18N
        jMenuItemShowCursor.setText("Show cursor location");
        jMenuItemShowCursor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowCursorActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemShowCursor);
        jPopupMenu.add(jSeparator3);

        jMenuItemCancel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItemCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Stop.png"))); // NOI18N
        jMenuItemCancel.setText("Cancel");
        jMenuItemCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCancelActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemCancel);

        jMenuItemRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Refresh.png"))); // NOI18N
        jMenuItemRefresh.setText("Refresh");
        jMenuItemRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRefreshActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemRefresh);

        jMenuItemConfiguration.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Configuration.png"))); // NOI18N
        jMenuItemConfiguration.setText("Editor configuration");
        jMenuItemConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConfigurationActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemConfiguration);

        jMenuItemErrorLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Log.png"))); // NOI18N
        jMenuItemErrorLog.setText("Error log");
        jMenuItemErrorLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemErrorLogActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemErrorLog);
        jPopupMenu.add(jSeparator2);

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemExit);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Resampling");
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {0.0, 1.0, 0.0};
        getContentPane().setLayout(layout);

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);
        jToolBar.setAlignmentY(0.5F);
        jToolBar.setMaximumSize(new java.awt.Dimension(67, 32));
        jToolBar.setMinimumSize(new java.awt.Dimension(67, 32));
        jToolBar.setPreferredSize(new java.awt.Dimension(67, 32));

        jButtonMenu.setText("Menu");
        jButtonMenu.setFocusable(false);
        jButtonMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMenu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMenu);

        jButtonOpenGallery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/OpenGallery.png"))); // NOI18N
        jButtonOpenGallery.setToolTipText("Open gallery");
        jButtonOpenGallery.setFocusable(false);
        jButtonOpenGallery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpenGallery.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonOpenGallery.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonOpenGallery.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonOpenGallery.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpenGallery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenGalleryActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonOpenGallery);

        jButtonOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/OpenFile.png"))); // NOI18N
        jButtonOpenFile.setToolTipText("Open file");
        jButtonOpenFile.setFocusable(false);
        jButtonOpenFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpenFile.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonOpenFile.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonOpenFile.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonOpenFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenFileActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonOpenFile);

        jButtonSaveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/SaveFile.png"))); // NOI18N
        jButtonSaveFile.setToolTipText("Save file as");
        jButtonSaveFile.setFocusable(false);
        jButtonSaveFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveFile.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonSaveFile.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonSaveFile.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonSaveFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveFileActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonSaveFile);

        jButtonShowOriginalImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Original.png"))); // NOI18N
        jButtonShowOriginalImage.setToolTipText("Show original image");
        jButtonShowOriginalImage.setFocusable(false);
        jButtonShowOriginalImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonShowOriginalImage.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonShowOriginalImage.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonShowOriginalImage.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonShowOriginalImage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonShowOriginalImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowOriginalImageActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonShowOriginalImage);

        jButtonZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomIn.png"))); // NOI18N
        jButtonZoomIn.setToolTipText("Zoom in");
        jButtonZoomIn.setFocusable(false);
        jButtonZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZoomIn.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonZoomIn.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonZoomIn.setPreferredSize(new java.awt.Dimension(24, 24));
        jButtonZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomInActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonZoomIn);

        jButtonZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomOut.png"))); // NOI18N
        jButtonZoomOut.setToolTipText("Zoom out");
        jButtonZoomOut.setFocusable(false);
        jButtonZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZoomOut.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonZoomOut.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonZoomOut.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomOutActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonZoomOut);

        jButtonZoomReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomReset.png"))); // NOI18N
        jButtonZoomReset.setToolTipText("Zoom reset");
        jButtonZoomReset.setFocusable(false);
        jButtonZoomReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZoomReset.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonZoomReset.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonZoomReset.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonZoomReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZoomReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomResetActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonZoomReset);

        jButtonZoomFit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/ZoomFit.png"))); // NOI18N
        jButtonZoomFit.setToolTipText("Zoom fit");
        jButtonZoomFit.setFocusable(false);
        jButtonZoomFit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZoomFit.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonZoomFit.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonZoomFit.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonZoomFit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZoomFit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomFitActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonZoomFit);

        jButtonMaximizePreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Maximize.png"))); // NOI18N
        jButtonMaximizePreview.setToolTipText("Maximize preview");
        jButtonMaximizePreview.setFocusable(false);
        jButtonMaximizePreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMaximizePreview.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonMaximizePreview.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonMaximizePreview.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonMaximizePreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMaximizePreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMaximizePreviewActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMaximizePreview);

        jButtonShowCursor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Cursor.png"))); // NOI18N
        jButtonShowCursor.setToolTipText("Show cursor location");
        jButtonShowCursor.setFocusable(false);
        jButtonShowCursor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonShowCursor.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonShowCursor.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonShowCursor.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonShowCursor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonShowCursor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowCursorActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonShowCursor);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Stop.png"))); // NOI18N
        jButtonCancel.setToolTipText("Cancel");
        jButtonCancel.setFocusable(false);
        jButtonCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCancel.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonCancel.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonCancel.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonCancel);

        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Refresh.png"))); // NOI18N
        jButtonRefresh.setToolTipText("Refresh");
        jButtonRefresh.setFocusable(false);
        jButtonRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRefresh.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonRefresh.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonRefresh.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonRefresh);

        jButtonConfiguration.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Configuration.png"))); // NOI18N
        jButtonConfiguration.setToolTipText("Editor configuration");
        jButtonConfiguration.setFocusable(false);
        jButtonConfiguration.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConfiguration.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonConfiguration.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonConfiguration.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonConfiguration.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigurationActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonConfiguration);

        jButtonErrorLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cz/mg/resampler/gui/icons/Log.png"))); // NOI18N
        jButtonErrorLog.setToolTipText("Error log");
        jButtonErrorLog.setFocusable(false);
        jButtonErrorLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonErrorLog.setMaximumSize(new java.awt.Dimension(26, 26));
        jButtonErrorLog.setMinimumSize(new java.awt.Dimension(26, 26));
        jButtonErrorLog.setPreferredSize(new java.awt.Dimension(26, 26));
        jButtonErrorLog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonErrorLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonErrorLogActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonErrorLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jToolBar, gridBagConstraints);

        jPanelContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        java.awt.GridBagLayout jPanelContentLayout = new java.awt.GridBagLayout();
        jPanelContentLayout.columnWeights = new double[] {1.0};
        jPanelContentLayout.rowWeights = new double[] {1.0};
        jPanelContent.setLayout(jPanelContentLayout);

        jSplitPane.setResizeWeight(1.0);

        java.awt.GridBagLayout jPanelPreviewAreaLayout = new java.awt.GridBagLayout();
        jPanelPreviewAreaLayout.columnWeights = new double[] {1.0, 0.0};
        jPanelPreviewAreaLayout.rowWeights = new double[] {1.0, 0.0};
        jPanelPreviewArea.setLayout(jPanelPreviewAreaLayout);

        floatValueSliderX.setToolTipText("");
        floatValueSliderX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                floatValueSliderXStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelPreviewArea.add(floatValueSliderX, gridBagConstraints);

        floatValueSliderY.setToolTipText("");
        floatValueSliderY.setOrientation(cz.mg.resampler.gui.components.value.FloatValueSlider.Orientation.VERTICAL);
        floatValueSliderY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                floatValueSliderYStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelPreviewArea.add(floatValueSliderY, gridBagConstraints);

        jPanelPreview.setLayout(new java.awt.GridLayout(2, 2, 2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelPreviewArea.add(jPanelPreview, gridBagConstraints);

        jLabelCenter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCenter.setText("c");
        jLabelCenter.setToolTipText("Double click to center view.");
        jLabelCenter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelCenterMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelPreviewArea.add(jLabelCenter, gridBagConstraints);

        jSplitPane.setLeftComponent(jPanelPreviewArea);

        jButton1.setText("jButton1");
        jScrollPaneConfiguration.setViewportView(jButton1);

        jSplitPane.setRightComponent(jScrollPaneConfiguration);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelContent.add(jSplitPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanelContent, gridBagConstraints);

        jPanelStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        java.awt.GridBagLayout jPanelStatusLayout = new java.awt.GridBagLayout();
        jPanelStatusLayout.columnWeights = new double[] {0.0, 1.0};
        jPanelStatusLayout.rowWeights = new double[] {1.0};
        jPanelStatus.setLayout(jPanelStatusLayout);

        jPanelStatusContent.setLayout(new java.awt.GridLayout(1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStatus.add(jPanelStatusContent, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelStatus.add(jPanelStatusDummy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanelStatus, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuActionPerformed
        showMenu();
    }//GEN-LAST:event_jButtonMenuActionPerformed

    private void jMenuItemOpenGalleryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenGalleryActionPerformed
        openGallery();
    }//GEN-LAST:event_jMenuItemOpenGalleryActionPerformed

    private void jButtonShowOriginalImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowOriginalImageActionPerformed
        showOriginalImage();
    }//GEN-LAST:event_jButtonShowOriginalImageActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        exit();
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenFileActionPerformed
        openFile();
    }//GEN-LAST:event_jMenuItemOpenFileActionPerformed

    private void floatValueSliderXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_floatValueSliderXStateChanged
        updateViewX();
    }//GEN-LAST:event_floatValueSliderXStateChanged

    private void floatValueSliderYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_floatValueSliderYStateChanged
        updateViewY();
    }//GEN-LAST:event_floatValueSliderYStateChanged

    private void jLabelCenterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelCenterMouseClicked
        if(evt.getClickCount() == 2) centerView();
    }//GEN-LAST:event_jLabelCenterMouseClicked

    private void jButtonZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomInActionPerformed
        zoomIn();
    }//GEN-LAST:event_jButtonZoomInActionPerformed

    private void jButtonZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomOutActionPerformed
        zoomOut();
    }//GEN-LAST:event_jButtonZoomOutActionPerformed

    private void jButtonZoomResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomResetActionPerformed
        zoomReset();
    }//GEN-LAST:event_jButtonZoomResetActionPerformed

    private void jButtonZoomFitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomFitActionPerformed
        zoomFit();
    }//GEN-LAST:event_jButtonZoomFitActionPerformed

    private void jButtonMaximizePreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMaximizePreviewActionPerformed
        switchPanelMaximization(selectedPreviewPanel);
    }//GEN-LAST:event_jButtonMaximizePreviewActionPerformed

    private void jButtonOpenGalleryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenGalleryActionPerformed
        openGallery();
    }//GEN-LAST:event_jButtonOpenGalleryActionPerformed

    private void jButtonOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenFileActionPerformed
        openFile();
    }//GEN-LAST:event_jButtonOpenFileActionPerformed

    private void jButtonSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveFileActionPerformed
        saveFile();
    }//GEN-LAST:event_jButtonSaveFileActionPerformed

    private void jMenuItemZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemZoomInActionPerformed
        zoomIn();
    }//GEN-LAST:event_jMenuItemZoomInActionPerformed

    private void jMenuItemZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemZoomOutActionPerformed
        zoomOut();
    }//GEN-LAST:event_jMenuItemZoomOutActionPerformed

    private void jMenuItemZoomResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemZoomResetActionPerformed
        zoomReset();
    }//GEN-LAST:event_jMenuItemZoomResetActionPerformed

    private void jMenuItemZoomFitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemZoomFitActionPerformed
        zoomFit();
    }//GEN-LAST:event_jMenuItemZoomFitActionPerformed

    private void jMenuItemShowOriginalImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShowOriginalImageActionPerformed
        showOriginalImage();
    }//GEN-LAST:event_jMenuItemShowOriginalImageActionPerformed

    private void jMenuItemMaximizePreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMaximizePreviewActionPerformed
        switchPanelMaximization(selectedPreviewPanel);
    }//GEN-LAST:event_jMenuItemMaximizePreviewActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        cancel();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jMenuItemCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCancelActionPerformed
        cancel();
    }//GEN-LAST:event_jMenuItemCancelActionPerformed

    private void jButtonConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigurationActionPerformed
        showConfiguration();
    }//GEN-LAST:event_jButtonConfigurationActionPerformed

    private void jMenuItemConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigurationActionPerformed
        showConfiguration();
    }//GEN-LAST:event_jMenuItemConfigurationActionPerformed

    private void jButtonShowCursorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowCursorActionPerformed
        switchShowCursor();
    }//GEN-LAST:event_jButtonShowCursorActionPerformed

    private void jMenuItemShowCursorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShowCursorActionPerformed
        switchShowCursor();
    }//GEN-LAST:event_jMenuItemShowCursorActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        refresh();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jMenuItemRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRefreshActionPerformed
        refresh();
    }//GEN-LAST:event_jMenuItemRefreshActionPerformed

    private void jMenuItemSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveFileActionPerformed
        saveFile();
    }//GEN-LAST:event_jMenuItemSaveFileActionPerformed

    private void jButtonErrorLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonErrorLogActionPerformed
        showErrorLog();
    }//GEN-LAST:event_jButtonErrorLogActionPerformed

    private void jMenuItemErrorLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemErrorLogActionPerformed
        showErrorLog();
    }//GEN-LAST:event_jMenuItemErrorLogActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow window = new MainWindow();
                window.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cz.mg.resampler.gui.components.value.FloatValueSlider floatValueSliderX;
    private cz.mg.resampler.gui.components.value.FloatValueSlider floatValueSliderY;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonConfiguration;
    private javax.swing.JButton jButtonErrorLog;
    private javax.swing.JButton jButtonMaximizePreview;
    private javax.swing.JButton jButtonMenu;
    private javax.swing.JButton jButtonOpenFile;
    private javax.swing.JButton jButtonOpenGallery;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSaveFile;
    private javax.swing.JButton jButtonShowCursor;
    private javax.swing.JButton jButtonShowOriginalImage;
    private javax.swing.JButton jButtonZoomFit;
    private javax.swing.JButton jButtonZoomIn;
    private javax.swing.JButton jButtonZoomOut;
    private javax.swing.JButton jButtonZoomReset;
    private javax.swing.JLabel jLabelCenter;
    private javax.swing.JMenuItem jMenuItemCancel;
    private javax.swing.JMenuItem jMenuItemConfiguration;
    private javax.swing.JMenuItem jMenuItemErrorLog;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemMaximizePreview;
    private javax.swing.JMenuItem jMenuItemOpenFile;
    private javax.swing.JMenuItem jMenuItemOpenGallery;
    private javax.swing.JMenuItem jMenuItemRefresh;
    private javax.swing.JMenuItem jMenuItemSaveFile;
    private javax.swing.JMenuItem jMenuItemShowCursor;
    private javax.swing.JMenuItem jMenuItemShowOriginalImage;
    private javax.swing.JMenuItem jMenuItemZoomFit;
    private javax.swing.JMenuItem jMenuItemZoomIn;
    private javax.swing.JMenuItem jMenuItemZoomOut;
    private javax.swing.JMenuItem jMenuItemZoomReset;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JPanel jPanelPreviewArea;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JPanel jPanelStatusContent;
    private javax.swing.JPanel jPanelStatusDummy;
    private javax.swing.JPopupMenu jPopupMenu;
    private javax.swing.JScrollPane jScrollPaneConfiguration;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
}
