//import org.eclipse.swt.SWT;
//import org.eclipse.swt.browser.Browser;
//import org.eclipse.swt.browser.CloseWindowListener;
//import org.eclipse.swt.browser.LocationEvent;
//import org.eclipse.swt.browser.LocationListener;
//import org.eclipse.swt.browser.OpenWindowListener;
//import org.eclipse.swt.browser.ProgressEvent;
//import org.eclipse.swt.browser.ProgressListener;
//import org.eclipse.swt.browser.StatusTextEvent;
//import org.eclipse.swt.browser.StatusTextListener;
//import org.eclipse.swt.browser.WindowEvent;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.FormAttachment;
//import org.eclipse.swt.layout.FormData;
//import org.eclipse.swt.layout.FormLayout;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//
///**
// * This class implements a web browser
// */
//public class TestBrowser {
//    // The "at rest" text of the throbber
//
//    private Label throbber;
//
//    private Button button;
//
//    private Combo url;
//
//    private Button button_4;
//
//    private Button button_3;
//
//    private Browser browser;
//
//    private Browser browser2;
//
//    private Label status;
//
//    private Button button_2;
//
//    private Button button_1;
//
//    private static final String AT_REST = "Ready";
//
//    private String[] urlList = new String[256];
//
//    int urlListItemCount = 0;
//
//    /**
//     * Runs the application
//     * 
//     * @param location
//     * the initial location to display
//     */
//    public void run(String location) {
//        Display display = new Display();
//        Shell shell = new Shell(display);
//        shell.setText("Advanced Browser");
//        createContents(shell, location);
//        shell.open();
//        while (!shell.isDisposed()) {
//            if (!display.readAndDispatch()) {
//                display.sleep();
//            }
//        }
//        display.dispose();
//    }
//
//    /**
//     * Creates the main window's contents
//     * 
//     * @param shell
//     * the main window
//     * @param location
//     * the initial location
//     */
//    public void createContents(Shell shell, String location) {
//        shell.setLayout(new FormLayout());
//
//        // Create the composite to hold the buttons and text field
//
//        Composite controls = new Composite(shell, SWT.NONE);
//        FormData data = new FormData();
//        data.top = new FormAttachment(0, 0);
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(100, 0);
//        controls.setLayoutData(data);
//
//        // Create the status bar
//
//        status = new Label(shell, SWT.NONE);
//        data = new FormData();
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(100, 0);
//        data.bottom = new FormAttachment(100, 0);
//        status.setLayoutData(data);
//
//        // Create the web browser
//
//        browser = new Browser(shell, SWT.BORDER);
//        browser.addOpenWindowListener(new OpenWindowListener() {
//            public void open(WindowEvent arg0) {
//                String urlStr = arg0.browser.getUrl();
//                int flag = 1;
//                browser.setVisible(false);
//                browser2.setVisible(true);
//                arg0.browser = browser2;
//                for (int i = 0; i < urlListItemCount; i++) {
//                    if (urlList[i].equals(urlStr)) {
//                        flag = 0;
//                    }
//                }
//                if (flag == 1) {
//                    urlList[urlListItemCount] = urlStr;
//                    url.add(urlStr);
//                    url.setText(urlStr);
//                    urlListItemCount++;
//                }
//            }
//        });
//        data = new FormData();
//        data.top = new FormAttachment(controls);
//        data.bottom = new FormAttachment(status);
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(100, 0);
//        browser.setLayoutData(data);
//
//        // Create the web browser
//
//        browser2 = new Browser(shell, SWT.BORDER);
//        browser2.addOpenWindowListener(new OpenWindowListener() {
//            public void open(WindowEvent arg0) {
//                String urlStr = arg0.browser.getUrl();
//                int flag = 1;
//                browser2.setVisible(false);
//                browser.setVisible(true);
//                arg0.browser = browser;
//                for (int i = 0; i < urlListItemCount; i++) {
//                    if (urlList[i].equals(urlStr)) {
//                        flag = 0;
//                    }
//                }
//                if (flag == 1) {
//                    urlList[urlListItemCount] = urlStr;
//                    url.add(urlStr);
//                    url.setText(urlStr);
//                    urlListItemCount++;
//                }
//            }
//        });
//        data = new FormData();
//        data.top = new FormAttachment(controls);
//        data.bottom = new FormAttachment(status);
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(100, 0);
//        browser2.setLayoutData(data);
//
//        // Create the controls and wire them to the browser
//
//        controls.setLayout(new GridLayout(7, false));
//
//        // Create the back button
//
//        button_1 = new Button(controls, SWT.PUSH);
//        button_1.setText("Back");
//        button_1.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                browser.back();
//            }
//        });
//
//        // Create the forward button
//
//        button_2 = new Button(controls, SWT.PUSH);
//        button_2.setText("Forward");
//        button_2.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                browser.forward();
//            }
//        });
//
//        // Create the refresh button
//
//        button_3 = new Button(controls, SWT.PUSH);
//        button_3.setText("Refresh");
//        button_3.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                browser.refresh();
//            }
//        });
//
//        // Create the stop button
//
//        button_4 = new Button(controls, SWT.PUSH);
//        button_4.setText("Stop");
//        button_4.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                browser.stop();
//            }
//        });
//
//        // Create the address entry field and set focus to it
//
//        url = new Combo(controls, SWT.ARROW_DOWN);
//        url.setLayoutData(new GridData(296, SWT.DEFAULT));
//        url.setSize(600, url.getSize().y);
//        url.setFocus();
//
//        // Create the go button
//
//        button = new Button(controls, SWT.PUSH);
//        button.setText("Go");
//        button.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                String urlStr = url.getText();
//                int flag = 1;
//                browser.setUrl(urlStr);
//                for (int i = 0; i < urlListItemCount; i++) {
//                    if (urlList[i].equals(urlStr)) {
//                        flag = 0;
//                    }
//                }
//                if (flag == 1) {
//                    urlList[urlListItemCount] = urlStr;
//                    url.add(urlStr);
//                    urlListItemCount++;
//                }
//            }
//        });
//
//        // Create the animated "throbber"
//
//        throbber = new Label(controls, SWT.NONE);
//        throbber.setText(AT_REST);
//
//        // Allow users to hit enter to go to the typed URL
//
//        shell.setDefaultButton(button);
//
//        // Add event handlers
//
//        browser.addCloseWindowListener(new AdvancedCloseWindowListener());
//        browser.addLocationListener(new AdvancedLocationListener(url));
//        browser.addProgressListener(new AdvancedProgressListener(throbber));
//        browser.addStatusTextListener(new AdvancedStatusTextListener(status));
//
//        // Go to the initial URL
//
//        if (location != null) {
//            browser.setUrl(location);
//        }
//    }
//
//    /**
//     * This class implements a CloseWindowListener for TestBrowser
//     */
//    class AdvancedCloseWindowListener implements CloseWindowListener {
//        /**
//         * Called when the parent window should be closed
//         */
//        public void close(WindowEvent event) {
//            // Close the parent window
//
//            ((Browser) event.widget).getShell().close();
//        }
//    }
//
//    /**
//     * This class implements a LocationListener for TestBrowser
//     */
//    class AdvancedLocationListener implements LocationListener {
//        // The address text box to update
//
//        private Combo location;
//
//        /**
//         * Constructs an AdvancedLocationListener
//         * 
//         * @param text
//         * the address text box to update
//         */
//        public AdvancedLocationListener(Combo text) {
//            // Store the address box for updates
//
//            location = text;
//        }
//
//        /**
//         * Called before the location changes
//         * 
//         * @param event
//         * the event
//         */
//        public void changing(LocationEvent event) {
//            // Show the location that's loading
//
//            location.setText("Loading " + event.location + "...");
//        }
//
//        /**
//         * Called after the location changes
//         * 
//         * @param event
//         * the event
//         */
//        public void changed(LocationEvent event) {
//            // Show the loaded location
//
//            location.setText(event.location);
//        }
//    }
//
//    /**
//     * This class implements a ProgressListener for TestBrowser
//     */
//    class AdvancedProgressListener implements ProgressListener {
//        // The label on which to report progress
//
//        private Label progress;
//
//        /**
//         * Constructs an AdvancedProgressListener
//         * 
//         * @param label
//         * the label on which to report progress
//         */
//        public AdvancedProgressListener(Label label) {
//            // Store the label on which to report updates
//
//            progress = label;
//        }
//
//        /**
//         * Called when progress is made
//         * 
//         * @param event
//         * the event
//         */
//        public void changed(ProgressEvent event) {
//            // Avoid divide-by-zero
//
//            if (event.total != 0) {
//                // Calculate a percentage and display it
//
//                int percent = (int) (event.current / event.total);
//                progress.setText(percent + "%");
//            } else {
//                // Since we can't calculate a percent, show confusion :-)
//
//                progress.setText("???");
//            }
//        }
//
//        /**
//         * Called when load is complete
//         * 
//         * @param event
//         * the event
//         */
//        public void completed(ProgressEvent event) {
//            // Reset to the "at rest" message
//
//            progress.setText(AT_REST);
//        }
//    }
//
//    /**
//     * This class implements a StatusTextListener for TestBrowser
//     */
//    class AdvancedStatusTextListener implements StatusTextListener {
//        // The label on which to report status
//
//        private Label status;
//
//        /**
//         * Constructs an AdvancedStatusTextListener
//         * 
//         * @param label
//         * the label on which to report status
//         */
//        public AdvancedStatusTextListener(Label label) {
//            // Store the label on which to report status
//
//            status = label;
//        }
//
//        /**
//         * Called when the status changes
//         * 
//         * @param event
//         * the event
//         */
//        public void changed(StatusTextEvent event) {
//            // Report the status
//
//            status.setText(event.text);
//        }
//    }
//
//    /**
//     * The application entry point
//     * 
//     * @param args
//     * the command line arguments
//     */
//    public static void main(String[] args) {
//        new TestBrowser().run(args.length == 0 ? null : args[0]);
//    }
//}
