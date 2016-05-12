package emailheaderinformation;

import fi.iki.elonen.NanoHTTPD;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;

class WebServer extends NanoHTTPD {
  private String mPage;

  /**
   * Constructs an HTTP server on given port.
   *
   * @param port
   */
  WebServer (Component component, String page, int port) throws IOException {
    super(port);
    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    JOptionPane.showMessageDialog(component,
                                  String.format("Running! Point your broswers to " +
                                                "http://localhost:%d/", port));
    mPage = page;
  }

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
    switch (session.getUri()) {
      case "/world-50m.json":
        try {
          File file = new File(this.getClass().getResource("/world-50m.json").toURI());
          try (InputStream in = new FileInputStream(file)) {
            try (InputStreamReader isr = new InputStreamReader(in)) {
              try (BufferedReader br = new BufferedReader(isr)) {
                StringBuilder sb = new StringBuilder();
                br.lines().forEach(sb:: append);
                return newFixedLengthResponse(sb.toString());
              }
            }
          }
        } catch (IOException | URISyntaxException e) {
          e.printStackTrace();
        }
        break;
      default:
        return newFixedLengthResponse(mPage);
    }
    return null;
  }

}
