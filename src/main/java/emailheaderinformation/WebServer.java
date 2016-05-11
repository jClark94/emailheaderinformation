package emailheaderinformation;

import fi.iki.elonen.NanoHTTPD;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by jaclark on 11/05/16.
 */
public class WebServer extends NanoHTTPD {
  private String mPage;

  /**
   * Override this to customize the server.
   * <p/>
   * <p/>
   * (By default, this returns a 404 "Not Found" plain text error response.)
   *
   * @param session
   *     The HTTP session
   *
   * @return HTTP response, see class Response for details
   */
  @Override public Response serve (IHTTPSession session) {
    return newFixedLengthResponse(mPage);

  }

  /**
   * Constructs an HTTP server on given port.
   *
   * @param port
   */
  public WebServer (Component component, String page, int port) throws IOException {
    super(port);
    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    JOptionPane.showMessageDialog(component,
                                  String.format("Running! Point your broswers to " +
                                                "http://localhost:%d/", port));
    mPage = page;
  }

}
