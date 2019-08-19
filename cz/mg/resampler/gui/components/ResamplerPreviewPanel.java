package cz.mg.resampler.gui.components;

import cz.mg.resampler.StretchResampler;
import cz.mg.resampler.gui.MainWindow;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelMaximalization;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelMovement;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelResize;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelSelection;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelCursorMovement;
import cz.mg.resampler.gui.components.interaction.ResamplerPreviewPanelZoom;
import cz.mg.resampler.gui.Configuration;
import cz.mg.resampler.gui.dialogs.ErrorLogDialog;
import cz.mg.resampler.gui.utilities.DrawingUtilities;
import cz.mg.resampler.gui.utilities.ErrorUtilities;
import cz.mg.resampler.gui.utilities.View;
import cz.mg.resampler.gui.utilities.ImageState;
import cz.mg.resampler.Image;
import cz.mg.resampler.Worker;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ResamplerPreviewPanel extends BufferedPanel {
    private static final int UPDATE_DELAY = 100;
    private static final Font PROGRESS_BAR_FONT = new Font("default", Font.PLAIN, 14);
    private static final int PROGRESS_BAR_HEIGHT = 32;
    private static final Color PROGRESS_BAR_BACKGROUND_COLOR = new Color(0, 0, 0, 192);
    private static final Color PROGRESS_BAR_FOREGROUND_COLOR = new Color(255, 255, 255, 255);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    
    private final MainWindow myParent;
    private final ResamplerConfigurationPanel configurationPanel;
    private ImageState canvas = null;
    private final Worker worker = new Worker();
    private Worker.State lastState = null;
    
    private final Timer timer = new Timer(UPDATE_DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Worker.State state = worker.getState();
            switch(state){
                case READY: break;
                case RUNNING: repaint(); break;
                case CANCELED: break;
                case ERROR: break;
                case DONE:
                        canvas.updateFromResamplerImage(getResampler());
                        worker.clear();
                        break;
            }
            if(lastState != state){
                lastState = state;
                repaint();
            }
        }
    });

    public ResamplerPreviewPanel(MainWindow myParent, String name) {
        this.myParent = myParent;
        this.configurationPanel = new ResamplerConfigurationPanel(name);
        initComponent();
        configurationPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                refresh();
            }
        });
    }
    
    private void initComponent(){
        setBorder(BorderFactory.createEtchedBorder());
        ResamplerPreviewPanelSelection.apply(this);
        ResamplerPreviewPanelMaximalization.apply(this);
        ResamplerPreviewPanelMovement.apply(this);
        ResamplerPreviewPanelZoom.apply(this);
        ResamplerPreviewPanelResize.apply(this);
        ResamplerPreviewPanelCursorMovement.apply(this);
        timer.start();
    }

    public MainWindow getMyParent() {
        return myParent;
    }
    
    private ImageState getImage(){
        return myParent.getImage();
    }
    
    private View getView(){
        return myParent.getView();
    }
    
    public StretchResampler getResampler(){
        return configurationPanel.getResamplerConfiguration().getResamplerImplementation().getResampler();
    }
    
    public ResamplerConfigurationPanel getConfigurationPanel() {
        return configurationPanel;
    }
    
    @Override
    protected void draw(Graphics g){
        drawBackground(g);
        drawImage(g);
        drawCursor(g);
    }
    
    private void drawBackground(Graphics g){
        Configuration c = Configuration.getInstance();
        DrawingUtilities.drawChecker(
                g,
                this,
                0,
                0,
                getWidth(),
                getHeight(),
                c.getBackgroundCheckerWidth(),
                c.getBackgroundCheckerHeight(),
                c.getBackgroundColorOuterFirst(),
                c.getBackgroundColorOuterSecond()
        );
    }

    private void drawImage(Graphics g) {
        if(getImage() == null) return;
        if(getResampler() == null) return;
        
        updateView();
        drawImageBackground(g);
        
        if(worker.getState() == Worker.State.READY){
            drawCanvasImage(g);
        } else {
            drawPlaceholderImage(g);
            drawProgressBar(g);
        }
    }
    
    private void drawImageBackground(Graphics g){
        int ix = getView().sourceToDestinationX_F2I(0.0f);
        int iy = getView().sourceToDestinationY_F2I(0.0f);
        int ix2 = getView().sourceToDestinationX_F2I(getImage().getWidth());
        int iy2 = getView().sourceToDestinationY_F2I(getImage().getHeight());
        int iw = ix2 - ix;
        int ih = iy2 - iy;
        if(iw < 0) iw = 0;
        if(ih < 0) ih = 0;
        
        Configuration c = Configuration.getInstance();
        DrawingUtilities.drawChecker(
                g,
                this,
                ix+1,
                iy+1,
                iw-1,
                ih-1,
                c.getBackgroundCheckerWidth(),
                c.getBackgroundCheckerHeight(),
                c.getBackgroundColorFirst(),
                c.getBackgroundColorSecond()
        );
    }
    
    private void drawCanvasImage(Graphics g){
        g.drawImage(canvas.getSwingImage(), 0, 0, this);
    }
    
    private void drawPlaceholderImage(Graphics g){
        int ix = getView().sourceToDestinationX_F2I(0.0f);
        int iy = getView().sourceToDestinationY_F2I(0.0f);
        int ix2 = getView().sourceToDestinationX_F2I(getImage().getWidth());
        int iy2 = getView().sourceToDestinationY_F2I(getImage().getHeight());
        int iw = ix2 - ix;
        int ih = iy2 - iy;
        if(iw < 0) iw = 0;
        if(ih < 0) ih = 0;
        g.drawImage(getImage().getSwingImage(), ix, iy, iw, ih, this);
    }
    
    private void drawProgressBar(Graphics g){
        g.setColor(PROGRESS_BAR_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), PROGRESS_BAR_HEIGHT);
        
        g.setColor(PROGRESS_BAR_FOREGROUND_COLOR);
        g.setFont(PROGRESS_BAR_FONT);
        
        String message = "";
        switch(worker.getState()){
            case RUNNING:
                message = "Resampling: " + workerProgressToString();
                break;
            case DONE:
                message = "Resampling: " + workerProgressToString();
                break;
            case CANCELED:
                message = "Canceled";
                break;
            case ERROR:
                message = "Error";
                ErrorLogDialog.log(ErrorUtilities.generateLog(worker.getException()));
                break;
        }
        DrawingUtilities.drawTextCentered(g, message, 0, 0, getWidth(), PROGRESS_BAR_HEIGHT);
    }
    
    private String workerProgressToString(){
        float progress = worker.getProgress() * 100.0f;
        return DECIMAL_FORMAT.format(progress) + " %";
    }
        
    private void drawCursor(Graphics g){
        if(!Configuration.getInstance().isShowCursorLocation()) return;
        int cx = myParent.getCursorX();
        int cy = myParent.getCursorY();
        int w = getWidth();
        int h = getHeight();
        
        if(cx < 0 || cx >= w) return;
        if(cy < 0 || cy >= h) return;
        
        for(int x = 0; x < w; x++){
            g.setColor(DrawingUtilities.highlight(getBufferColor(x, cy)));
            g.drawLine(x, cy, x, cy);
        }
        
        for(int y = 0; y < h; y++){
            g.setColor(DrawingUtilities.highlight(getBufferColor(cx, y)));
            g.drawLine(cx, y, cx, y);
        }
    }
    
    private void updateCanvas(){
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if(panelWidth < 1) panelWidth = 1;
        if(panelHeight < 1) panelHeight = 1;
        if(canvas == null || panelWidth != canvas.getWidth() || panelHeight != canvas.getHeight()){
            try {
                canvas = new ImageState(panelWidth, panelHeight);
            } catch (Exception e) {
                canvas = null;
                ErrorLogDialog.log(ErrorUtilities.generateLog(e));
            }
        }
    }
    
    public void updateView(){
        updateViewSource();
        updateViewDestination();
    }
    
    private void updateViewSource(){
        View view = getView();
        ImageState image = getImage();
        view.setSourceWidth(image == null ? 0 : image.getWidth());
        view.setSourceHeight(image == null ? 0 : image.getHeight());
    }
    
    private void updateViewDestination(){
        View view = getView();
        view.setDestinationWidth(getWidth());
        view.setDestinationHeight(getHeight());
    }
    
    public void zoomIn(int mx, int my){
        updateView();
        myParent.getView().zoomIn(mx, my);
        myParent.refresh();
        myParent.updateSliderX();
        myParent.updateSliderY();
    }
    
    public void zoomOut(int mx, int my){
        updateView();
        myParent.getView().zoomOut(mx, my);
        myParent.refresh();
        myParent.updateSliderX();
        myParent.updateSliderY();
    }
    
    public void zoomReset(int mx, int my){
        updateView();
        myParent.getView().zoomReset(mx, my);
        myParent.refresh();
        myParent.updateSliderX();
        myParent.updateSliderY();
    }
    
    public void refresh(){
        cancel();
        
        updateCanvas();
        updateView();
        
        if(getImage() == null) return;
        if(getResampler() == null) return;
        
        Image[] images = getView().getImages(getImage().getResamplerImage(getResampler()), canvas.getResamplerImage(getResampler()));
        if(images == null) return;
        
        Image sourceImage = images[0];
        Image destinationImage = images[1];
        
        worker.start(getResampler(), sourceImage, destinationImage);
        repaint();
    }
    
    public void cancel(){
        worker.cancel();
        repaint();
    }
    
    public void shutdown(){
        cancel();
        timer.stop();
    }
}
