package cz.mg.resampler;


/**
 *  This interface is used to retrieve the current progress of a resampler task.
 *  The retrieved progress information is only tentative and should not be relied on.
 */
public interface Progress {
    /**
     *  Gets the current thread progress.
     *  The progress value does not change until requestUpdate followed by step has been called.
     *  @return float value from 0 to 1
     */
    public float getProgress();

    /**
     *  Requests update of the progress property.
     *  This method shall be called only in the parent thread.
     */
    public void requestUpdate();
    
    /**
     *  Gets the maximum number of steps of the resampler task.
     */
    public int getMaxSteps();
    
    /**
     *  An alternative way to interrupt the resampler task.
     *  (weird java bug fix when interrup does not set interrupted flag for unknown reason)
     *  This method shall be called only in the parent thread.
     */
    public void cancel();
}
