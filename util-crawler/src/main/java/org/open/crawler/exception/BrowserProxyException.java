package org.open.crawler.exception;

/**
 * 自定义 BrowserProxy 例外类
 * @author peng
 * @version $Id: BrowserProxyException.java,v 1.1 2010/01/21 02:47:42 wyp Exp $
 */
public class BrowserProxyException extends Exception 
{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7198723294050124120L;

	/**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public BrowserProxyException() {
	super();
    }

	/**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
    public BrowserProxyException(String message) {
	super(message);
    }

    /**
     * Constructs a new TCrawlException with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this TCrawlException's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public BrowserProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new TCrawlException with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for TCrawlExceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionTCrawlException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public BrowserProxyException(Throwable cause) {
        super(cause);
    }
}
