package win32;

/**
 * Exception class for the GuiAutomation class.
 */
public class GuiAutomationException extends RuntimeException {
    /**
     * Constructs a new GuiAutomationException with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link RuntimeException#initCause}.
     */
    public GuiAutomationException() {
    }

    /**
     * Constructs a new GuiAutomationException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link RuntimeException#initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public GuiAutomationException(String message) {
        super(message);
    }

    /**
     * Constructs a new GuiAutomationException with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public GuiAutomationException(String message, Throwable cause) {
        super(message, cause);
    }
}
