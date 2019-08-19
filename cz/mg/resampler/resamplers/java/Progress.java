package cz.mg.resampler.resamplers.java;


/**
 *  Java implementation of the progress interface.
 */
public class Progress implements cz.mg.resampler.Progress {
    private volatile float progress = 0.0f;
    private volatile boolean update = false;
    private volatile boolean cancelled = false;
    
    private int steps = 0;
    private final int maxSteps;

    /**
     *  Creates progress object with given maximum number of steps for the resampler task.
     *  The value shall be computed using the resampler.getProgressStepCount method.
     *  If the value is set incorrectly, the progress property might then be greater than 1.
     *  This method shall be called only in the parent thread.
     *  @param maxSteps: the maximum number of steps to be set (sets 1 for values < 1)
     */
    public Progress(int maxSteps) {
        if(maxSteps < 1) maxSteps = 1;
        this.maxSteps = maxSteps;
    }
    
    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public void requestUpdate() {
        this.update = true;
    }
    
    @Override
    public int getMaxSteps(){
        return this.maxSteps;
    }
    
    /**
     *  Increments current step by one.
     *  This method shall be called only in the worker thread.
     *  @throws InterruptedException if the thread has been interrupted or if progress has been cancelled.
     */
    public void step() throws InterruptedException {
        if(Thread.interrupted()) throw new InterruptedException();
        if(cancelled) throw new InterruptedException();
        steps++;
        if(update){
            update = false;
            progress = (float)steps / (float)maxSteps;
        }
    }
    
    @Override
    public void cancel(){
        cancelled = true;
    }
}
