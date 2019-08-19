package cz.mg.resampler;


/**
 *  Class that makes resampler multithreading easier.
 *  Parent thread shall use this class to communicate with the worker thread.
 */
public class Worker {
    private final ReusableThread thread = new ReusableThread();
    private Progress progress;
    private volatile State state = State.READY;
    private volatile Exception exception = null;

    public Worker() {
    }
    
    /**
     *  Starts a new resampler task in a new thread.
     *  The worker changes states based on what it is currently doing.
     *  The initial state is READY.
     *  After the start method has been called, the state chagnes to RUNNING.
     *  When the resampling finishes, the state changes to either DONCE, CANCELED or ERROR.
     *  If the state results in ERROR, an exception can be retrieved with getException.
     *  Important note: source and destination images must be compatible with resampler
     *  (for example java resampler should use java image implementations)
     *  @param resampler: the resampler to be used for resampling
     *  @param sourceImage: the source image that shall be resampled
     *  @param destinationImage: the destination image where the result will be stored
     */
    public void start(final Resampler resampler, final Image sourceImage, final Image destinationImage){
        cancel();
        createProgress(resampler, sourceImage, destinationImage);
        state = State.RUNNING;
        thread.start(new Runnable() {
            @Override
            public void run() {
                try {
                    destinationImage.clear();
                    resampler.resample(sourceImage, destinationImage, Worker.this.progress);
                    state = State.DONE;
                } catch(InterruptedException e){
                    state = State.CANCELED;
                } catch (Exception e){
                    state = State.ERROR;
                    exception = e;
                }
            }
        });
    }
    
    private void createProgress(Resampler resampler, Image sourceImage, Image destinationImage){
        int maxSteps = 1;
        
        if(resampler instanceof StretchResampler){
            maxSteps = ((StretchResampler)resampler).getProgressStepCount(sourceImage.getWidth(), sourceImage.getHeight(), destinationImage.getWidth(), destinationImage.getHeight());
        } else if(resampler instanceof ScaleUpResampler){
            maxSteps = ((ScaleUpResampler)resampler).getProgressStepCount(sourceImage.getWidth(), sourceImage.getHeight());
        } else if(resampler instanceof ScaleDownResampler){
            maxSteps = ((ScaleDownResampler)resampler).getProgressStepCount(sourceImage.getWidth(), sourceImage.getHeight());
        } else {
            throw new UnsupportedOperationException();
        }
        
        if(resampler instanceof cz.mg.resampler.resamplers.java.Resampler){
            progress = new cz.mg.resampler.resamplers.java.Progress(maxSteps);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     *  Cancels the resampler task and sets state to READY.
     */
    public void clear(){
        cancel();
        state = State.READY;
    }
    
    /**
     *  Cancels the resampler task.
     *  It uses both java thread interrupt and progress cancel methods.
     *  The method blocks until the worker thread finishes execution.
     */
    public void cancel(){
        if(progress != null) progress.cancel();
        thread.cancel();
        state = State.CANCELED;
    }

    /**
     *  Gets the current resampler progress.
     *  @return float value from 0 to 1
     */
    public float getProgress(){
        if(progress == null) return 0;
        progress.requestUpdate();
        return progress.getProgress();
    }

    /**
     *  Gets the current state of the worker thread.
     *  @return the current state of the worker thread
     */
    public State getState() {
        return state;
    }

    /**
     *  Gets an exception in case the resampler result state is ERROR.
     *  Note: interrupted exception sets the CANCELED state, not an ERROR state, so the interrupted exception is not saved!
     *  @return the exception that caused the worker thread to stop
     */
    public Exception getException() {
        return exception;
    }
    
    /**
     *  Enumeration of all possible worker states.
     */
    public static enum State {
        READY,
        RUNNING,
        DONE,
        CANCELED,
        ERROR
    }
    
    /**
     *  Thread class that can be reused unlike traditional java thread class that cannot.
     */
    private static class ReusableThread {
       private Thread thread = null;

       public ReusableThread() {
       }

       /**
        *  Starts a new thread, running given task in it.
        *  @param task: instance of runnable that shall be run in the thread
        */
       public void start(Runnable task) {
           cancel();
           thread = new Thread(task);
           thread.start();
       }

       /**
        *  Interrupts the running thread and waits until it finishes execution.
        *  Does nothing if the thread is not running.
        */
       public void cancel(){
           if(thread == null) return;
           thread.interrupt();
           join();
           thread = null;
       }

       private void join(){
           if(Thread.currentThread() == thread) return;
           try {
               thread.join();
           } catch (InterruptedException e) {
               System.out.println("THIS SHALL NOT HAPPEN");
           }
       }
    }
}
